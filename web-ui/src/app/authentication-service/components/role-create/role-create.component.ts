import {Component, OnInit} from '@angular/core';
import {RoleResponse} from "../../models/role.response";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {ErrorHandlerService} from "../../../exception/error-handler.service";
import {Router} from "@angular/router";
import {RoleService} from "../../service/role/role.service";
import {NgbAlert, NgbAlertConfig} from "@ng-bootstrap/ng-bootstrap";
import {RoleRequest} from "../../models/role.request";
import {NgForOf, NgIf} from "@angular/common";

@Component({
  selector: 'app-role-create',
  standalone: true,
  imports: [
    NgForOf,
    NgIf,
    NgbAlert,
    ReactiveFormsModule
  ],
  templateUrl: './role-create.component.html',
  styleUrl: './role-create.component.css'
})
export class RoleCreateComponent implements OnInit {

  errorFlag: boolean = false;
  errorMessage!: string;
  errorCode!: number;
  errorValidation!: [];
  createRoleFormGroup!: FormGroup;

  constructor(private readonly errorHandlerService: ErrorHandlerService,
              private readonly router: Router,
              private readonly roleService: RoleService,
              private readonly fb: FormBuilder,
              alertConfig: NgbAlertConfig) {

    alertConfig.type = 'warning';
    alertConfig.dismissible = false;
  }

  ngOnInit(): void {
    this.initCreateRoleFormGroup();
  }

  handleSubmit(): void {
    let request: RoleRequest = this.createRoleFormGroup.value;
    this.roleService.createRole(request).subscribe({
      next: result => {
        this.handleCreationSuccess(result);
      },
      error: err => {
        this.handleCreationFailure(err);
      }
    });
  }

  initCreateRoleFormGroup(): void {
    this.createRoleFormGroup = this.fb.group({
      name: this.fb.control(null, [
        Validators.required,
        Validators.minLength(1),
        Validators.maxLength(256)
      ]),

      description: this.fb.control(null, [
        Validators.required,
        Validators.minLength(1),
        Validators.maxLength(256)
      ]),
    });

  }

  private handleCreationSuccess(result: RoleResponse): void {
    this.router.navigate(['/role', result.id]).then();
  }

  private handleCreationFailure(err: any) {
    console.log(err);
    this.errorHandlerService.handleError(err);
    this.subscribeToErrors();
    this.errorFlag = true;
  }

  private subscribeToErrors(): void {
    this.errorHandlerService.error$.subscribe(error => {
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
