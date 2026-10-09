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


