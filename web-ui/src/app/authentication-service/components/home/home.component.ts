import {Component, OnInit} from '@angular/core';
import {NgbPopover} from "@ng-bootstrap/ng-bootstrap";
import {Router} from "@angular/router";
import {AuthService} from "../../service/auth/auth.service";

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    NgbPopover
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {

  userManagementMessage: string = "This service allows you to manage bank users, their roles and their authorizations to administer all banking services.";
  customerManagementMessage : string = "This service manages the bank's customers.";
  accountManagementMessage : string = "This service manages customers' bank accounts and transactions.";
  roleManagementMessage: string = "This service allows you to manage roles for security";

  constructor(private readonly router: Router, private readonly authService: AuthService) {
  }

  gotoUsersComponent() :void {
    this.router.navigate(['/users']).then();
  }

  gotoRolesComponent() :void {
    this.router.navigate(['/roles']).then();
  }

  ngOnInit(): void {
    this.authService.security();
  }
}
