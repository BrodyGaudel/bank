import {ActivatedRouteSnapshot, CanActivateFn, createUrlTreeFromSnapshot, UrlTree} from '@angular/router';
import {AuthService} from "../../services/security/auth/auth.service";
import {inject} from "@angular/core";
import {map} from "rxjs";

export const authGuard: CanActivateFn = (route: ActivatedRouteSnapshot, state) => {
  return inject(AuthService).getIsLoggedIn()
    .pipe(
      map((isLoggedIn :boolean):boolean | UrlTree => isLoggedIn ? true : createUrlTreeFromSnapshot(route, ['/', 'login']))
    );
};
