# ⚡ Prompt Window Optimizer

A high-performance Java engine that optimizes LLM prompt contexts using classic algorithmic paradigms (**Sliding Window** and **Prefix Sum**). It guarantees $O(1)$ token range evaluations and $O(N)$ optimal window selection while strictly preserving critical instructions (`PINNED` system prompts and latest user interactions).

---

## 📌 The Problem
Sending full conversational histories to LLMs introduces critical engineering issues:
1. **Exponential Token Costs:** Paying for repetitive, stale chat turns.
2. **High Latency:** Slower time-to-first-token due to heavy prompt ingestion.
3. **Context Window Breaches:** Hitting rate limits or token exhaustion.

---

## 🏗️ Architecture & How It Works

The engine acts as an in-memory optimization filter before requests hit the LLM endpoint:

```text
                      +---------------------------------------+
                      | Raw Conversation History (500+ Tokens) |
                      +-------------------+-------------------+
                                          |
                                          v
                      +---------------------------------------+
                      |       Prompt Window Optimizer         |
                      |   [Dynamic Sliding Window Engine]     |
                      +-------------------+-------------------+
                                          |
                      +-------------------+-------------------+
                      |                                       |
                      v                                       v
         [Permanent System Context]               [Token Budget Pruner]
         * Core instructions kept               * Trims older turns
         * Never evicted                        * Retains recent turns
                      |                                       |
                      +-------------------+-------------------+
                                          |
                                          v
                      +---------------------------------------+
                      | Optimized Request Payload (97 Tokens) |
                      +-------------------+-------------------+
                                          |
                                          v
                      +---------------------------------------+
                      |        Google Gemini API Client       |
                      |          (gemini-3.8-flash)           |
                      +---------------------------------------+

## 🏗️ Architecture & How It Works

The engine acts as an in-memory optimization filter before requests hit the LLM endpoint:

```text
                      +---------------------------------------+
                      | Raw Conversation History (500+ Tokens) |
                      +-------------------+-------------------+
                                          |
                                          v
                      +---------------------------------------+
                      |       Prompt Window Optimizer         |
                      |   [Dynamic Sliding Window Engine]     |
                      +-------------------+-------------------+
                                          |
                      +-------------------+-------------------+
                      |                                       |
                      v                                       v
         [Permanent System Context]               [Token Budget Pruner]
         * Core instructions kept               * Trims older turns
         * Never evicted                        * Retains recent turns
                      |                                       |
                      +-------------------+-------------------+
                                          |
                                          v
                      +---------------------------------------+
                      | Optimized Request Payload (97 Tokens) |
                      +-------------------+-------------------+
                                          |
                                          v
                      +---------------------------------------+
                      |        Google Gemini API Client       |
                      |          (gemini-3.8-flash)           |
                      +---------------------------------------+
```
---

## 📊 Live Verification & Benchmark

In integration tests with `gemini-3.8-flash`, the optimizer dynamically pruned conversational context while preserving system rules:

| Metric | Unoptimized History | Optimized Payload | Total Savings |
| :--- | :--- | :--- | :--- |
| **Token Size** | `536 tokens` | `97 tokens` | **`81.9%` reduction** |
| **System Rules Integrity** | Preserved | Preserved | `100%` |
| **Response Quality** | Maintained | Maintained | `100%` |

> Output snippet from runtime:
> `[Tokens: 536 -> 97 | Saved: 81.9%]`

---

## ✨ Key Features
- **Pure Java (Zero Dependencies):** Built natively with JDK tools (`java.net.http.*`). No external frameworks (No LangChain, No Jackson).
- **Dual Mode:** Run directly against the live **Gemini API** or test in offline **Demo Mode** without an API key.
- **Model-Agnostic:** The algorithmic windowing logic is decoupled from transport and can be integrated into any LLM service.

---

## 🚀 Quick Start Guide

### 1. Clone & Navigate
```bash
git clone [https://github.com/DoaaAlyazgi/prompt-window-optimizer.git](https://github.com/DoaaAlyazgi/prompt-window-optimizer.git)
cd prompt-window-optimizer
