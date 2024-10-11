import {Component, OnInit} from '@angular/core';
import {RoleResponse} from "../../models/role.response";
import {RoleRequest} from "../../models/role.request";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {RoleService} from "../../service/role/role.service";
import {ErrorHandlerService} from "../../../exception/error-handler.service";
import {NgForOf, NgIf} from "@angular/common";
import {AuthService} from "../../service/auth/auth.service";

@Component({
  selector: 'app-role-update',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    NgIf,
    NgForOf
  ],
  templateUrl: './role-update.component.html',
  styleUrl: './role-update.component.css'
})
export class RoleUpdateComponent implements OnInit {

  updateRoleFormGroup!: FormGroup;
  roleId!: number;
  errorFlag: boolean = false;
  errorMessage!: string;
  errorCode!: number;
  errorValidation!: [];

  constructor(private readonly fb: FormBuilder,
              private readonly activatedRoute: ActivatedRoute,
              private readonly router: Router,
              private readonly roleService: RoleService,
              private readonly errorHandlerService: ErrorHandlerService,
              private readonly authService: AuthService) {}

  ngOnInit(): void {
    this.authService.security();
    this.roleId = this.activatedRoute.snapshot.params['id'];
    this.initUpdateRoleFormGroup();
    this.loadRoleData();
  }

  initUpdateRoleFormGroup(): void {
    this.updateRoleFormGroup = this.fb.group({
      name: this.fb.control(null, [Validators.required, Validators.minLength(1), Validators.maxLength(256)]),
      description: this.fb.control(null, [Validators.required, Validators.minLength(1), Validators.maxLength(256)]),
    });
  }

  loadRoleData(): void {
    this.roleService.getRoleById(this.roleId).subscribe({
      next: (role: RoleResponse) => {
        this.updateRoleFormGroup.patchValue(role);
      },
      error: (err) => {
        console.error(err);
        this.errorHandlerService.handleError(err);
      }
    });
  }

  handleSubmit(): void {
    if (this.updateRoleFormGroup.valid) {
      let request: RoleRequest = this.updateRoleFormGroup.value;
      this.roleService.updateRole(this.roleId, request).subscribe({
        next: (result: RoleResponse): void => {
          this.handleUpdateSuccess(result);
        },
        error: (err): void => {
          this.handleUpdateFailure(err);
        }
      });
    }
  }

  private handleUpdateSuccess(result: RoleResponse): void {
    this.router.navigate(['/role', result.id]).then();
  }

  private handleUpdateFailure(err: any): void {
    console.log(err);
    this.errorHandlerService.handleError(err);
    this.subscribeToErrors();
    this.errorFlag = true;
  }

  private subscribeToErrors(): void {
    this.errorHandlerService.error$.subscribe((error) => {
      if (error) {
        this.errorMessage = error.message;
        this.errorCode = error.code;
        this.errorValidation = error.validationErrors;
        this.errorFlag = true;
      } else {
        this.errorFlag = false;
      }
    });
  }


}
