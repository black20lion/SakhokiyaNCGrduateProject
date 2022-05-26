import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {FormsModule} from "@angular/forms";
import {registerLocaleData} from '@angular/common';
import localeRu from '@angular/common/locales/ru';

import {AppComponent} from './app.component';
import {IconComponent} from "./entity/icon/icon.component";
import {HttpClientModule} from "@angular/common/http";
import { ProductCardComponent } from './entity/productcard/product-card.component';
import { PersonalAccountComponent } from './entity/personal-account/personal-account.component';
import {RouterModule} from "@angular/router";
import { MainPageComponent } from './entity/main-page/main-page.component';
import {TokenServiceService} from "./services/token-service/token-service.service";
import {CartServiceService} from "./services/cart-service/cart-service.service";
import { AuthorizedPageComponent} from "./entity/authorized-page/authorized-page.component";
import {IconAuthorizedComponent} from "./entity/icon-authorized/icon-authorized.component";
import { PersonalInfoComponent } from './entity/personal-info/personal-info.component';
import { CurrentOrdersComponent } from './entity/current-orders/current-orders.component';
import { OrdersHistoryComponent } from './entity/orders-history/orders-history.component';
import { RegistrationSuccessComponent } from './entity/registration-success/registration-success.component';
import {CookieService} from "ngx-cookie-service";
import {CartComponent} from "./entity/cart/cart.component";
import {ProductCardServiceService} from "./services/product-card-service/product-card-service.service";
import {CartItemComponent} from "./entity/cart-item/cart-item.component";
import {ProductCardAuthComponent} from "./entity/product-card-auth/product-card-auth.component";
import {CartAuthComponent} from "./entity/cart-auth/cart-auth.component";

registerLocaleData(localeRu, 'ru')

const routes = [
  {path: '', component: MainPageComponent},
  {path: 'authorized/account', component: PersonalAccountComponent},
  {path: 'authorized', component: AuthorizedPageComponent},
  {path: 'authorized/account/personal-info', component: PersonalInfoComponent},
  {path: 'authorized/account/current-orders', component: CurrentOrdersComponent},
  {path: 'authorized/account/orders-history', component: OrdersHistoryComponent},
  {path: 'registration-success', component: RegistrationSuccessComponent},
  {path: 'cart', component: CartComponent},
  {path: 'authorized/cart', component: CartAuthComponent}
]

@NgModule({
  declarations: [
    AppComponent,
    IconComponent,
    ProductCardComponent,
    PersonalAccountComponent,
    MainPageComponent,
    AuthorizedPageComponent,
    IconAuthorizedComponent,
    PersonalInfoComponent,
    CurrentOrdersComponent,
    OrdersHistoryComponent,
    RegistrationSuccessComponent,
    CartComponent,
    CartItemComponent,
    ProductCardAuthComponent,
    CartAuthComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    RouterModule.forRoot(routes)
  ],
  providers: [TokenServiceService, CookieService, CartServiceService, ProductCardServiceService],
  bootstrap: [AppComponent]
})
export class AppModule { }
