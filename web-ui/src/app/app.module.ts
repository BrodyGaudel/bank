import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import {NgOptimizedImage} from "@angular/common";
import {HttpClientModule} from "@angular/common/http";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { NavbarComponent } from './template/navbar/navbar.component';
import { CustomersComponent } from './components/customer-components/customers/customers.component';
import { ShowCustomerComponent } from './components/customer-components/show-customer/show-customer.component';
import { CreateCustomerComponent } from './components/customer-components/create-customer/create-customer.component';
import { UpdateCustomerComponent } from './components/customer-components/update-customer/update-customer.component';
import { CreateAccountComponent } from './components/account-components/create-account/create-account.component';
import { ShowAccountComponent } from './components/account-components/show-account/show-account.component';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    CustomersComponent,
    ShowCustomerComponent,
    CreateCustomerComponent,
    UpdateCustomerComponent,
    CreateAccountComponent,
    ShowAccountComponent
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
