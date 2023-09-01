import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {CustomersComponent} from "./components/customer-components/customers/customers.component";
import {ShowCustomerComponent} from "./components/customer-components/show-customer/show-customer.component";
import {CreateCustomerComponent} from "./components/customer-components/create-customer/create-customer.component";
import {UpdateCustomerComponent} from "./components/customer-components/update-customer/update-customer.component";
import {CreateAccountComponent} from "./components/account-components/create-account/create-account.component";

const routes: Routes = [
  {path: '', redirectTo: 'customers', pathMatch: 'full' },
  {path: 'customers', component: CustomersComponent},
  {path: 'show-customer/:id', component: ShowCustomerComponent},
  {path: 'create-customer', component: CreateCustomerComponent},
  {path: 'update-customer/:id', component: UpdateCustomerComponent},
  {path: 'create-account/:id', component: CreateAccountComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
