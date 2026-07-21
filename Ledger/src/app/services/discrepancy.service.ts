import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class DiscrepancyService {

    private readonly baseUrl = 'http://localhost:8080/api/discrepancies';

    constructor(private http: HttpClient) { }

    getAll(status?: string): Observable<any> {
        const params: any = {};
        if (status) params['status'] = status;
        return this.http.get<any>(this.baseUrl, { params });
    }

    getById(id: number): Observable<any> {
        return this.http.get<any>(`${this.baseUrl}/${id}`);
    }

    resolve(id: number, comments: string): Observable<any> {
        return this.http.put<any>(`${this.baseUrl}/${id}/resolve`, { comments });
    }
}
