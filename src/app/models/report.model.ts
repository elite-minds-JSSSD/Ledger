export interface Report {
  id: number;
  reportName: string;
  generatedBy: string;
  generatedDate: Date;
  reportType: 'PDF' | 'EXCEL';
  totalTransactions: number;
  matchedTransactions: number;
  unmatchedTransactions: number;
  downloadUrl: string;
}