import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Transaction } from '../models/transaction.model';

@Injectable({
  providedIn: 'root'
})
export class TransactionService {

  private readonly baseUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) { }

  getBankTransactions(page: number = 0, size: number = 20): Observable<any> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<any>(`${this.baseUrl}/transactions/bank`, { params });
  }

  getLedgerEntries(page: number = 0, size: number = 20): Observable<any> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<any>(`${this.baseUrl}/ledger`, { params });
  }

  getAllTransactions(page: number = 0, size: number = 20): Observable<any> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<any>(`${this.baseUrl}/transactions`, { params });
  }

  getTransactionById(id: number): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/transactions/${id}`);
  }

  createTransaction(transaction: Partial<Transaction>): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/transactions`, transaction);
  }

  deleteTransaction(id: number): Observable<any> {
    return this.http.delete<any>(`${this.baseUrl}/transactions/${id}`);
  }

  createLedgerEntry(entry: any): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/ledger`, entry);
  }

  updateLedgerEntry(id: number, entry: any): Observable<any> {
    return this.http.put<any>(`${this.baseUrl}/ledger/${id}`, entry);
  }

  deleteLedgerEntry(id: number): Observable<any> {
    return this.http.delete<any>(`${this.baseUrl}/ledger/${id}`);
  }
}
