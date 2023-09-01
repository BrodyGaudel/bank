import {Component, OnInit} from '@angular/core';
import {catchError, Observable, throwError} from "rxjs";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AccountModel} from "../../../models/account.model";
import {ActivatedRoute, Router} from "@angular/router";
import {AccountService} from "../../../services/account-service/account.service";

@Component({
  selector: 'app-create-account',
  templateUrl: './create-account.component.html',
  styleUrls: ['./create-account.component.css']
})
export class CreateAccountComponent implements OnInit{
  accountSaved!: Observable<AccountModel>;
  addAccountFormGroup!: FormGroup;
  customerId!: string;

  constructor(private accountService: AccountService,
              private router: Router,
              private fb: FormBuilder,
              private activatedRoute: ActivatedRoute) {
  }


  ngOnInit(): void {
    this.customerId = this.activatedRoute.snapshot.params['id'];
    this.addAccountFormGroup = this.fb.group( {
      currency : this.fb.control(null, [Validators.required]),
      status : this.fb.control(null, [Validators.required]),
    });
  }

  addAccount() : void {
    let model: AccountModel = new AccountModel();
    model.currency = this.addAccountFormGroup.value.currency;
    model.customerId = this.customerId;
    model.status = this.addAccountFormGroup.value.status;
    model.balance = 0;
    let date : Date = new Date();
    model.creation = date;
    model.lastUpdate = date;


    this.accountSaved = this.accountService.createAccount(model).pipe(
      catchError(err => {
        alert(err.message);
        return throwError(() => err);
      })
    );
    this.accountSaved.subscribe(
      a => {
        this.router.navigate(['show-account', a.id]).then();
      }
    );
  }

}
