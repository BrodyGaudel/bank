import {Component, OnInit} from '@angular/core';
import {Observable, of, tap} from "rxjs";
import {UserResponse} from "../../models/user.response";
import {ActivatedRoute, Router} from "@angular/router";
import {UserService} from "../../service/user/user.service";
import {ErrorHandlerService} from "../../../exception/error-handler.service";
import {catchError} from "rxjs/operators";
import {AsyncPipe, DatePipe, NgForOf, NgIf} from "@angular/common";
import {UserRoleRequest} from "../../models/user-role.request";
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {AuthService} from "../../service/auth/auth.service";

@Component({
  selector: 'app-user',
  standalone: true,
  imports: [
    NgIf,
    AsyncPipe,
    DatePipe,
    NgForOf,
    FormsModule,
    ReactiveFormsModule
  ],
  templateUrl: './user.component.html',
  styleUrl: './user.component.css'
})
export class UserComponent implements OnInit {

  userObservable!: Observable<UserResponse>;
  userId!: string;
  errorMessage: string = '';
  errorCode: number = 0;
  errorFlag: boolean = false;
  addRoleToUserFormGroup!: FormGroup;


  constructor(private readonly router: Router,
              private readonly userService: UserService,
              private readonly activatedRoute: ActivatedRoute,
              private readonly errorHandlerService: ErrorHandlerService,
              private readonly fb: FormBuilder,
              private readonly authService: AuthService) {

  }

  ngOnInit(): void {
    this.authService.security();
    this.userId = this.activatedRoute.snapshot.params['id'];
    this.initAddRoleToUserFormGroup();
    this.fetchUserDetails();
    this.subscribeToErrors();
  }

  private fetchUserDetails(): void {
    this.userObservable = this.userService.getById(this.userId).pipe(
      tap(response => {
        this.errorFlag = false;
        console.log(response.id);
      }),
      catchError(error => {
        this.errorHandlerService.handleError(error);
        return of(new UserResponse()); // Return a safe value or handle the error appropriately
      })
    )
  }

  private subscribeToErrors(): void {
    this.errorHandlerService.error$.subscribe(error => {
      if (error) {
        this.errorMessage = error.message;
        this.errorCode = error.code;
        this.errorFlag = true;
      } else {
        this.errorFlag = false;
      }
    });
  }

  enabledOrDisabled(id: string) :void {
    this.userObservable = this.userService.updateStatus(id).pipe(
      tap(response => {
        this.errorFlag = false;
        console.log(response.id);
      }),
      catchError(error => {
        this.errorHandlerService.handleError(error);
        return of(new UserResponse());
      })
    )
  }

  textEnabled(enabled: boolean) :"YES"|"NO" {
    if(enabled){
      return "YES"
    }
    return "NO";
  }

  styleEnabled(enabled: boolean) :"text-success"|"text-danger" {
    if(enabled){
      return "text-success";
    }
    return "text-danger";
  }

  delete(id: string) :void {
    if (confirm("Are you sure you want to delete this user?")) {
      this.userService.delete(id).subscribe({
        next: () => { this.router.navigate(["/users"]).then(); },
        error: () => { this.router.navigate(["/users"]).then(); },
      })
    }
  }


  gotoUserUpdateComponent(id: string) :void {
    this.router.navigate(['/user-update/' + id]).then();
  }

  removeRoleFromUser(roleName: string, username: string) :void {
    if(confirm("Are you sure you want to remove this role to this user?")) {
      let request: UserRoleRequest = new UserRoleRequest();
      request.username = username;
      request.roleName = roleName;
      this.userObservable = this.userService.removeRoleFromUser(request).pipe(
        tap(response => {
          this.errorFlag = false;
          console.log(response.id);
        }),
        catchError(error => {
          this.errorHandlerService.handleError(error);
          return of(new UserResponse());
        })
      )

    }
  }

  initAddRoleToUserFormGroup(): void {
    this.addRoleToUserFormGroup = this.fb.group({
      roleName: this.fb.control(null, [Validators.required, Validators.minLength(1), Validators.maxLength(256)])
    });
  }

  handleAddRoleToUserSubmit(username: string) :void {
    let request: UserRoleRequest = new UserRoleRequest();
    request.roleName = this.addRoleToUserFormGroup.value.roleName;
    request.username = username;
    this.userObservable = this.userService.addRoleToUser(request).pipe(
      tap(response => {
        this.errorFlag = false;
        console.log(response.id);
      }),
      catchError(error => {
        this.errorHandlerService.handleError(error);
        return of(new UserResponse());
      })
    )
  }
}
