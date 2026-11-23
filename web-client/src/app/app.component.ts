import {CommonModule} from '@angular/common';
import {Component, OnInit, inject} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {ApiService, Bill, Customer, Product} from './api.service';
@Component({selector:'app-root',standalone:true,imports:[CommonModule,FormsModule],templateUrl:'./app.component.html'})
export class AppComponent implements OnInit {
  readonly api=inject(ApiService);
  section:'overview'|'customers'|'products'|'bills'='overview';
  customers:Customer[]=[]; products:Product[]=[]; bills:Bill[]=[];
  busy=false; error=''; success='';
  newCustomer={name:'',email:''};
  newProduct={name:'',price:100,quantity:10};
  selectedCustomerId=0; selectedProductId=''; selectedQuantity=1;
  selectedBill:Bill|null=null;
  ngOnInit(){this.reload();}
  get totalRevenue(){return this.bills.reduce((s,b)=>s+Number(b.total),0);}
  get totalUnits(){return this.bills.reduce((s,b)=>s+b.items.reduce((n,x)=>n+x.quantity,0),0);}
  money(amount:number){return new Intl.NumberFormat('fr-MA',{style:'currency',currency:'MAD'}).format(amount);}
  time(value:string){return new Date(value).toLocaleString('fr-MA');}
  reload(){
    this.error='';
    this.api.customers().subscribe({next:x=>{this.customers=x;if(!this.selectedCustomerId&&x.length)this.selectedCustomerId=x[0].id;},error:e=>this.fail(e)});
    this.api.products().subscribe({next:x=>{this.products=x;if(!this.selectedProductId&&x.length)this.selectedProductId=x[0].id;},error:e=>this.fail(e)});
    this.api.bills().subscribe({next:x=>this.bills=x,error:e=>this.fail(e)});
  }
  fail(e:unknown){const err=e as {error?:{detail?:string;message?:string};message?:string};this.error=err?.error?.detail||err?.error?.message||err?.message||'Service indisponible';this.busy=false;}
  private completed(message:string){this.success=message;this.error='';this.busy=false;this.reload();}
  addCustomer(){if(!this.newCustomer.name||!this.newCustomer.email)return;this.busy=true;
    this.api.customer(this.newCustomer).subscribe({next:()=>{this.newCustomer={name:'',email:''};this.completed('Client créé avec succès');},error:e=>this.fail(e)});}
  addProduct(){if(!this.newProduct.name||this.newProduct.price<=0||this.newProduct.quantity<0)return;this.busy=true;
    this.api.product(this.newProduct).subscribe({next:()=>{this.newProduct={name:'',price:100,quantity:10};this.completed('Produit créé avec succès');},error:e=>this.fail(e)});}
  addBill(){if(!this.selectedCustomerId||!this.selectedProductId||this.selectedQuantity<=0)return;this.busy=true;
    this.api.bill({customerId:Number(this.selectedCustomerId),items:[{productId:this.selectedProductId,quantity:this.selectedQuantity}]}).subscribe({next:b=>{this.selectedBill=b;this.completed('Facture #'+b.id+' créée');},error:e=>this.fail(e)});}
  showBill(b:Bill){this.selectedBill=b;this.section='bills';}
}
