export interface Reconciliation {
  id: number;
  bankTransactionId: string;
  ledgerTransactionId: string;
  reconciliationDate: Date;
  matchStatus: 'MATCHED' | 'PARTIAL_MATCH' | 'UNMATCHED';
  matchedAmount: number;
  remarks: string;
}