import {Component, OnInit} from '@angular/core';
import {OperationService} from "../../services/operation-service/operation.service";
import {FormBuilder, FormGroup} from "@angular/forms";
import {catchError, Observable, throwError} from "rxjs";
import {HistoryModel} from "../../models/history.model";
import {CreditModel} from "../../models/credit.model";
import {DebitModel} from "../../models/debit.model";
import {CustomerModel} from "../../models/customer.model";
import {CustomerService} from "../../services/customer-service/customer.service";
import {OperationModel} from "../../models/operation.model";

@Component({
  selector: 'app-accounts-operations',
  templateUrl: './accounts-operations.component.html',
  styleUrls: ['./accounts-operations.component.css']
})
export class AccountsOperationsComponent implements OnInit{

  currentPage: number =0;
  pageSize: number = 3;
  historyModelObservable!: Observable<HistoryModel>;
  accountFormGroup!: FormGroup;
  operationFromGroup!: FormGroup;
  customerModel!: CustomerModel;
  operationModel!: OperationModel;



  constructor(private fb: FormBuilder,
              private customerService: CustomerService,
              private operationService: OperationService) {
  }


  ngOnInit(): void {
    this.accountFormGroup=this.fb.group({accountId : this.fb.control('')});
    this.operationFromGroup=this.fb.group({
      operationType : this.fb.control(null),
      amount : this.fb.control(0),
      description : this.fb.control(null),
      accountDestination : this.fb.control(null)
    });
  }

  handleSearchAccount() :void{
    let accountId: string = this.accountFormGroup.value.accountId;
    this.historyModelObservable = this.operationService.getHistory(accountId, this.currentPage, this.pageSize).pipe(
      catchError( (err) => throwError(() => new Error(err.message)))
    );
    this.historyModelObservable.subscribe( (data: HistoryModel) =>{
      this.showCustomer(data.customerId);
    });
  }

  gotoPage(page: number) :void {
    this.currentPage = page;
    this.handleSearchAccount();
  }

  handleOperation() :void {
    let operationType = this.operationFromGroup.value.operationType;
    if(operationType == 'CREDIT'){
      this.creditAccount();
    }else {
      this.debitAccount();
    }
  }

  creditAccount() :void{
    let model: CreditModel = new CreditModel();
    model.accountId = this.accountFormGroup.value.accountId;
    model.amount = this.operationFromGroup.value.amount;
    model.description = this.operationFromGroup.value.description;
    this.operationService.credit(model).subscribe({
      next : (data: CreditModel) : void =>{
        alert("Account credited successfully with: "+data.amount);
        this.operationFromGroup.reset();
        this.handleSearchAccount();
      },
      error : (err) :void =>{
        alert(err);
        console.log(err);
      }
    });
  }

  debitAccount() :void{
    let model: DebitModel = new DebitModel();
    model.accountId = this.accountFormGroup.value.accountId;
    model.amount = this.operationFromGroup.value.amount;
    model.description = this.operationFromGroup.value.description;
    this.operationService.debit(model).subscribe({
      next : (data: DebitModel) :void =>{
        alert("Success Debit: "+data.amount);
        this.operationFromGroup.reset();
        this.handleSearchAccount();
      },
      error : (err) :void =>{
        alert(err);
        console.log(err);
      }
    });
  }


    showCustomer(customerId: string) :void {
      this.customerService.getById(customerId).subscribe({
        next : (data: CustomerModel) : void =>{
          this.customerModel = data;
        },
        error : (err) :void =>{
          alert(err);
          console.log(err);
        }
      });
    }

  formatAccountId(accountId: string): string {
    const segments: any[] = [];
    for (let i = 0; i < accountId.length; i += 3) {
      segments.push(accountId.slice(i, i + 3));
    }
    return segments.join(' ');
  }

  showOperation(op: OperationModel) :void {
    this.operationModel = op;
  }
}
