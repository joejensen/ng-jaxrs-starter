import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HttpClientModule} from "@angular/common/http";
import {ApiModule} from "./server/api.module";
import {Configuration, ConfigurationParameters} from "./server/configuration";
import { UserListingComponent } from './user-listing/user-listing.component';

export function apiConfigFactory(): Configuration {
  const params: ConfigurationParameters = {
    basePath: '/api'
  };
  return new Configuration(params);
}

@NgModule({
  declarations: [
    AppComponent,
    UserListingComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ApiModule.forRoot(apiConfigFactory)
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
