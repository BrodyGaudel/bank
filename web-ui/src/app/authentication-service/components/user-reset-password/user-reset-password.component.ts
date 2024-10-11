import {Component, OnInit} from '@angular/core';
import {FormGroup} from "@angular/forms";

@Component({
  selector: 'app-user-reset-password',
  standalone: true,
  imports: [],
  templateUrl: './user-reset-password.component.html',
  styleUrl: './user-reset-password.component.css'
})
export class UserResetPasswordComponent implements OnInit{

  errorFlag: boolean = false;
  errorMessage!: string;
  errorCode!: number;
  errorValidation!: [];
  updatePasswordFormGroup!: FormGroup;

  ngOnInit(): void {
    // TODO document why this method 'ngOnInit' is empty

  }

}
