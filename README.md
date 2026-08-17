# Smart Expense Tracker — Spring Boot API

A REST API rewrite of the original Java/JavaFX desktop app. Same features
(auth, expenses, insights, savings/wishlist) re-architected as a stateless
backend with a **Controller → Service → Repository** structure.

## What changed from the original

| Original (desktop) | This version (Spring Boot) |
|---|---|
| Console + JavaFX UI | REST API (no UI — a web/mobile frontend calls these endpoints) |
| `.txt` files in `data/` | H2 database via Spring Data JPA |
| SHA-256 password hashing | BCrypt (`PasswordEncoder`) |
| In-memory `currentUser` flag | Stateless **JWT** auth (`Authorization: Bearer <token>`) |
| Manual file parsing | JPA entities + repositories |
| Static `main()` menu loop | `@RestController` endpoints |

Per-user data isolation is preserved: every expense, goal, and saving is scoped
to the authenticated username.

## Security hardening (post-review)

A code review surfaced a few real issues, since fixed:

- **H2 console was publicly reachable.** `/h2-console` was whitelisted and
  enabled unconditionally, exposing the database (including password hashes)
  on the live deployment. It's now disabled by default and gated behind
  `H2_CONSOLE_ENABLED`, with no route whitelisting for it in `SecurityConfig`.
- **JWT secret had a committed fallback.** If `JWT_SECRET` wasn't set, the app
  silently signed tokens with a placeholder string sitting in this public repo
  — anyone could have forged valid tokens. The app now fails to start if
  `JWT_SECRET` is missing, instead of falling back silently.
- **Generic exceptions leaked internals to callers.** `GlobalExceptionHandler`
  returned raw `ex.getMessage()` (potentially including SQL/constraint details)
  for any unhandled exception. It now logs the real exception server-side and
  returns a generic message to the client.
- **Duplicate-username race condition.** `existsByUsername` followed by
  `save()` isn't atomic — two concurrent registrations with the same username
  could both pass the check and hit the DB's unique constraint, surfacing as
  an ugly 500. `AuthService` now catches `DataIntegrityViolationException` and
  returns a clean 400 either way.
- **CORS was wide open** (`allowedOrigins("*")`). Narrowed to a single
  configurable origin via `APP_ALLOWED_ORIGIN`.
- **Currency fields used `double`.** `Expense.amount`, `Saving.amount`, and
  `Goal.targetAmount` are now `BigDecimal` (`precision = 19, scale = 2`) to
  avoid floating-point rounding errors in money math. `SavingService`'s
  percent-saved calculation uses `BigDecimal.divide(target, 4, RoundingMode.HALF_UP)`
  rather than `/`, since unscaled `BigDecimal` division can throw at runtime.

## Tech stack

Java 17 · Spring Boot 3.2 · Spring Web · Spring Data JPA · Spring Security ·
H2 · jjwt (JWT) · Bean Validation · Maven

## Run it

`JWT_SECRET` is **required** — the app fails fast on startup if it's missing
(no fallback secret is baked into the code or committed to the repo).

```bash
# macOS/Linux
export JWT_SECRET=a-long-random-string-at-least-32-characters

# Windows PowerShell (per-session)
$env:JWT_SECRET = "a-long-random-string-at-least-32-characters"

# Windows PowerShell (persists across terminals)
[Environment]::SetEnvironmentVariable("JWT_SECRET", "a-long-random-string-at-least-32-characters", "User")

mvn spring-boot:run
```

The API starts on `http://localhost:8080`.

H2 console is **off by default** and only reachable when you explicitly opt
in — it must never be left enabled in a public deployment:
```bash
# macOS/Linux
export H2_CONSOLE_ENABLED=true
# Windows PowerShell
$env:H2_CONSOLE_ENABLED = "true"
```
Then visit `http://localhost:8080/h2-console`
(JDBC URL `jdbc:h2:file:./data/expensetracker`, user `sa`, no password).
Leave `H2_CONSOLE_ENABLED` unset on Render/production — it defaults to `false`.

CORS is restricted to a single allowed origin, configurable via `APP_ALLOWED_ORIGIN`
(defaults to `http://localhost:8080` if unset):
```bash
export APP_ALLOWED_ORIGIN=https://your-deployed-url.onrender.com
```

## API

All `/api/**` routes except `/api/auth/**` require a JWT.

### Auth
| Method | Path | Body |
|---|---|---|
| POST | `/api/auth/register` | `{ "username": "...", "password": "..." }` |
| POST | `/api/auth/login` | `{ "username": "...", "password": "..." }` |

Both return `{ "token": "...", "username": "..." }`. Send the token as
`Authorization: Bearer <token>` on every other request.

### Expenses
| Method | Path | Notes |
|---|---|---|
| POST | `/api/expenses` | `{ "title", "amount", "category" }` — date set to today |
| GET | `/api/expenses` | list current user's expenses (newest first) |

### Insights
| Method | Path | Returns |
|---|---|---|
| GET | `/api/insights` | total, highest category + amount, category breakdown, smart insight |

### Savings & wishlist
| Method | Path | Notes |
|---|---|---|
| POST | `/api/savings/goals` | `{ "productName", "targetAmount", "productLink" }` |
| GET | `/api/savings/goals` | list wishlist goals |
| POST | `/api/savings` | `{ "amount" }` — record a saving |
| GET | `/api/savings` | total saved + all savings |
| GET | `/api/savings/insight` | per-goal % saved, `canBuy=true` once ≥ 80% |

## Quick smoke test

```bash
# register (grab the token from the response)
curl -s -X POST localhost:8080/api/auth/register \
  -H 'Content-Type: application/json' \
  -d '{"username":"sanvi","password":"secret123"}'

TOKEN=... # paste token here

# add an expense
curl -s -X POST localhost:8080/api/expenses \
  -H "Authorization: Bearer $TOKEN" -H 'Content-Type: application/json' \
  -d '{"title":"Groceries","amount":1200,"category":"Food"}'

# see insights
curl -s localhost:8080/api/insights -H "Authorization: Bearer $TOKEN"
```

## Project structure

```
src/main/java/com/expensetracker/
├── SmartExpenseTrackerApplication.java
├── config/           SecurityConfig (JWT filter chain, CORS, BCrypt)
├── security/         JwtService, JwtAuthFilter
├── common/           GlobalExceptionHandler
├── auth/             User entity, repo, AuthService, AuthController, dto/
├── expense/          Expense entity, repo, service, controller, dto/
├── insights/         InsightsService, controller, dto/
└── savings/          Goal + Saving entities, repos, service, controller, dto/
```

## Deploying (Render or similar)

Set these environment variables on the host before deploying:

| Variable | Required | Notes |
|---|---|---|
| `JWT_SECRET` | Yes | App refuses to start without it |
| `APP_ALLOWED_ORIGIN` | Recommended | Your deployed URL, for CORS |
| `H2_CONSOLE_ENABLED` | No | Leave unset — defaults to `false` |
