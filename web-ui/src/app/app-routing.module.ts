import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {AccountsOperationsComponent} from "./components/accounts-operations/accounts-operations.component";
import {CreateCustomerComponent} from "./components/create-customer/create-customer.component";
import {ShowCustomerComponent} from "./components/show-customer/show-customer.component";
import {ListCustomersComponent} from "./components/list-customers/list-customers.component";
import {LoginComponent} from "./components/login/login.component";

const routes: Routes = [
  {path: '', redirectTo: 'accounts-operations', pathMatch: 'full' },
  {path: 'accounts-operations', component: AccountsOperationsComponent},
  {path: 'create-customer', component: CreateCustomerComponent},
  {path: 'show-customer/:id', component: ShowCustomerComponent},
  {path: 'list-customers/:id', component: ListCustomersComponent},
  {path: 'login', component: LoginComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
