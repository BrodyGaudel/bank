import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {AccountsOperationsComponent} from "./components/accounts-operations/accounts-operations.component";
import {CreateCustomerComponent} from "./components/create-customer/create-customer.component";

const routes: Routes = [
  {path: '', redirectTo: 'accounts-operations', pathMatch: 'full' },
  {path: 'accounts-operations', component: AccountsOperationsComponent},
  {path: 'create-customer', component: CreateCustomerComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
