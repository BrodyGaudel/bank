import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {CreateCustomerComponent} from "./components/create-customer/create-customer.component";
import {ListCustomerComponent} from "./components/list-customer/list-customer.component";
import {ShowCustomerComponent} from "./components/show-customer/show-customer.component";
import {UpdateCustomerComponent} from "./components/update-customer/update-customer.component";
import {AccountOperationComponent} from "./components/account-operation/account-operation.component";

const routes: Routes = [
  {path: '', redirectTo: 'operations', pathMatch: 'full' },
  {path: 'create-customer', component: CreateCustomerComponent},
  {path: 'list-customer/:id', component: ListCustomerComponent},
  {path: 'show-customer/:id', component: ShowCustomerComponent},
  {path: 'update-customer/:id', component: UpdateCustomerComponent},
  {path: 'operations', component: AccountOperationComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
