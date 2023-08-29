import { Component } from '@angular/core';
import {Router} from "@angular/router";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {
  active : number = 1;

  constructor(private router: Router) {
  }

  gotoCustomersComponent() {
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

  gotoCreateCustomerComponent() {
    this.router.navigate(['/create-customer']).then(
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
