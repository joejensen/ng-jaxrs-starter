import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { UserListingComponent } from "./user-listing/user-listing.component";

const routes: Routes = [
  {
    path: '',
    component: UserListingComponent,
  },
  {
    path: '',
    redirectTo: '/users',
    pathMatch: 'full'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
