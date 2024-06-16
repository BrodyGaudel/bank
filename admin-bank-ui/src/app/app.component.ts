import {Component, OnInit} from '@angular/core';
import {AuthService} from "./services/security/auth/auth.service";
import {NavigationEnd, Router} from "@angular/router";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent  implements OnInit {
  title = 'admin-bank-ui';
  showNavbar:boolean = true;

  constructor(private authService: AuthService, private router: Router) {
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.showNavbar = !event.url.includes('/login');
        this.showNavbar = !event.url.includes('/forbidden');
      }
    });
  }

  ngOnInit(): void {

  }

  logout():void {
    this.authService.logout();
  }
}
