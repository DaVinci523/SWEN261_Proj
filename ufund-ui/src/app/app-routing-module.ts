import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { NeedsCupboard } from './needs-cupboard/needs-cupboard';

const routes: Routes = [{ path: '', component: NeedsCupboard}];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
