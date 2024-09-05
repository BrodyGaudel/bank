import { Injectable } from '@angular/core';
import {BehaviorSubject} from "rxjs";
import {HttpErrorResponse} from "@angular/common/http";
import {ExceptionResponse} from "../../models/response/exception.response";

@Injectable({
  providedIn: 'root'
})
export class ErrorHandlerService {

  private errorSubject = new BehaviorSubject<any>(null);
  public error$ = this.errorSubject.asObservable();

  constructor() { }

  handleError(error: HttpErrorResponse): void {
    let exceptionResponse: ExceptionResponse = {
      code: error.status,
      message: error.message,
      description: '',
      validationErrors: [],
    };

    if (error.error) {
      exceptionResponse = {
        ...exceptionResponse,
        code: error.error.code || error.status,
        message: error.error.message || error.message,
        description: error.error.description || '',
        validationErrors: error.error.validationErrors || [],
      };
      this.errorSubject.next(exceptionResponse);
      console.error(`Error: ${exceptionResponse.message}, Code: ${exceptionResponse.code}, Description: ${exceptionResponse.description}`);
    }

  }

  clearError(): void {
    this.errorSubject.next(null);
  }
}
