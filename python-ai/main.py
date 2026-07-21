"""
LedgerMatch - Python AI Service
Powered by Groq (llama-3.1-70b-versatile)
Endpoints:
  POST /ai/reconcile              - Smart reconciliation matching
  POST /ai/discrepancy-analysis   - Discrepancy pattern analysis
  POST /ai/report-summary         - Natural language report summary
"""

import os
import json
from typing import Any, Dict, List, Optional

from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from dotenv import load_dotenv
from groq import Groq

load_dotenv()

# ──────────────────────────────────────────────
# Groq Client
# ──────────────────────────────────────────────
GROQ_API_KEY = os.getenv("GROQ_API_KEY")
if not GROQ_API_KEY:
    raise RuntimeError("GROQ_API_KEY is not set in .env file")

groq_client = Groq(api_key=GROQ_API_KEY)
MODEL = "llama-3.1-70b-versatile"

# ──────────────────────────────────────────────
# FastAPI App
# ──────────────────────────────────────────────
app = FastAPI(
    title="LedgerMatch AI Service",
    description="Groq-powered AI service for Ledger Reconciliation (TechWing)",
    version="1.0.0",
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:4200", "http://localhost:8080"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# ──────────────────────────────────────────────
# Pydantic Models
# ──────────────────────────────────────────────

class TransactionItem(BaseModel):
    id: Optional[str] = None
    transactionId: Optional[str] = None
    referenceNumber: Optional[str] = None
    transactionDate: Optional[str] = None
    description: Optional[str] = None
    amount: Optional[float] = None
    type: Optional[str] = None  # DEBIT / CREDIT
    source: Optional[str] = None  # BANK / LEDGER


class ReconcileRequest(BaseModel):
    bankTransactions: List[TransactionItem]
    ledgerTransactions: List[TransactionItem]


class DiscrepancyItem(BaseModel):
    id: Optional[str] = None
    transactionId: Optional[str] = None
    discrepancyType: Optional[str] = None
    expectedAmount: Optional[float] = None
    actualAmount: Optional[float] = None
    difference: Optional[float] = None
    status: Optional[str] = None
    comments: Optional[str] = None


class DiscrepancyAnalysisRequest(BaseModel):
    discrepancies: List[DiscrepancyItem]


class ReportSummaryRequest(BaseModel):
    reportName: Optional[str] = None
    dateRange: Optional[str] = None
    totalTransactions: Optional[int] = None
    matchedTransactions: Optional[int] = None
    unmatchedTransactions: Optional[int] = None
    discrepancySummary: Optional[Dict[str, int]] = None
    generatedBy: Optional[str] = None


# ──────────────────────────────────────────────
# Helper: Groq Chat Completion
# ──────────────────────────────────────────────
def groq_chat(system_prompt: str, user_message: str) -> str:
    try:
        response = groq_client.chat.completions.create(
            model=MODEL,
            messages=[
                {"role": "system", "content": system_prompt},
                {"role": "user", "content": user_message},
            ],
            temperature=0.2,
            max_tokens=2048,
        )
        return response.choices[0].message.content
    except Exception as e:
        raise HTTPException(status_code=502, detail=f"Groq API error: {str(e)}")


# ──────────────────────────────────────────────
# Endpoint 1: Smart Reconciliation
# POST /ai/reconcile
# ──────────────────────────────────────────────
@app.post("/ai/reconcile")
async def ai_reconcile(request: ReconcileRequest) -> Dict[str, Any]:
    """
    Given bank and ledger transactions, use Groq LLM to suggest
    probable matches even for transactions with slight mismatches.
    Returns a list of suggested matches with confidence scores.
    """
    system_prompt = """You are an expert financial reconciliation AI.
Your job is to match bank transactions with internal ledger entries.

Rules:
1. Match primarily by referenceNumber (exact or partial).
2. If no reference match, match by amount proximity (within 2%) and date proximity (within 3 days).
3. Description similarity is a secondary signal.
4. Assign a confidence score: HIGH (>90%), MEDIUM (60–90%), LOW (<60%).
5. Explain WHY each match was made.

IMPORTANT: Return ONLY valid JSON. No extra text, no markdown.
Format:
{
  "matches": [
    {
      "bankTransactionId": "...",
      "ledgerTransactionId": "...",
      "confidence": "HIGH|MEDIUM|LOW",
      "matchReason": "...",
      "amountDifference": 0.00,
      "suggestedStatus": "MATCHED|PARTIAL_MATCH|UNMATCHED"
    }
  ],
  "unmatchedBank": ["transactionId1", ...],
  "unmatchedLedger": ["entryId1", ...],
  "summary": "..."
}"""

    bank_data = [t.model_dump() for t in request.bankTransactions[:50]]  # limit to 50 rows
    ledger_data = [t.model_dump() for t in request.ledgerTransactions[:50]]

    user_message = f"""Bank Transactions (max 50):
{json.dumps(bank_data, indent=2)}

Ledger Entries (max 50):
{json.dumps(ledger_data, indent=2)}

Please reconcile and return JSON only."""

    raw = groq_chat(system_prompt, user_message)

    try:
        result = json.loads(raw)
    except json.JSONDecodeError:
        # Attempt to extract JSON from response
        import re
        json_match = re.search(r'\{.*\}', raw, re.DOTALL)
        if json_match:
            result = json.loads(json_match.group())
        else:
            result = {"raw_response": raw, "parse_error": True}

    return {
        "success": True,
        "message": "AI reconciliation complete",
        "data": result
    }


# ──────────────────────────────────────────────
# Endpoint 2: Discrepancy Analysis
# POST /ai/discrepancy-analysis
# ──────────────────────────────────────────────
@app.post("/ai/discrepancy-analysis")
async def ai_discrepancy_analysis(request: DiscrepancyAnalysisRequest) -> Dict[str, Any]:
    """
    Analyze a list of discrepancies to:
    - Identify patterns (e.g., systematic errors, recurring mismatches)
    - Prioritize which to resolve first
    - Suggest root causes
    - Recommend actions
    """
    system_prompt = """You are a financial audit expert AI.
Analyze the given list of transaction discrepancies and provide:
1. Pattern analysis (are there recurring discrepancy types?)
2. Priority order for resolution (which are most critical?)
3. Probable root causes for each discrepancy type
4. Recommended actions to resolve them
5. Risk assessment

IMPORTANT: Return ONLY valid JSON. No extra text.
Format:
{
  "patterns": [
    {"type": "...", "count": 0, "description": "..."}
  ],
  "prioritized": [
    {"transactionId": "...", "priority": "HIGH|MEDIUM|LOW", "reason": "..."}
  ],
  "rootCauses": {
    "AMOUNT_MISMATCH": "...",
    "MISSING_BANK_ENTRY": "...",
    "MISSING_LEDGER_ENTRY": "...",
    "DUPLICATE_TRANSACTION": "..."
  },
  "recommendations": ["...", "..."],
  "riskLevel": "HIGH|MEDIUM|LOW",
  "summary": "..."
}"""

    data = [d.model_dump() for d in request.discrepancies[:100]]  # limit to 100
    user_message = f"""Discrepancies to analyze:
{json.dumps(data, indent=2)}

Provide analysis as JSON only."""

    raw = groq_chat(system_prompt, user_message)

    try:
        result = json.loads(raw)
    except json.JSONDecodeError:
        import re
        json_match = re.search(r'\{.*\}', raw, re.DOTALL)
        result = json.loads(json_match.group()) if json_match else {"raw_response": raw, "parse_error": True}

    return {
        "success": True,
        "message": "AI discrepancy analysis complete",
        "data": result
    }


# ──────────────────────────────────────────────
# Endpoint 3: Report Summary
# POST /ai/report-summary
# ──────────────────────────────────────────────
@app.post("/ai/report-summary")
async def ai_report_summary(request: ReportSummaryRequest) -> Dict[str, Any]:
    """
    Generate a natural language executive summary of a reconciliation report.
    Used for the Audit Reports section of the frontend.
    """
    system_prompt = """You are a senior financial controller writing executive summaries.
Given reconciliation report statistics, generate a professional, concise executive summary.
The summary should:
- Be 3–5 sentences, professional tone
- Highlight match rate and key concerns
- Note any significant discrepancies
- End with a risk or compliance note if needed

Return ONLY valid JSON:
{
  "executiveSummary": "...",
  "matchRate": 0.0,
  "keyFindings": ["...", "..."],
  "riskFlag": "NONE|LOW|MEDIUM|HIGH",
  "complianceNote": "..."
}"""

    user_message = f"""Report details:
- Report Name: {request.reportName}
- Date Range: {request.dateRange}
- Total Transactions: {request.totalTransactions}
- Matched Transactions: {request.matchedTransactions}
- Unmatched Transactions: {request.unmatchedTransactions}
- Discrepancy Summary: {json.dumps(request.discrepancySummary)}
- Generated By: {request.generatedBy}

Generate executive summary as JSON only."""

    raw = groq_chat(system_prompt, user_message)

    try:
        result = json.loads(raw)
    except json.JSONDecodeError:
        import re
        json_match = re.search(r'\{.*\}', raw, re.DOTALL)
        result = json.loads(json_match.group()) if json_match else {"raw_response": raw, "parse_error": True}

    # Calculate match rate if possible
    if request.totalTransactions and request.totalTransactions > 0:
        match_rate = round((request.matchedTransactions / request.totalTransactions) * 100, 2)
        result["matchRate"] = match_rate

    return {
        "success": True,
        "message": "AI report summary generated",
        "data": result
    }


# ──────────────────────────────────────────────
# Health Check
# ──────────────────────────────────────────────
@app.get("/health")
async def health_check():
    return {"status": "healthy", "service": "LedgerMatch AI Service", "model": MODEL}


@app.get("/")
async def root():
    return {
        "service": "LedgerMatch AI Service (Groq)",
        "version": "1.0.0",
        "docs": "/docs",
        "endpoints": ["/ai/reconcile", "/ai/discrepancy-analysis", "/ai/report-summary"]
    }
