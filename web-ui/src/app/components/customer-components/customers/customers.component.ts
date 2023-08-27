import {Component, OnInit} from '@angular/core';
import {CustomerModel} from "../../../models/customer.model";
import {catchError, Observable, throwError} from "rxjs";
import {CustomerService} from "../../../services/customer-service/customer.service";
import {Router} from "@angular/router";
import {FormBuilder, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-customers',
  templateUrl: './customers.component.html',
  styleUrls: ['./customers.component.css']
})
export class CustomersComponent implements OnInit{

  customers!: Observable<Array<CustomerModel>>;
  searchFormGroup!: FormGroup;
  errorMessage!: string;

  constructor(private customerService: CustomerService,
              private router: Router,
              private fb: FormBuilder) {
  }

  ngOnInit(): void {
    this.searchFormGroup=this.fb.group({
      keyword : this.fb.control("")
    });
    this.getAllCustomers();
  }

  getAllCustomers() : void {
    this.customers = this.customerService.getAllCustomers(25, 0);
  }


  handleSearch() : void {
    let keyword = this.searchFormGroup.value.keyword;
    this.customers = this.customerService.searchCustomer(keyword, 25, 0).pipe(
      catchError(err => {
        this.errorMessage = err.message;
        return throwError(() => err);
      })
    );
  }


  handleDetails(id: string) : void {
    this.router.navigate(['/show-customer', id]).then(
      () => {
        // La promesse a été résolue avec succès.
      },
      error => {
        // La promesse a été rejetée avec une erreur.
        alert(error)
      }
    );
  }
}
