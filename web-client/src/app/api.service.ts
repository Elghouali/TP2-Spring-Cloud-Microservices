import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
export interface Customer {id: number; name: string; email: string}
export interface Product {id: string; name: string; price: number; quantity: number}
export interface BillItem {id: number; productId: string; productName: string; quantity: number; unitPrice: number; lineTotal: number}
export interface Bill {id: number; customerId: number; customerName: string; customerEmail: string; issuedAt: string; total: number; currency: string; items: BillItem[]}
export interface BillInput {customerId: number; items: {productId: string; quantity: number}[]}
@Injectable({providedIn:'root'})
export class ApiService {
  private readonly http=inject(HttpClient);
  customers():Observable<Customer[]>{return this.http.get<Customer[]>('/api/customers');}
  products():Observable<Product[]>{return this.http.get<Product[]>('/api/products');}
  bills():Observable<Bill[]>{return this.http.get<Bill[]>('/api/bills');}
  customer(input:{name:string;email:string}):Observable<Customer>{return this.http.post<Customer>('/api/customers',input);}
  product(input:{name:string;price:number;quantity:number}):Observable<Product>{return this.http.post<Product>('/api/products',input);}
  bill(input:BillInput):Observable<Bill>{return this.http.post<Bill>('/api/bills',input);}
}
