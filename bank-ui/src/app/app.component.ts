import {Component, OnInit} from '@angular/core';
import {NavigationEnd, Router} from "@angular/router";
import {AuthService} from "./services/security/auth/auth.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  title = 'bank-ui';

  showNavbar: boolean = true;

  constructor(private authService: AuthService, private router: Router) {
  }

  ngOnInit(): void {
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.showNavbar = !event.url.includes('/authentication') && !event.url.includes('/update-password');
      }
    });
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
}
