import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ReconciliationRequest {
  startDate: string;
  endDate: string;
  toleranceAmount?: number;
}

@Injectable({
  providedIn: 'root'
})
export class ReconciliationService {

  private readonly baseUrl = 'http://localhost:8080/api/reconciliation';

  constructor(private http: HttpClient) { }

  runReconciliation(request: ReconciliationRequest): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/run`, request);
  }

  getResults(page: number = 0, size: number = 20): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/results`, {
      params: { page, size }
    });
  }

  getById(id: number): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/${id}`);
  }
}
