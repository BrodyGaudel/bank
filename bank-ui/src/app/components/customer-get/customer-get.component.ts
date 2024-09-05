import {Component, OnInit} from '@angular/core';
import {CustomerResponse} from "../../models/response/customer.response";
import {ActivatedRoute, Router} from "@angular/router";
import {CustomerService} from "../../services/customers/customer.service";
import {ErrorHandlerService} from "../../services/exception/error-handler.service";

@Component({
  selector: 'app-customer-get',
  templateUrl: './customer-get.component.html',
  styleUrl: './customer-get.component.css'
})
export class CustomerGetComponent implements OnInit{

  customerId!: string;
  customer!: CustomerResponse;
  errorFlag: boolean = false;
  errorMessage!: string;
  errorCode!: number;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private customerService: CustomerService,
              private errorHandlerService: ErrorHandlerService) {}

  ngOnInit(): void {
    this.customerId = this.route.snapshot.paramMap.get('id')!;
    this.loadCustomerData();
  }

  loadCustomerData(): void {
    this.customerService.getCustomerById(this.customerId).subscribe({
      next: (response: CustomerResponse) => {
        this.customer = response;
      },
      error: (err) => {
        console.error(err);
        this.errorHandlerService.handleError(err);
        this.subscribeToErrors();
      }
    });
  }

  private subscribeToErrors(): void {
    this.errorHandlerService.error$.subscribe((error) => {
      if (error) {
        this.errorMessage = error.message;
        this.errorCode = error.code;
        this.errorFlag = true;
      } else {
        this.errorFlag = false;
      }
    });
  }

  protected alreadyLoaded(): boolean {
    return this.customer != null;
  }

  gotoCustomerUpdateComponent(id: string):void {
    this.router.navigate(['/customer-update', id]).then();
  }
}
