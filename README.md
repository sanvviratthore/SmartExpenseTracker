# FDA 510(k) Engine — Phase 2

A local Retrieval-Augmented Generation (RAG) engine that indexes FDA 510(k) summary PDFs and enables semantic search and AI-assisted draft generation over the corpus.

---

## Requirements

- Python 3.11
- Ollama installed and running — https://ollama.com
- FDA 510(k) PDFs from Phase 1 (available on OneDrive — contact project lead for access)

---

## Setup

**1. Clone the repository**

    git clone https://github.com/sanvviratthore/fda-510k-engine
    cd fda-510k-engine

**2. Create and activate a Python 3.11 virtual environment**

    py -3.11 -m venv venv
    .\venv\Scripts\Activate.ps1        # Windows
    source venv/bin/activate            # Mac/Linux

**3. Install dependencies**

    pip install -r requirements.txt

**4. Copy .env.example to .env**

    cp .env.example .env

**5. Install Ollama and pull the required models**

    ollama pull nomic-embed-text
    ollama pull llama3.2:1b

---

## Building the ChromaDB Index from Scratch

The `chroma_index/` folder is not committed to git — it is a large derived artifact that must be built locally from the raw PDFs. Follow these steps exactly.

**Step 1 — Get the PDFs**

Download the PDF folder from OneDrive (contact project lead for access) and place it at any local path, for example:

    C:\Users\yourname\fda-510k-pdfs\

**Step 2 — Run the batch indexer**

Run the indexer in sessions of 250 PDFs at a time. This keeps memory usage low and prevents corruption if the process is interrupted:

    python cli/index_batch.py --pdf-dir "path/to/pdfs" --limit 250

Each session takes roughly 5-10 minutes. After it finishes, wait a moment for the laptop to cool, then run the same command again. The indexer is fully resumable — it reads from a local cache file (`index_cache/indexed_k_numbers.json`) and skips any PDFs already processed.

**Step 3 — Check progress**

At any point you can check how many chunks and documents are in the index:

    python check_index.py

**Step 4 — Repeat until complete**

Keep running the same command until you see:

    Total K-numbers processed: 62,885

At that point the full corpus is indexed. The final index contains approximately 1.1 million chunks.

**If the index gets corrupted** (e.g. from a hard shutdown mid-write), delete the `chroma_index/` folder and restart from Step 2. The cache file in `index_cache/` will ensure you skip PDFs already successfully indexed in previous sessions:

    # Windows
    Remove-Item -Recurse -Force chroma_index

    # Mac/Linux
    rm -rf chroma_index

Then re-run the indexer — it will rebuild only what is missing.

**Alternatively — download the pre-built index**

A pre-built `chroma_index/` folder is available on OneDrive (contact project lead). Download and place it in the project root to skip indexing entirely.

---

## Starting Ollama

The generation layer requires Ollama running as a local server. Before using the draft generator or Streamlit UI, start Ollama in a separate terminal:

    ollama serve

You will see it print "Listening on 127.0.0.1:11434". Leave this terminal open. Alternatively, open the Ollama desktop app from your system tray.

---

## Usage

**Search the corpus (CLI)**

    python cli/search.py "cardiac monitoring wireless patch"
    python cli/search.py "predicate device comparison" --section "Predicate Device" --k 5

**Generate a draft 510(k) summary (CLI)**

    python cli/draft.py "a wireless cardiac monitoring patch for continuous ECG recording in adult patients"

**Launch the Streamlit UI**

    streamlit run ui/streamlit_app.py

This opens a browser at http://localhost:8501 with two modes — semantic search over the corpus, and AI-assisted 510(k) draft generation with citation validation.

**Check index stats**

    python check_index.py

---

## Architecture

The engine is built in four layers:

**Layer 1 — Ingestion** (`fda_510k_engine/ingest.py`)
Extracts text from PDFs using PyMuPDF. Falls back to Tesseract OCR for scanned documents. Splits text into overlapping chunks of ~900 characters with section-aware metadata tagging (Device Description, Indications for Use, Predicate Device, etc.).

**Layer 2 — Indexing** (`fda_510k_engine/index.py`)
Converts chunks to vector embeddings using sentence-transformers (all-MiniLM-L6-v2) and stores them in a local ChromaDB vector store. A persistent JSON cache (`index_cache/`) tracks progress independently of ChromaDB so the process survives crashes.

**Layer 3 — Retrieval** (`fda_510k_engine/retrieve.py`)
Given a query, converts it to a vector and finds the most semantically similar chunks in ChromaDB using cosine similarity. Supports optional section filtering (e.g. only search Predicate Device sections).

**Layer 4 — Generation** (`fda_510k_engine/generate.py`)
Retrieves relevant chunks, formats them as context, and prompts a locally-running Llama model via Ollama to draft a structured 510(k) summary. Citation validation ensures only K-numbers present in the retrieved context appear in the output.

---

## Project Structure

    fda_510k_engine/
        ingest.py      — Layer 1: PDF text extraction and chunking
        index.py       — Layer 2: embedding generation and ChromaDB storage
        retrieve.py    — Layer 3: semantic search over the corpus
        generate.py    — Layer 4: LLM draft generation with citation validation
        prompts.py     — prompt templates
        validate.py    — citation checking and watermarking
        cache.py       — crash-safe persistent progress cache

    cli/
        index_batch.py — resumable batch indexer
        search.py      — semantic search CLI
        draft.py       — draft generation CLI

    ui/
        streamlit_app.py — Streamlit web interface

    data/           — PDFs go here (not committed to git)
    chroma_index/   — ChromaDB vector store (not committed to git)
    index_cache/    — indexing progress cache (not committed to git)

---

## Hardware Notes

The embedding model (all-MiniLM-L6-v2) uses approximately 500MB RAM. The Llama 3.2 1B generation model uses approximately 1.3GB RAM. Minimum recommended is 8GB RAM with other applications closed. Use `--limit 250` per indexing session on machines with thermal or memory constraints.

---

## Known Limitations

- Generation quality is limited by the 1B model size due to RAM constraints. Switching to `llama3.2:3b` in `fda_510k_engine/generate.py` improves output quality on machines with 16GB+ RAM.
- Scanned PDFs from the 1970s-2000s era have no extractable text and are skipped during indexing. OCR support via Tesseract can be re-enabled in `ingest.py` once Tesseract is installed.
- The ChromaDB index is not thread-safe for concurrent writes. Run only one indexing session at a time.