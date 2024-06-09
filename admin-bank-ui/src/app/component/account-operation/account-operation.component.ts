import {Component, OnInit} from '@angular/core';
import {AccountService} from "../../services/account/account.service";
import {Router} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {catchError, Observable, throwError} from "rxjs";
import {AccountResponse} from "../../dto/account/account.response";
import {OperationResponse} from "../../dto/account/operation.response";
import {CreditAccountRequest} from "../../dto/account/credit-account.request";
import {DebitAccountRequest} from "../../dto/account/debit-account.request";
import {TransferRequest} from "../../dto/account/transfer.request";

@Component({
  selector: 'app-account-operation',
  templateUrl: './account-operation.component.html',
  styleUrl: './account-operation.component.css'
})
export class AccountOperationComponent implements OnInit{

  accountObservable!: Observable<AccountResponse>;
  searchAccountFormGroup!: FormGroup;
  errorMessage!: string;
  errorDescription!: string;
  errorCode!: number;
  errorFlag: boolean = false;
  operationsObservable!: Observable<Array<OperationResponse>>;
  page: number =0;
  size: number = 5;
  accountId!: string;
  operationFromGroup!: FormGroup;
  isTransfer: boolean = false;

  constructor(private accountService: AccountService,
              private router: Router,
              private fb: FormBuilder) {

  }


  ngOnInit(): void {
    this.searchAccountFormGroup = this.fb.group({
      accountId : this.fb.control(null, [Validators.required, Validators.minLength(12), Validators.maxLength(12)]),
    });
    this.operationFromGroup=this.fb.group({
      operationType : this.fb.control(null),
      amount : this.fb.control(0, [Validators.required]),
      description : this.fb.control(null, [Validators.required, Validators.minLength(2), Validators.maxLength(256)]),
      destination : this.fb.control(null)
    });
  }

  handleSearchAccount() :void {
    this.errorFlag = false;
    this.accountId = this.searchAccountFormGroup.value.accountId;
    if(this.isNumeric(this.accountId)){
      this.accountObservable = this.accountService.findById(this.accountId).pipe(
        catchError(err => {
          this.handleError(err);
          return throwError(() => new Error(err.message));
        })
      );
      if(!this.errorFlag){
        this.operationsObservable = this.getAllOperationByAccountId(this.accountId, this.page, this.size);
      }
    }else{
      alert("Un numéro de compte ne peu contenir que des chiffres. Exemple: 123456789");
    }
  }

  private handleError(error: any) :void {
    this.errorFlag = true;
    if (error.error instanceof ErrorEvent) {
      this.errorMessage = error.error.message;
    } else {
      this.errorMessage = error.error.message;
      this.errorCode = error.error.code;
      this.errorDescription = error.error.description;
    }
  }

  private isNumeric(str: string): boolean {
    const regex: RegExp = /^\d+$/;
    return regex.test(str);
  }

  private getAllOperationByAccountId(id: string, pageNumber: number, pageSize: number): Observable<Array<OperationResponse>> {
    return this.accountService.findAllOperationsByAccountId(id, pageNumber, pageSize).pipe(
      catchError( (err) => throwError(() => new Error(err.message)))
    );
  }


  gotoPreviousPage() :void {
    if(this.page>=1){
      this.page = this.page - 1;
      this.operationsObservable = this.getAllOperationByAccountId(this.accountId, this.page, this.size);
    }
  }


  gotoNextPage(length: number):void {
    if(length<this.size){
      alert("C'est la dernière pages");
    }else{
      this.page = this.page + 1;
      this.operationsObservable = this.getAllOperationByAccountId(this.accountId, this.page, this.size);
    }
  }

  handleSaveOperation():void {
    let operationType = this.operationFromGroup.value.operationType;
    if(operationType == 'CREDIT'){
      this.credit();
    }else if(operationType == 'DEBIT'){
      this.debit();
    }else if(operationType == 'TRANSFER'){
      this.transfer();
    }
  }

  unSetTransfer(): void {
    this.isTransfer = false;
  }

  setTransfer(): void {
    this.isTransfer = true;
  }

  private credit(): void{
    let model: CreditAccountRequest = new CreditAccountRequest();
    model.accountId = this.accountId;
    model.amount = this.operationFromGroup.value.amount;
    model.description = this.operationFromGroup.value.description;
    this.accountService.credit(model).subscribe({
      next : (data: string) : void =>{
        alert("Le compte a été crédité avec succès");
        console.log(data);
        this.operationFromGroup.reset();
        this.handleSearchAccount();
      },
      error : (err) :void =>{
        alert(err);
        console.log(err);
      }
    });
  }

  private debit(): void{
    let model: DebitAccountRequest = new DebitAccountRequest();
    model.amount = this.operationFromGroup.value.amount;
    model.description = this.operationFromGroup.value.description;
    model.accountId = this.accountId;
    this.accountService.debit(model).subscribe({
      next : (data: string) : void =>{
        alert("Le compte a été débité avec succès.");
        console.log(data);
        this.operationFromGroup.reset();
        this.handleSearchAccount();
      },
      error : (err) :void =>{
        alert(err);
        console.log(err);
      }
    });
  }

  private transfer(): void{
    let model: TransferRequest = new TransferRequest();
    model.accountIdFrom = this.accountId;
    model.amount = this.operationFromGroup.value.amount;
    model.description = this.operationFromGroup.value.description;
    model.accountIdTo = this.operationFromGroup.value.destination;
    this.accountService.transfer(model).subscribe({
      next : (data: any) : void =>{
        alert("Le transfert a été effectué avec succès! ");
        console.log(data);
        this.operationFromGroup.reset();
        this.handleSearchAccount();
      },
      error : (err) :void =>{
        alert(err);
        console.log(err);
      }
    });
  }

  handleKeyDown(event: KeyboardEvent, operationType: string) {
    if (event.key === 'Enter' || event.key === ' ') {
      if (operationType === 'DEBIT' || operationType === 'CREDIT') {
        this.unSetTransfer();
      } else if (operationType === 'TRANSFER') {
        this.setTransfer();
      }
      event.preventDefault();
    }
  }
}
