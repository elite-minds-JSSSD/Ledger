export interface Discrepancy {
  id: number;
  transactionId: string;
  discrepancyType:
    | 'AMOUNT_MISMATCH'
    | 'MISSING_BANK_ENTRY'
    | 'MISSING_LEDGER_ENTRY'
    | 'DUPLICATE_TRANSACTION';
  expectedAmount: number;
  actualAmount: number;
  difference: number;
  status: 'OPEN' | 'RESOLVED';
  comments?: string;
}