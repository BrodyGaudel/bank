import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {NgOptimizedImage} from "@angular/common";
import {HttpClientModule} from "@angular/common/http";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { CreateCustomerComponent } from './component/create-customer/create-customer.component';
import { ShowAllCustomerComponent } from './component/show-all-customer/show-all-customer.component';
import { CustomerDetailsComponent } from './component/customer-details/customer-details.component';
import { UpdateCustomerComponent } from './component/update-customer/update-customer.component';

@NgModule({
  declarations: [
    AppComponent,
    CreateCustomerComponent,
    ShowAllCustomerComponent,
    CustomerDetailsComponent,
    UpdateCustomerComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    FormsModule,
    HttpClientModule,
    NgOptimizedImage,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
