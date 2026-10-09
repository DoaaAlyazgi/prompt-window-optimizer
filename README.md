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
