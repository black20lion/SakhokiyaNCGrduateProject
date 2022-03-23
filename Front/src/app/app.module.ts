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
import { AuthorizedPageComponent} from "./entity/authorized-page/authorized-page.component";
import {IconAuthorizedComponent} from "./entity/icon-authorized/icon-authorized.component";
import { PersonalInfoComponent } from './entity/personal-info/personal-info.component';
import { CurrentOrdersComponent } from './entity/current-orders/current-orders.component';
import { OrdersHistoryComponent } from './entity/orders-history/orders-history.component';

registerLocaleData(localeRu, 'ru')

const routes = [
  {path: '', component: MainPageComponent},
  {path: 'authorized/account', component: PersonalAccountComponent},
  {path: 'authorized', component: AuthorizedPageComponent},
  {path: 'authorized/account/personal-info', component: PersonalInfoComponent},
  {path: 'authorized/account/current-orders', component: CurrentOrdersComponent},
  {path: 'authorized/account/orders-history', component: OrdersHistoryComponent}
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
    OrdersHistoryComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    RouterModule.forRoot(routes)
  ],
  providers: [TokenServiceService],
  bootstrap: [AppComponent]
})
export class AppModule { }
