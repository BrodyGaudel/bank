import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {CustomerService} from "../../services/customer-service/customer.service";
import {catchError, Observable, throwError} from "rxjs";
import {CustomerModel} from "../../models/customer.model";

@Component({
  selector: 'app-list-customer',
  templateUrl: './list-customer.component.html',
  styleUrls: ['./list-customer.component.css']
})
export class ListCustomerComponent implements OnInit{

  keyword!: string;
  customers!: Observable<Array<CustomerModel>>;
  errorMessage!: string;
  sizePage: number = 5;
  currentPage: number =0;
  length!: number;

  constructor(private router: Router,
              private activatedRoute: ActivatedRoute,
              private customerService: CustomerService) {
  }
  ngOnInit(): void {
    this.keyword = this.activatedRoute.snapshot.params['id'];
    this.searchCustomer();
  }

  searchCustomer(): void {
    this.customers = this.customerService.searchCustomer(this.keyword, this.sizePage, this.currentPage).pipe(
      catchError(err => {
        this.errorMessage = err.message;
        return throwError(() => err);
      })
    );
    this.setTotalPage();
  }

  nextPage() : void {
    if(this.length <5){
      alert("C'est la dernière page");
    }else{
      this.currentPage = this.currentPage + 1;
      this.searchCustomer();
    }
  }

  previousPage(): void {
    if (this.currentPage > 0){
      this.currentPage = this.currentPage - 1;
      this.searchCustomer();
    }else{
      //do not activate button
      alert("Vous etes sur la premiere page");
    }
  }

  setTotalPage(): void {
    let listCustomer: CustomerModel[];
    this.customers.subscribe({
      next : (customerModels: Array<CustomerModel>) : void => {
        listCustomer = customerModels;
        this.length = listCustomer.length;
        },
      error : (err): void =>{ this.errorMessage = err}
    });

  }

  gotoShowCustomerComponent(id: string) : void{
    this.router.navigate(['show-customer', id]).then();
  }

}
