import { NgModule, provideBrowserGlobalErrorListeners } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing-module';
import { App } from './app';
import { HomePage } from './home-page/home-page';
import { LoginPage } from './login-page/login-page';
import { AdminPage } from './admin-page/admin-page';
import { HelperPage } from './helper-page/helper-page';
import { CheckoutPage } from './checkout-page/checkout-page';
import { NeedsCupboard } from './needs-cupboard/needs-cupboard';
import { FundingBasket } from './funding-basket/funding-basket';

@NgModule({
  declarations: [
    App,
    HomePage,
    LoginPage,
    AdminPage,
    HelperPage,
    CheckoutPage,
    NeedsCupboard,
    FundingBasket
  ],
  imports: [
    BrowserModule,
    AppRoutingModule
  ],
  providers: [
    provideBrowserGlobalErrorListeners()
  ],
  bootstrap: [App]
})
export class AppModule { }
