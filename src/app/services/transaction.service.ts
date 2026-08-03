import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class TransactionService {

  private apiUrl = `${environment.apiUrl}/ledger`;

  constructor(private http: HttpClient) { }

  // Example: Get all ledger entries
  getLedgerEntries(): Observable<any> {
    return this.http.get(this.apiUrl);
  }

  // Example: Get entry by ID
  getLedgerEntryById(id: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/${id}`);
  }
}
