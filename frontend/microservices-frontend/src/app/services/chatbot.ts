import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ChatbotService {
  private apiUrl = '/chatbot/chat'; // Via Gateway (proxy)
  private httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'text/plain'
    })
  };

  constructor(private http: HttpClient) {}

  sendMessage(query: string): Observable<string> {
    const params = new HttpParams().set('query', query);
    return this.http.get(this.apiUrl, { 
      ...this.httpOptions, 
      params,
      responseType: 'text'
    });
  }
}

