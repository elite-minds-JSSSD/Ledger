import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface DashboardStats {
  totalTransactions: number;
  matchedTransactions: number;
  unmatchedTransactions: number;
  openDiscrepancies: number;
  matchRate: number;
  totalReports: number;
}

export interface RecentActivity {
  action: string;
  entity: string;
  entityId: string;
  performedBy: string;
  timestamp: string;
  details: string;
}

@Injectable({
  providedIn: 'root'
})
export class DashboardService {

  private readonly baseUrl = 'http://localhost:8080/api/dashboard';

  constructor(private http: HttpClient) { }

  getStats(): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/stats`);
  }

  getRecentActivity(): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/recent-activity`);
  }
}
