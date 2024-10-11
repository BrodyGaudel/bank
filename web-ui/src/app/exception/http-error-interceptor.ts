import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import {ErrorHandlerService} from "./error-handler.service";

@Injectable()
export class HttpErrorInterceptor implements HttpInterceptor {

  constructor(private readonly errorHandlerService: ErrorHandlerService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      catchError((error: HttpErrorResponse) => {
        this.errorHandlerService.handleError(error);
        return throwError(error);
      })
    );
  }
}
