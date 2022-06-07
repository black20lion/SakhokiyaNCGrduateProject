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
  formEmail!: string;

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
  infoWasChanged: boolean = false;

  phoneReg: RegExp = new RegExp("^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$");
  emailReg: RegExp = new RegExp("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\\\[\x01-\x09\x0b\x0c\x0e-\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\\])");


  icons: Icon[] = [
    {width: 30, height: 30, src: 'assets/img/icons/search.png', alt: 'Поиск'}
  ]

  productCards: ProductCard[] = [];
  productCardsUniqueArticle: ProductCard[] = [];
  bestOffers: ProductCard[] = [];
  currentProductCard!
    :
    ProductCard;
  currentProduct: Product = new Product();

  productCardsFilled = false;

  constructor(private http: HttpClient, private router: Router) {

  }

  ngOnInit(): void {
  }

  ngDoCheck() {
    if (!TokenServiceService.infoWasChanged) {
      this.initCustomerInfo()
      this.infoWasChanged = false;
    }

    if (TokenServiceService.infoWasChanged) {
      this.infoWasChanged = true;
    }
  }


  initCustomerInfo(): void {
    this.advancedUserInfo = TokenServiceService.advancedUserInfo;

    this.formEmail = TokenServiceService.email;
    this.email = TokenServiceService.email;
  }

  markChange(): void {
    this.infoWasChanged = true;
    TokenServiceService.infoWasChanged = true;
  }

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

  saveChanges():void {
    this.http
      .post('http://localhost:8081/rest/users/updateUserInfo', null, {
        responseType: "text",
        headers: new HttpHeaders().set('Authorization', 'Bearer ' + TokenServiceService.token.access_token.toString()),
        params: new HttpParams().
        set('customerId', "" + new Number(TokenServiceService.customerId)).
        set('firstName', "" + this.advancedUserInfo.firstName).
        set('lastName', "" + this.advancedUserInfo.lastName).
        set('gender', "" + this.advancedUserInfo.gender).
        set('phoneNumber', "" + this.advancedUserInfo.phoneNumber).
        set('lastDeliveryAddress', "" + this.advancedUserInfo.lastDeliveryAddress == null ? "" : this.advancedUserInfo.lastDeliveryAddress).
        set('birthDate', "" + this.advancedUserInfo.birthDate)
      })
      .subscribe(result => {
        console.log("OK")
      }, () => {}, () => {
        this.initInfoUpdate();
      });

  }

  initInfoUpdate():void {
    let options = {
      headers: new HttpHeaders().set('Authorization', 'Bearer ' + TokenServiceService.token.access_token),
      params: new HttpParams().set('email', "" + TokenServiceService.email)
    };

    this.email = TokenServiceService.email;

    this.http
      .get<Advanceduserinfo>('http://localhost:8081/rest/users/email', options)
      .subscribe(result => {
        this.advancedUserInfo = result;
        TokenServiceService.advancedUserInfo = result;
      }, () => {
      }, () => {
        TokenServiceService.infoWasChanged = false
        this.infoWasChanged = false
      });
  }
}

