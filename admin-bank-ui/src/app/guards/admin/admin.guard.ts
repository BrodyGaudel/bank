import {ActivatedRouteSnapshot, CanActivateFn, createUrlTreeFromSnapshot, UrlTree} from '@angular/router';
import {inject} from "@angular/core";
import {AuthService} from "../../services/security/auth/auth.service";
import {map, Observable, of} from "rxjs";


export const adminGuard: CanActivateFn = (route: ActivatedRouteSnapshot, state) => {
  let value:Observable<boolean> = of(inject(AuthService).isAdmin());
  return value.pipe(
      map((isAdmin :boolean):boolean | UrlTree => isAdmin ? true : createUrlTreeFromSnapshot(route, ['/', 'forbidden']))
    );

};
