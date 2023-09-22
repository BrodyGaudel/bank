import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {CreateCustomerComponent} from "./components/create-customer/create-customer.component";
import {ListCustomerComponent} from "./components/list-customer/list-customer.component";
import {ShowCustomerComponent} from "./components/show-customer/show-customer.component";
import {UpdateCustomerComponent} from "./components/update-customer/update-customer.component";
import {AccountOperationComponent} from "./components/account-operation/account-operation.component";
import {LoginComponent} from "./components/login/login.component";
import {AdminGuard} from "./components/guard/admin.guard";
import {ForbiddenComponent} from "./components/forbidden/forbidden.component";
import {SecurityGuard} from "./components/guard/security.guard";

const routes: Routes = [
  {path: '', redirectTo: 'operations', pathMatch: 'full' },
  {path: 'create-customer', component: CreateCustomerComponent, canActivate: [AdminGuard]},
  {path: 'list-customer/:id', component: ListCustomerComponent, canActivate: [SecurityGuard]},
  {path: 'show-customer/:id', component: ShowCustomerComponent, canActivate: [SecurityGuard]},
  {path: 'update-customer/:id', component: UpdateCustomerComponent, canActivate: [AdminGuard]},
  {path: 'operations', component: AccountOperationComponent, canActivate: [SecurityGuard]},
  {path: 'login', component: LoginComponent},
  {path: 'forbidden', component: ForbiddenComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
