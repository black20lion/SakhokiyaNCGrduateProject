import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {TokenServiceService} from "../token-service/token-service.service";
import {Basketitem} from "../../domain/basketitem";
import {Router} from "@angular/router";
import {CookieService} from "ngx-cookie-service";

@Injectable({
  providedIn: 'root'
})
export class CartServiceService {



  constructor(private http: HttpClient, private router: Router, private cookieService: CookieService) { }

  public static cart = new Map<bigint, number>();

  public static cartWasChanged: boolean = false;
  public static cartWasRefreshed: boolean = false;
  public static refreshedCart: Basketitem[] = []


  public static refreshCart(http: HttpClient):void {

    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer ' + TokenServiceService.token.access_token.toString())
    };

    http
      .get<Basketitem[]>('http://localhost:8081/rest/baskets/' + TokenServiceService.customerId.toString(), options)
      .subscribe(result => {
        this.refreshedCart = result
      }, (error) => {
        error = error.message
      }, () => {
        console.log(CartServiceService.refreshedCart)
        CartServiceService.cart.clear()
        for (let current of CartServiceService.refreshedCart) {
          CartServiceService.cart.set(current.offerId, current.quantity)
        }
        CartServiceService.refreshedCart = [];
        console.log(CartServiceService.cart)
        console.log(CartServiceService.refreshedCart)
        CartServiceService.cartWasChanged = true;
        CartServiceService.cartWasRefreshed = true;
      });
  }

}
