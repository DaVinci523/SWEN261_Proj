import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { AdminPage } from './admin-page/admin-page';
import { HomePage } from './home-page/home-page';
import { LoginPage } from './login-page/login-page';
import { CheckoutPage } from './checkout-page/checkout-page';
import { HelperPage } from './helper-page/helper-page';

const routes: Routes = [  
  { path : 'admin', component: AdminPage},
  { path : 'homepage', component: HomePage},
  { path : '', redirectTo: '/homepage', pathMatch: 'full' },
  { path: 'login', component : LoginPage},
  { path : 'checkout', component : CheckoutPage},
  { path : 'helper', component : HelperPage}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
