import {Component, OnInit} from '@angular/core';
import {NavigationEnd, Router, RouterLink, RouterOutlet} from '@angular/router';
import {NgbNavModule} from "@ng-bootstrap/ng-bootstrap";
import {AuthService} from "./authentication-service/service/auth/auth.service";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, NgbNavModule, NgIf, RouterLink],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  title = 'web-ui';

  showNavbar: boolean = true;

  constructor(private readonly authService: AuthService, private readonly router: Router) {

  }

  ngOnInit(): void {
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.showNavbar = !event.url.includes('/authentication') && !event.url.includes('/update-password');
      }
    });
    //this.authService.security();
  }

  getLoggedUser(): string {
    const user: string | undefined = this.authService.getLoggedUser();
    return user ?? 'unknown';
  }

  isLoggedIn(): boolean{
    let yes: boolean = false;
    this.authService.getIsLoggedIn().subscribe(isLoggedIn => {
      yes = isLoggedIn;
    });
    return yes;
  }

  logout(): void {
    if(confirm("Are you sure you want to disconnect ?")){
      this.authService.logout();
    }
  }

  isAdmin() :boolean{
    return this.authService.isAdmin();
  }
}
