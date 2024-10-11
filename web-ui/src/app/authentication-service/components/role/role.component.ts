import {Component, OnInit} from '@angular/core';
import {Observable, of, tap} from "rxjs";
import {ActivatedRoute, Router} from "@angular/router";
import {RoleResponse} from "../../models/role.response";
import {RoleService} from "../../service/role/role.service";
import {ErrorHandlerService} from "../../../exception/error-handler.service";
import {catchError} from "rxjs/operators";
import {AsyncPipe, NgIf} from "@angular/common";
import {AuthService} from "../../service/auth/auth.service";

@Component({
  selector: 'app-role',
  standalone: true,
  imports: [
    NgIf,
    AsyncPipe
  ],
  templateUrl: './role.component.html',
  styleUrl: './role.component.css'
})
export class RoleComponent implements OnInit {

  roleObservable!: Observable<RoleResponse>;
  roleId!: number;
  errorMessage: string = '';
  errorCode: number = 0;
  errorFlag: boolean = false;

  constructor(private readonly router: Router,
              private readonly roleService: RoleService,
              private readonly activatedRoute: ActivatedRoute,
              private readonly errorHandlerService: ErrorHandlerService,
              private readonly authService: AuthService,) {

  }

  ngOnInit(): void {
    this.authService.security();
    this.roleId = this.activatedRoute.snapshot.params['id'];
    this.fetchRoleDetails();
    this.subscribeToErrors();
  }

  private fetchRoleDetails(): void {
    this.roleObservable = this.roleService.getRoleById(this.roleId).pipe(
      tap(response => {
        this.errorFlag = false;
        console.log(response.id);
      }),
      catchError(error => {
        this.errorHandlerService.handleError(error);
        return of(new RoleResponse()); // Return a safe value or handle the error appropriately
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


  delete(role: RoleResponse) :void {
    if(role.name == "USER" || role.name === "USER" || role.name == "ADMIN" || role.name === "ADMIN" || role.name == "SUPER_ADMIN" || role.name === "SUPER_ADMIN") {
      alert("You cannot remove the default roles: \"USER\", \"ADMIN\" and \"SUPER_ADMIN\"");
    }else if (confirm("Are you sure you want to delete this role?")) {
      this.roleService.deleteRoleById(role.id).subscribe({
        next: () => {
          this.router.navigate(["/roles"]).then();
        },
        error: () => {
          this.router.navigate(["/roles"]).then();
        },
      })
    }
  }

  gotoRoleUpdateComponent(id: number) :void {
    this.router.navigate(['/role-update/' + id]).then();
  }


}
