import {Component, OnInit} from '@angular/core';
import {AccountService} from "../../../services/account-service/account.service";
import {OperationService} from "../../../services/operation-service/operation.service";
import {Router} from "@angular/router";
import {FormBuilder, FormGroup} from "@angular/forms";
import {catchError, Observable, throwError} from "rxjs";
import {HistoryModel} from "../../../models/history.model";
import {DebitModel} from "../../../models/debit.model";
import {CreditModel} from "../../../models/credit.model";
import {OperationModel} from "../../../models/operation.model";

@Component({
  selector: 'app-show-account',
  templateUrl: './show-account.component.html',
  styleUrls: ['./show-account.component.css']
})
export class ShowAccountComponent implements OnInit{

  accountFormGroup!: FormGroup;
  currentPage: number =0;
  pageSize: number = 5;
  historyModelObservable!: Observable<HistoryModel>
  operationFromGroup!: FormGroup;
  errorMessage!: string;
  operation: OperationModel = new OperationModel();

  constructor(private accountService: AccountService,
              private operationService: OperationService,
              private router: Router,
              private fb: FormBuilder) {
  }
  ngOnInit(): void {
    this.accountFormGroup=this.fb.group({
      accountId : this.fb.control('')
    });
    this.operationFromGroup=this.fb.group({
      operationType : this.fb.control(null),
      amount : this.fb.control(0),
      description : this.fb.control(null),
      accountDestination : this.fb.control(null)
    });
  }

  handleSearchAccount(): void {
    let accountId: string = this.accountFormGroup.value.accountId;
    this.historyModelObservable = this.operationService.getHistory(accountId,this.currentPage, this.pageSize)
      .pipe(
        catchError(err => {
          this.errorMessage = err.message;
          return throwError(() => err);
        })
      );
  }

  handleAccountOperation(): void {
    let operationType = this.operationFromGroup.value.operationType;
    if(operationType=='DEBIT'){
      this.debitAccount();
    }else if (operationType=='CREDIT'){
      this.creditAccount();
    }
  }

  debitAccount(): void {
    let model: DebitModel = new DebitModel();
    model.id = this.accountFormGroup.value.accountId;
    model.amount = this.operationFromGroup.value.amount;
    model.description = this.operationFromGroup.value.description;
    this.operationService.debitAccount(model).subscribe(
      {
        next : (data: DebitModel) : void =>{
          alert("Success Debit: "+data.amount);
          this.operationFromGroup.reset();
          this.handleSearchAccount();
        },
        error : (err) : void =>{
          alert(err);
          console.log(err);
        }
      }
    );
  }

  creditAccount(): void {
    let model: CreditModel = new CreditModel();
    model.id = this.accountFormGroup.value.accountId;
    model.amount = this.operationFromGroup.value.amount;
    model.description = this.operationFromGroup.value.description;
    this.operationService.creditAccount(model).subscribe(
      {
        next : (data: CreditModel) : void =>{
          alert("Success Credit: "+data.amount);
          this.operationFromGroup.reset();
          this.handleSearchAccount();
        },
        error : (err) : void =>{
          alert(err);
          console.log(err);
        }
      }
    );
  }

  getOperationById(id: string): void {
    this.operationService.getOperationById(id).subscribe(
      {
        next : (data: OperationModel) : void =>{
          this.operation = data;
        },
        error : (err) : void =>{
          alert(err);
          console.log(err);
        }
      }
    );
  }

  gotoPage(page: number): void {
    this.currentPage = page;
    this.handleSearchAccount();
  }

}
