import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Bill } from '../models/bill.model';

@Injectable({
  providedIn: 'root',
})
export class BillingService {
  private apiUrl = '/billing-service/api/bills'; // Via Gateway (proxy)

  constructor(private http: HttpClient) {}

  getBillById(id: number): Observable<Bill> {
    return this.http.get<Bill>(`${this.apiUrl}/${id}`);
  }
}
