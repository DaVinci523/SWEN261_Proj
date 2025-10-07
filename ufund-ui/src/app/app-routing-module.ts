import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminPage } from './admin-page/admin-page';
import { HomePage } from './home-page/home-page';

const routes: Routes = [
  { path : 'admin', component: AdminPage},
  { path : 'app-homepage', component: HomePage},
  { path : '', redirectTo: '/app-homepage', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
