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

@Component({
  selector: 'app-personal-info',
  templateUrl: './personal-info.component.html',
  styleUrls: ['./personal-info.component.scss']
})
export class PersonalInfoComponent implements OnInit {

  advancedUserInfo!: Advanceduserinfo;
  methodHasBeenCalled: boolean = false;
  ready: boolean = false;
  email!: string;

  constructor(private http: HttpClient, private router: Router) {

    this.http.get<Category[]>('http://localhost:8081/rest/categories/MALE').subscribe(result => {
      this.maleCategory = result;
    });

    this.http.get<Category[]>('http://localhost:8081/rest/categories/FEMALE').subscribe(result => {
      this.femaleCategory = result;
    });

    this.http.get<Product[]>('http://localhost:8081/rest/products').subscribe(result => {
      this.products = result;
    });

    this.http.get<Offer[]>('http://localhost:8081/rest/offers').subscribe(result => {
      this.offers = result;
    });

  }

  showCustomerInfo(): void {
    if (!this.methodHasBeenCalled) {
      let options = {
        headers: new HttpHeaders().set('Authorization', 'Bearer ' + TokenServiceService.token.access_token),
        params: new HttpParams().set('email', "" + TokenServiceService.email)
      };

      this.email = TokenServiceService.email;

      this.http
        .get<Advanceduserinfo>('http://localhost:8081/rest/users/email', options)
        .subscribe(result => {
          this.advancedUserInfo = result;
        });
      this.methodHasBeenCalled = true;
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
      this.showCustomerInfo()
    }
    if (this.advancedUserInfo != undefined) {
      setTimeout(() => {
        this.ready =true;
      }, 500);
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

  fillProductCards() {
    if (!this.productCardsFilled) {
      for (let i = 0; i < this.offers.length; i++) {
        this.currentProduct = this.getProductById(this.offers[i].productId);

        this.currentProductCard = {
          productId: this.currentProduct.id,
          offerId: this.offers[i].id,
          price: this.offers[i].price,
          priceOverride: this.offers[i].price_override,
          article: this.currentProduct.article,
          productName: this.currentProduct.productName,
          imageUrl: this.currentProduct.imageUrl
        }
        this.productCards.push(this.currentProductCard)
      }

      this.productCardsUniqueArticle = this.productCards;
      let articleList: string[] = [];

      for (let i = this.productCardsUniqueArticle.length - 1; i >= 0; i--) {
        if (articleList.includes(this.productCardsUniqueArticle[i].article)) {
          this.productCardsUniqueArticle.splice(i, 1);
        } else {
          articleList.push(this.productCardsUniqueArticle[i].article)
        }
      }

      this.bestOffers.push(this.productCardsUniqueArticle[0]);
      this.bestOffers.push(this.productCardsUniqueArticle[1]);
      this.bestOffers.push(this.productCardsUniqueArticle[2]);
      this.bestOffers.push(this.productCardsUniqueArticle[3]);
      this.bestOffers.push(this.productCardsUniqueArticle[4]);
      this.productCardsFilled = true
    }
  }

  changeLogin(event: any) {
    this.login = event.target.value;
  }

  changePassword(event: any) {
    this.password = event.target.value;
  }

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
      .subscribe((result) => {
        this.token = result;
      }, (error) => {
        this.error = error.message
        console.log(error)
      }, () => {
        this.getUserInfo()
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
          TokenServiceService.token = this.token;
          TokenServiceService.email = this.email;
        });
    }
  }
}
