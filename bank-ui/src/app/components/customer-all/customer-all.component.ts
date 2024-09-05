import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Observable} from "rxjs";
import {CustomerResponse} from "../../models/response/customer.response";
import {Router} from "@angular/router";
import {CustomerService} from "../../services/customers/customer.service";

@Component({
  selector: 'app-customer-all',
  templateUrl: './customer-all.component.html',
  styleUrl: './customer-all.component.css'
})
export class CustomerAllComponent implements OnInit {

  searchFormGroup!: FormGroup;
  customersObservable!: Observable<Array<CustomerResponse>>;
  page: number = 0;
  size: number = 9;
  keyword: string = "";

  constructor(private userService: CustomerService,
              private fb: FormBuilder, private router: Router) {
  }

  ngOnInit(): void {
    this.searchFormGroup = this.fb.group({
      keyword: this.fb.control('', [Validators.minLength(1), Validators.maxLength(256)]),
    });

    this.customersObservable = this.searchUser(this.keyword, this.page, this.size);

    //Listen to changes in the form to launch the search automatically (optional)
    this.searchFormGroup.get('keyword')?.valueChanges.subscribe(value => {
      this.keyword = value;
      this.page = 0; //Reset to first page when searching
      this.customersObservable = this.searchUser(this.keyword, this.page, this.size);
    });
  }

  searchUser(keyword: string, p: number, s: number): Observable<Array<CustomerResponse>> {
    return this.userService.searchCustomer(keyword, p, s).pipe();
  }

  //Method called when the search form is submitted
  onSearch(): void {
    this.keyword = this.searchFormGroup.value.keyword;
    this.page = 0; //Reset to first page when searching
    this.customersObservable = this.searchUser(this.keyword, this.page, this.size);
  }

  //Method for managing page changes (to be called from the pagination in the template)
  onPageChange(page: number): void {
    this.page = page;
    this.customersObservable = this.searchUser(this.keyword, this.page, this.size);
  }


  gotoNextPage(page: number, length: number): void {
    if(length>=this.size){
      this.onPageChange(page);
    }else {
      console.log("No more data available.");
    }
  }

  gotoPreviousPage(page: number): void {
    if(page>=0){
      this.onPageChange(page);
    }else {
      console.log("No more data available.");
    }
  }

  gotoShowCustomerComponent(id: string ): void {
    this.router.navigate(["customer-get",id]).then();
  }

}
