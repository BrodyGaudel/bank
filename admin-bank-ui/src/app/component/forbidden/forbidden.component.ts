import { Component } from '@angular/core';
import {AuthService} from "../../services/security/auth/auth.service";

@Component({
  selector: 'app-forbidden',
  templateUrl: './forbidden.component.html',
  styleUrl: './forbidden.component.css'
})
export class ForbiddenComponent {

  constructor(private authService: AuthService) {
  }

  gotoLoginComponent(): void {
    this.authService.logout();
  }
}
