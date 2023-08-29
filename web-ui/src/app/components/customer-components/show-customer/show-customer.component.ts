import {Component, OnInit} from '@angular/core';
import {CustomerService} from "../../../services/customer-service/customer.service";
import {ActivatedRoute, Router} from "@angular/router";
import {catchError, Observable, throwError} from "rxjs";
import {CustomerModel} from "../../../models/customer.model";

@Component({
  selector: 'app-show-customer',
  templateUrl: './show-customer.component.html',
  styleUrls: ['./show-customer.component.css']
})
export class ShowCustomerComponent implements OnInit {

  customer!: Observable<CustomerModel>;
  id!: string;
  errorMessage!: string;



  constructor(private customerService: CustomerService,
              private router: Router,
              private activatedRoute: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.id = this.activatedRoute.snapshot.params['id'];
    this.getCustomerById(this.id);
  }

  getCustomerById(id: string): void {
    this.customer = this.customerService.getCustomerById(id).pipe(
        catchError(err => {
          this.errorMessage = err.message;
          return throwError(() => err);
        })
    );
  }

}
