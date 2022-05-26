import {Component, Input, OnInit} from '@angular/core';
import {ProductCard} from "../main-page/main-page.component";
import {CartServiceService} from "../../services/cart-service/cart-service.service";
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {Router} from "@angular/router";
import {CookieService} from "ngx-cookie-service";
import {TokenServiceService} from "../../services/token-service/token-service.service";

@Component({
  selector: 'app-product-card-auth',
  templateUrl: './product-card-auth.component.html',
  styleUrls: ['./product-card-auth.component.scss']
})
export class ProductCardAuthComponent implements OnInit {

  constructor(private http: HttpClient, private router: Router, private cookieService: CookieService) {

  }

  @Input() productCard!: ProductCard
  @Input() index!: number
  inCart: number = 0
  error!: string;

  ngOnInit(): void {
  }

  ngDoCheck(): void {
    this.checkInCart()
  }


  add(): void {
    CartServiceService.refreshCart(this.http)

    setTimeout(() => {
      this.checkInCart();


      ++this.inCart;
      CartServiceService.cart.set(this.productCard.offerId, this.inCart);
      CartServiceService.cartWasChanged = true;
      CartServiceService.cart.forEach(console.log);


      this.http
        .post<string>('http://localhost:8081/rest/baskets/add', null, {
          headers: new HttpHeaders().set('Authorization', 'Bearer ' + TokenServiceService.token.access_token.toString()),
          params: new HttpParams().set('customer_id', "" + TokenServiceService.customerId).set('offer_id', "" + this.productCard.offerId)
        })
        .subscribe(result => {
          console.log('result')
        }, (error) => {
          this.error = error.message
          console.log(error)
        });
    }, 1000);
    }


  delete(): void {
    CartServiceService.refreshCart(this.http)
    setTimeout(() => {
      this.checkInCart();

      --this.inCart;
      CartServiceService.cart.set(this.productCard.offerId, this.inCart);
      CartServiceService.cartWasChanged = true;
      CartServiceService.cart.forEach(console.log);


      this.http
        .post<string>('http://localhost:8081/rest/baskets/remove', null, {
          headers: new HttpHeaders().set('Authorization', 'Bearer ' + TokenServiceService.token.access_token.toString()),
          params: new HttpParams().set('customer_id', "" + TokenServiceService.customerId).set('offer_id', "" + this.productCard.offerId)
        })
        .subscribe(result => {
          console.log('result')
        }, (error) => {
          this.error = error.message
          console.log(error)
        });
    }, 1000);

  }

  checkInCart(): void {
    let isFound = false;
    for (let currentProductInCart of CartServiceService.cart) {
      if (currentProductInCart[0] === this.productCard.offerId) {
        this.inCart = currentProductInCart[1];
        isFound = true;
        break;
      }
    }
    if (!isFound) {
      this.inCart = 0;
    }
  }
}
