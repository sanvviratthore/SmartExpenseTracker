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

## Tech stack

Java 17 · Spring Boot 3.2 · Spring Web · Spring Data JPA · Spring Security ·
H2 · jjwt (JWT) · Bean Validation · Maven

## Run it

```bash
mvn spring-boot:run
```

The API starts on `http://localhost:8080`.
H2 console: `http://localhost:8080/h2-console`
(JDBC URL `jdbc:h2:file:./data/expensetracker`, user `sa`, no password).

> Set a strong secret in production: `export JWT_SECRET=your-long-random-secret`

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

## Notes & next steps

- Passwords now use BCrypt, so old `users.txt` credentials do not carry over —
  users re-register once.
- To swap H2 for PostgreSQL, change the `spring.datasource.*` properties and add
  the Postgres driver; no code changes needed.
- Suggested follow-ups: pagination on expense listing, a `POST /api/auth/refresh`
  endpoint, and integration tests with `spring-security-test`.
