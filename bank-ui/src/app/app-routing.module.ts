import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LoginComponent} from "./components/login/login.component";
import {HomeComponent} from "./components/home/home.component";
import {CustomerCreateComponent} from "./components/customer-create/customer-create.component";
import {CustomerUpdateComponent} from "./components/customer-update/customer-update.component";
import {CustomerAllComponent} from "./components/customer-all/customer-all.component";
import {CustomerGetComponent} from "./components/customer-get/customer-get.component";
import {AccountGetComponent} from "./components/account-get/account-get.component";

const routes: Routes = [
  {path: '', redirectTo: 'authentication', pathMatch: 'full' },
  {path: 'authentication', component: LoginComponent},
  {path: 'home', component: HomeComponent},
  {path: 'customer-create', component: CustomerCreateComponent},
  {path: 'customer-update/:id', component: CustomerUpdateComponent},
  {path: 'customer-all', component: CustomerAllComponent},
  {path: 'customer-get/:id', component: CustomerGetComponent},
  {path: 'account-get', component: AccountGetComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
