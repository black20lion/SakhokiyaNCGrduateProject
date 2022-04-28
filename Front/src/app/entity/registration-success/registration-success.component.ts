import { Component, OnInit } from '@angular/core';
import {Advanceduserinfo} from "../../domain/advanceduserinfo";
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {Router} from "@angular/router";
import {Category} from "../../domain/category";
import {Product} from "../../domain/product";
import {Offer} from "../../domain/offer";
import {TokenServiceService} from "../../services/token-service/token-service.service";
import {UserInfo} from "../../domain/userInfo";
import {Token} from "../../domain/token";
import {Icon, ProductCard} from "../authorized-page/authorized-page.component";
import {CookieService} from "ngx-cookie-service";

@Component({
  selector: 'app-registration-success',
  templateUrl: './registration-success.component.html',
  styleUrls: ['./registration-success.component.scss']
})
export class RegistrationSuccessComponent implements OnInit {

  advancedUserInfo!: Advanceduserinfo;
  methodHasBeenCalled: boolean = false;
  ready: boolean = false;
  email!: string;
  hasBeenInitialized: boolean = false;

  constructor(private http: HttpClient, private router: Router, private cookieService: CookieService) {

    let URL = window.location.href;
    this.code = URL.substring(142, URL.length);
    this.state = URL.substring(49, 85);
    console.log(this.code);
    console.log(this.state)
    if (!this.hasBeenInitialized) {
      this.getToken();
      this.hasBeenInitialized=true;
    }
  }


  ngOnInit(): void {
  }

  ngDoCheck() {
    let x = 0;
    if ((TokenServiceService.token != undefined && TokenServiceService.email != undefined)) {
      x++;
    }
    if (x === 1) {

    }
    if (this.advancedUserInfo != undefined) {
      setTimeout(() => {
        this.ready = true;
      }, 500);
    }
    if (TokenServiceService.isAuthorized === true) {
      this.router.navigate(['../authorized']);
    }
  }

  maleCategory!: Category[];
  femaleCategory!: Category[];
  products: Product[] = [];
  offers: Offer[] = [];
  userInfo!: UserInfo;

  token!: Token;
  password!: string;
  login!: string;
  error: any;
  code!: string;
  state!:string;

  maleMenuShow: boolean = false;
  femaleMenuShow: boolean = false;
  modalWindowOpen: boolean = false;


  logOut() {
    TokenServiceService.email = "";
    TokenServiceService.token = new Token();
    TokenServiceService.isAuthorized = false;
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


  changeLogin(event: any) {
    this.login = event.target.value;
  }

  changePassword(event: any) {
    this.password = event.target.value;
  }


  getToken(): void {
    let body = new URLSearchParams();
    body.set('client_id', "springboot-keycloak");
    body.set('grant_type', "authorization_code");
    body.set('redirect_uri', "http://localhost:4200/registration-success");
    body.set('code', this.code);

    let options = {
      headers: new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded')
    };

    this.http
      .post<Token>('http://localhost:8080/realms/shop/protocol/openid-connect/token', body.toString(), options)
      .subscribe((result) => {
        this.token = result;
      }, (error) => {
        this.error = error.message
        console.log(error)
      }, () => {
        console.log(this.token.access_token);
        TokenServiceService.loadRefreshTokenTimer();
        this.getUserInfo();
      });
  }

  getUserInfo():void {
    if (this.token != undefined) {
      let options = {
        headers: new HttpHeaders().set('Authorization', 'Bearer ' + this.token.access_token.toString())
      };

      this.http
        .get<UserInfo>('http://localhost:8080/realms/shop/protocol/openid-connect/userinfo', options)
        .subscribe(result => {
          this.userInfo = result;
          this.email = result.email;
        }, (error) => {
          this.error = error.message
          console.log(error)
        }, () => {
          console.log(this.userInfo.given_name)
          console.log(this.userInfo.family_name)
          console.log(this.userInfo.email)
          console.log(this.cookieService.get('JSESSIONID'))
          this.createUser();
          TokenServiceService.token = this.token;
          TokenServiceService.email = this.email;
          TokenServiceService.isAuthorized = true;
          this.router.navigate(['../authorized'])
        });
    }
  }

  createUser():void {



    this.http
      .post<string>('http://localhost:8081/rest/users/createUser', null, {
        headers: new HttpHeaders().set('Authorization', 'Bearer ' + this.token.access_token.toString()),
        params: new HttpParams().set('firstName', this.userInfo.given_name).set('lastName', this.userInfo.family_name).set('email', this.userInfo.email)
      })
      .subscribe(result => {
        console.log('result')
      }, (error) => {
        this.error = error.message
        console.log(error)
      }, () => {

      });
  }


}
