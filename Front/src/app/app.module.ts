import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {FormsModule} from "@angular/forms";
import {registerLocaleData} from '@angular/common';
import localeRu from '@angular/common/locales/ru';

import {AppComponent} from './app.component';
import {IconComponent} from "./entity/icon/icon.component";
import {HttpClientModule} from "@angular/common/http";
import { ProductCardComponent } from './entity/productcard/product-card.component';
import {ProductCardServiceService} from "./services/product-card-service.service";

registerLocaleData(localeRu, 'ru')

@NgModule({
  declarations: [
    AppComponent,
    IconComponent,
    ProductCardComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule
  ],
  providers: [ProductCardServiceService],
  bootstrap: [AppComponent]
})
export class AppModule { }
