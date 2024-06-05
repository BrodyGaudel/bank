import {Component, OnInit} from '@angular/core';
import {catchError, Observable, throwError} from "rxjs";
import {CustomerResponse} from "../../dto/customer.response";
import {CustomerService} from "../../services/customer/customer.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-customer-details',
  templateUrl: './customer-details.component.html',
  styleUrl: './customer-details.component.css'
})
export class CustomerDetailsComponent implements OnInit {

  customersObservable!: Observable<CustomerResponse>;
  customerId!: string;
  errorMessage!: string;
  errorDescription!: string;
  errorCode!: number;

  constructor(private customerService: CustomerService,
              private router: Router,
              private activatedRoute: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.customerId = this.activatedRoute.snapshot.params['id'];
    this.customersObservable = this.getCustomerById(this.customerId);
  }

  private getCustomerById(id: string):Observable<CustomerResponse> {
    return this.customerService.getCustomerById(id).pipe(
      catchError(err => {
        this.handleError(err);
        return throwError(() => new Error(err.message));
      })
    );
  }

  private handleError(error: any) :void {
    if (error.error instanceof ErrorEvent) {
      this.errorMessage = error.error.message;
    } else {
      this.errorMessage = error.error.message;
      this.errorCode = error.error.code;
      this.errorDescription = error.error.description;
    }
  }

  gotoUpdateCustomer(): void {
    this.router.navigate(['/update-customer', this.customerId]).then();
  }
}
