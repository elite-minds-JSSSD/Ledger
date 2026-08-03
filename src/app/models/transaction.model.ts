export interface Transaction {
  id: number;
  transactionId: string;
  referenceNumber: string;
  transactionDate: Date;
  description: string;
  accountName: string;
  amount: number;
  currency: string;
  type: 'DEBIT' | 'CREDIT';
  source: 'BANK' | 'LEDGER';
  status: 'MATCHED' | 'UNMATCHED' | 'PENDING';
}