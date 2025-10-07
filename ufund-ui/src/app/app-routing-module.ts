import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginPage } from './login-page/login-page';
import { HomePage } from './home-page/home-page'

const routes: Routes = [
  { path: '', component : HomePage},
  { path: 'login', component : LoginPage},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
