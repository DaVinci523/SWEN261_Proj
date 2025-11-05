import { NgModule} from '@angular/core';
import { provideBrowserGlobalErrorListeners } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';


import { AppRoutingModule } from './app-routing-module';
import { App } from './app';
import { HomePage } from './home-page/home-page';
import { LoginPage } from './login-page/login-page';
import { AdminPage } from './admin-page/admin-page';
import { HelperPage } from './helper-page/helper-page';
import { CheckoutPage } from './checkout-page/checkout-page';
import { NeedsCupboard } from './needs-cupboard/needs-cupboard';
import { FundingBasket } from './funding-basket/funding-basket';
import { NeedDetailComponent } from './need-details/need-details';
import { NavBar } from './nav-bar/nav-bar';
import { Checkout } from './checkout/checkout';
import { ReviewsPage } from './review-page/review-page';
@NgModule({
  declarations: [
    App,
    HomePage,
    LoginPage,
    AdminPage,
    HelperPage,
    CheckoutPage,
    NeedsCupboard,
    FundingBasket,
    NeedDetailComponent,
    NavBar,
    Checkout,
    ReviewsPage
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule

  ],
  providers: [
    provideBrowserGlobalErrorListeners()
  ],
  bootstrap: [App]
})
export class AppModule { }
