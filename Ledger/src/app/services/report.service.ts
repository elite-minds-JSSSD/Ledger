import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface GenerateReportRequest {
  reportName: string;
  reportType: 'PDF' | 'EXCEL';
  dateRangeLabel?: string;
}

@Injectable({
  providedIn: 'root'
})
export class ReportService {

  private readonly baseUrl = 'http://localhost:8080/api/reports';

  constructor(private http: HttpClient) { }

  getAll(): Observable<any> {
    return this.http.get<any>(this.baseUrl);
  }

  getById(id: number): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/${id}`);
  }

  generate(request: GenerateReportRequest): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/generate`, request);
  }

  download(id: number): Observable<Blob> {
    return this.http.get(`${this.baseUrl}/${id}/download`, {
      responseType: 'blob'
    });
  }

  delete(id: number): Observable<any> {
    return this.http.delete<any>(`${this.baseUrl}/${id}`);
  }
}
