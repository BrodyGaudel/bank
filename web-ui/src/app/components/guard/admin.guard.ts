import {AuthService} from "../../services/auth-service/auth.service";
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from "@angular/router";
import {Injectable} from "@angular/core";

@Injectable({
  providedIn: 'root'
})
export class AdminGuard implements CanActivate {

  constructor (private authService: AuthService,
               private router : Router) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean{
    if (this.authService.isAdmin()){
      return true;
    }else {
      this.router.navigate(['forbidden']).then();
      return false;
    }

  }
}
