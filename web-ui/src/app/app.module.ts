import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";
import {NgOptimizedImage} from "@angular/common";
import { CreateCustomerComponent } from './components/create-customer/create-customer.component';
import { NavBarComponent } from './components/nav-bar/nav-bar.component';
import { ListCustomerComponent } from './components/list-customer/list-customer.component';
import { ShowCustomerComponent } from './components/show-customer/show-customer.component';
import { UpdateCustomerComponent } from './components/update-customer/update-customer.component';
import { AccountOperationComponent } from './components/account-operation/account-operation.component';

@NgModule({
  declarations: [
    AppComponent,
    CreateCustomerComponent,
    NavBarComponent,
    ListCustomerComponent,
    ShowCustomerComponent,
    UpdateCustomerComponent,
    AccountOperationComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule,
    ReactiveFormsModule,
    FormsModule,
    HttpClientModule,
    NgOptimizedImage
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
