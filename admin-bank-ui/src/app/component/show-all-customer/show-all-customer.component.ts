import {Component, OnInit} from '@angular/core';
import {catchError, Observable, throwError} from "rxjs";
import {CustomerService} from "../../services/customer/customer.service";
import {Router} from "@angular/router";
import {CustomerPageResponse} from "../../dto/customer-page.response";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {PageStatus} from "../../dto/page-status";

@Component({
  selector: 'app-show-all-customer',
  templateUrl: './show-all-customer.component.html',
  styleUrl: './show-all-customer.component.css'
})
export class ShowAllCustomerComponent implements OnInit {

  customersObservable!: Observable<CustomerPageResponse>;
  page: number =0;
  size: number = 10;
  keyword!: string;


  searchCustomerFormGroup!: FormGroup;

  constructor(private fb: FormBuilder,
              private customerService: CustomerService,
              private router: Router) {
  }

  ngOnInit(): void {
    this.searchCustomerFormGroup = this.fb.group({
      keyword : this.fb.control(null, [Validators.required, Validators.minLength(1), Validators.maxLength(256)])
    });
  }

  handleSearchCustomer():void {
    this.keyword = this.searchCustomerFormGroup.value.keyword;
    this.customersObservable = this.customerService.searchCustomers(this.keyword, this.page, this.size).pipe(
      catchError( (err) => throwError(() => new Error(err.message)))
    );
  }



  gotoNextPage(status: PageStatus):void {
    if(status.hasNext && status.hasContent && !status.isLast){
      this.page = this.page + 1;
      this.handleSearchCustomer();
    }
  }

  gotoPreviousPage(status: PageStatus):void {
    if(status.hasPrevious && status.hasContent && !status.isFirst){
      this.page = this.page - 1;
      this.handleSearchCustomer();
    }
  }


  getPreviousButtonStyle(status: PageStatus): string {
    if(status.isFirst){
      return "page-item disabled"
    }
    return "page-item";
  }

  getNextButtonStyle(status: PageStatus):string {
    if(status.isLast){
      return "page-item disabled"
    }
    return "page-item";
  }

  gotoShowCustomerDetails(id: string):void {
    this.router.navigate(['/customers-details', id]).then();
  }
}
