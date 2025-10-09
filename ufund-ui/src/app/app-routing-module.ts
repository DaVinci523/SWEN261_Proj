import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { AdminPage } from './admin-page/admin-page';
import { HomePage } from './home-page/home-page';
import { LoginPage } from './login-page/login-page';

const routes: Routes = [
  { path : 'admin', component: AdminPage},
  { path : 'homepage', component: HomePage},
  { path : '', redirectTo: '/homepage', pathMatch: 'full' },
  { path: 'login', component : LoginPage}

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
