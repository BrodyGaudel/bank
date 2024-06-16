import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {CreateCustomerComponent} from "./component/create-customer/create-customer.component";
import {ShowAllCustomerComponent} from "./component/show-all-customer/show-all-customer.component";
import {CustomerDetailsComponent} from "./component/customer-details/customer-details.component";
import {UpdateCustomerComponent} from "./component/update-customer/update-customer.component";
import {OperationComponent} from "./component/operation/operation.component";
import {AccountOperationComponent} from "./component/account-operation/account-operation.component";
import {LoginComponent} from "./component/login/login.component";
import {ForbiddenComponent} from "./component/forbidden/forbidden.component";
import {authGuard} from "./guards/auth/auth.guard";
import {adminGuard} from "./guards/admin/admin.guard";
import {UsersComponent} from "./component/users/users.component";

const routes: Routes = [
  {path: '', redirectTo: 'all-operations', pathMatch: 'full' },
  {path: 'create-customer', component: CreateCustomerComponent, canActivate: [authGuard]},
  {path: 'show-all-customer', component: ShowAllCustomerComponent, canActivate: [authGuard]},
  {path: 'customers-details/:id', component: CustomerDetailsComponent, canActivate: [authGuard]},
  {path: 'update-customer/:id', component: UpdateCustomerComponent, canActivate: [authGuard]},
  {path: 'operation/:id', component: OperationComponent, canActivate: [authGuard]},
  {path: 'all-operations', component: AccountOperationComponent, canActivate: [authGuard]},
  {path: 'users', component: UsersComponent, canActivate: [adminGuard]},
  {path: 'login', component: LoginComponent},
  {path: 'forbidden', component: ForbiddenComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
