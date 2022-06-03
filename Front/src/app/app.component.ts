import {Component, OnInit, DoCheck} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";

import {Product} from "./domain/product";
import {Offer} from "./domain/offer";
import {Token} from "./domain/token";
import {Category} from "./domain/category";
import {UserInfo} from "./domain/userInfo";
import {TokenServiceService} from "./services/token-service/token-service.service";

export interface Icon {
  width: number
  height: number
  src: string
  alt: string
}

export interface ProductCard {
  productId: bigint
  offerId: bigint
  price: number
  priceOverride: number
  article: string
  productName: string
  imageUrl: string
}

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit, DoCheck {

  maleCategory!: Category[];
  femaleCategory!: Category[];
  products: Product[] = [];
  offers: Offer[] = [];
  token!: Token;
  userInfo!: UserInfo;

  password!: string;
  login!: string;
  email!: string;

  maleMenuShow: boolean = false;
  femaleMenuShow: boolean = false;
  modalWindowOpen: boolean = false;

  constructor(private http: HttpClient) {

  }

  ngOnInit() {

  }

  ngDoCheck() {
    let x = 0;
    if ((this.offers.length != 0 && this.products.length != 0)) {
      x++;
    }

    if (TokenServiceService.tokenIsOld) {

      TokenServiceService.refreshToken(this.http);
      TokenServiceService.loadRefreshTokenTimer();
      console.log(TokenServiceService.token.access_token);
    }
  }


  showFemaleMenu() {
    this.femaleMenuShow = true;
  }

  hideFemaleMenu()
    :
    void {
    this.femaleMenuShow = false;
  }

  showMaleMenu() {
    this.maleMenuShow = true;
  }

  hideMaleMenu()
    :
    void {
    this.maleMenuShow = false;
  }

  showScroll() {
    this.modalWindowOpen = false;
  }

  hideScroll() {
    this.modalWindowOpen = true;
  }

  getProductById(productId
                   :
                   bigint
  ):
    Product {
    let currentProduct = new Product();
    for (let i = 0; i < this.products.length; i++) {
      if (this.products[i].id == productId) {
        currentProduct = this.products[i]
      }
    }
    return currentProduct;
  }

  makeBig(number
            :
            number
  ) {
    return BigInt(number)
  }

  icons: Icon[] = [
    {width: 30, height: 30, src: 'assets/img/icons/search.png', alt: 'Поиск'},
    {width: 30, height: 30, src: 'assets/img/icons/user.png', alt: 'Личный кабинет'},
    {width: 30, height: 30, src: 'assets/img/icons/cart.png', alt: 'Корзина'}
  ]

  productCards: ProductCard[] = [];
  productCardsUniqueArticle: ProductCard[] = [];
  bestOffers: ProductCard[] = [];
  currentProductCard!
    :
    ProductCard;
  currentProduct: Product = new Product();

  productCardsFilled = false;


  getToken(): void {
    let body = new URLSearchParams();
    body.set('username', this.login);
    body.set('password', this.password);
    body.set('client_id', "springboot-keycloak");
    body.set('grant_type', "password");

    let options = {
      headers: new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded')
    };

    this.http
      .post<Token>('http://localhost:8080/realms/shop/protocol/openid-connect/token', body.toString(), options)
      .subscribe(result => {
        this.token = result;
      });
  }

  getUserInfo(): void {
    if (this.token != undefined) {
      let options = {
        headers: new HttpHeaders().set('Authorization', 'Bearer ' + this.token.access_token.toString())
      };

      this.http
        .get<UserInfo>('http://localhost:8080/realms/shop/protocol/openid-connect/userinfo', options)
        .subscribe(result => {
          this.userInfo = result;
          this.email = result.email;
          console.log(this.email);
        });
    }
  }
}
