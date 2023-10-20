import {Component, OnInit} from '@angular/core';
import {CustomerService} from "../../services/customer-service/customer.service";
import {ActivatedRoute, Router} from "@angular/router";
import {CustomerPageModel} from "../../models/customerPage.model";
import {CustomerModel} from "../../models/customer.model";

@Component({
  selector: 'app-list-customers',
  templateUrl: './list-customers.component.html',
  styleUrls: ['./list-customers.component.css']
})
export class ListCustomersComponent implements OnInit{

  keyword!: string;
  customersPage!: CustomerPageModel;
  page: number=0;
  size: number=5;
  tmp: number =1;
  customers!: CustomerModel[];

  constructor(private customerService: CustomerService,
              private router: Router,
              private activatedRoute: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.keyword = this.activatedRoute.snapshot.params['id'];
    this.searchCustomers(this.keyword, this.page, this.size);
  }

  searchCustomers(keyword: string, page: number, size: number){
    this.customerService.searchAll(keyword, page, size).subscribe({
      next : (data: CustomerPageModel) : void =>{
        this.customers = data.customerDTOList;
        this.customersPage = data;
      },
      error : (err) :void =>{
        this.tmp = 0;
        console.log(err);
      }
    });
  }

  nextPage(): void {
    this.page = this.customersPage.page +1;
    if(this.page < this.customersPage.totalPage){
      this.searchCustomers(this.keyword, this.page, this.size);
    }else {
      alert("It is the last page");
    }
  }

    previousPage(): void {
      this.page = this.customersPage.page -1;
      if(this.page >= 0){
        this.searchCustomers(this.keyword, this.page, this.size);
      }else{
        alert("It the first page");
      }
    }

  showCustomer(id: string): void {
    this.router.navigate(["show-customer", id]).then();
  }
}
