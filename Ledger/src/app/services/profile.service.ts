import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface UpdateProfileRequest {
  firstName: string;
  lastName: string;
  phone?: string;
  profileImage?: string;
}

export interface ChangePasswordRequest {
  currentPassword: string;
  newPassword: string;
}

@Injectable({
  providedIn: 'root'
})
export class ProfileService {

  private readonly baseUrl = 'http://localhost:8080/api/users';

  constructor(private http: HttpClient) { }

  getProfile(): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/profile`);
  }

  updateProfile(data: UpdateProfileRequest): Observable<any> {
    return this.http.put<any>(`${this.baseUrl}/profile`, data);
  }

  changePassword(data: ChangePasswordRequest): Observable<any> {
    return this.http.put<any>(`${this.baseUrl}/password`, data);
  }
}
