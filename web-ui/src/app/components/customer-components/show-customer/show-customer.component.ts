import {Component, OnInit} from '@angular/core';
import {CustomerService} from "../../../services/customer-service/customer.service";
import {ActivatedRoute, Router} from "@angular/router";
import {catchError, Observable, Subscription, throwError} from "rxjs";
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

  gotoCustomersComponent() : void {
    this.router.navigate(['/customers']).then(
      () => {
        // La promesse a été résolue avec succès.
      },
      error => {
        // La promesse a été rejetée avec une erreur.
        alert(error)
      }
    );
  }

  gotoUpdateCustomerComponent(id: string) : void {
    this.router.navigate(['/update-customer', id]).then(
      () => {
        // La promesse a été résolue avec succès.
      },
      error => {
        // La promesse a été rejetée avec une erreur.
        alert(error)
      }
    );
  }



  deleteCustomerById(id: string) : void {
    const conf : boolean = confirm('Etes-vous sûr de vouloir supprimer?');
    if(conf){
      const response : Subscription = this.customerService.deleteCustomerById(id).subscribe(
        () => {
          console.log(response);
          alert("CLIENT SUPPRIMER !");
          this.gotoCustomersComponent();
        }
      );
    }
  }

  gotoCreateAccountComponent(id: string) : void {
    this.router.navigate(['/create-account', id]).then(
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
