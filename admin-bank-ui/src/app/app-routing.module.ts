import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {CreateCustomerComponent} from "./component/create-customer/create-customer.component";
import {ShowAllCustomerComponent} from "./component/show-all-customer/show-all-customer.component";
import {CustomerDetailsComponent} from "./component/customer-details/customer-details.component";
import {UpdateCustomerComponent} from "./component/update-customer/update-customer.component";
import {OperationComponent} from "./component/operation/operation.component";
import {AccountOperationComponent} from "./component/account-operation/account-operation.component";

const routes: Routes = [
  {path: '', redirectTo: 'create-customer', pathMatch: 'full' },
  {path: 'create-customer', component: CreateCustomerComponent},
  {path: 'show-all-customer', component: ShowAllCustomerComponent},
  {path: 'customers-details/:id', component: CustomerDetailsComponent},
  {path: 'update-customer/:id', component: UpdateCustomerComponent},
  {path: 'operation/:id', component: OperationComponent},
  {path: 'all-operations/', component: AccountOperationComponent},

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
