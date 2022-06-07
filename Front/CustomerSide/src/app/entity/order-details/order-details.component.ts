import { Component, OnInit } from '@angular/core';
import {Icon, ProductCard} from "../main-page/main-page.component";
import {Category} from "../../domain/category";
import {Product} from "../../domain/product";
import {Offer} from "../../domain/offer";
import {UserInfo} from "../../domain/userInfo";
import {Token} from "../../domain/token";
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {Router} from "@angular/router";
import {CartServiceService} from "../../services/cart-service/cart-service.service";
import {ProductCardServiceService} from "../../services/product-card-service/product-card-service.service";
import {TokenServiceService} from "../../services/token-service/token-service.service";

export interface supportCartItem {
  productCard: ProductCard
  count: number
}

@Component({
  selector: 'app-order-details',
  templateUrl: './order-details.component.html',
  styleUrls: ['./order-details.component.scss']
})
export class OrderDetailsComponent implements OnInit {



  cartProductCards = new Map<ProductCard, number>();
  supportCartItems: supportCartItem[] = [];

  maleCategory!: Category[];
  femaleCategory!: Category[];
  products: Product[] = [];
  offers: Offer[] = [];
  userInfo!: UserInfo;

  token!: Token;
  password!: string;
  login!: string;
  email!: string;
  error: any;
  isEmpty!: boolean;
  total: number = 0;

  maleMenuShow: boolean = false;
  femaleMenuShow: boolean = false;
  modalWindowOpen: boolean = false;

  public name: string = TokenServiceService.advancedUserInfo.firstName + " " + TokenServiceService.advancedUserInfo.lastName;
  phone: string = TokenServiceService.advancedUserInfo.phoneNumber;
  formEmail!: string;
  deliveryType: String = "DELIVERY";
  deliveryAddress: string = TokenServiceService.advancedUserInfo.lastDeliveryAddress;
  payType: String = "CASH";
  commentary: string = "";

  phoneReg: RegExp = new RegExp("^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$");
  emailReg: RegExp = new RegExp("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\\\[\x01-\x09\x0b\x0c\x0e-\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\\])");


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

  ngOnInit() {

  }

  ngDoCheck() {

    console.log(this.deliveryType)

    console.log(this.payType)

    let x = 0;
    if ((this.offers.length != 0 && this.products.length != 0)) {
      x++;
    }
    if (x === 1) {
      this.fillProductCards()
    }

    if (CartServiceService.cartWasChanged && CartServiceService.cart.size > 0) {
      this.fillCartProductCards();
    }

    if (CartServiceService.cart.size > 0) {
      this.isEmpty = false;
    } else {
      this.isEmpty = true;
    }

    this.countTotal();
    this.email = TokenServiceService.email;
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

  logOut() {
    TokenServiceService.email = "";
    TokenServiceService.token = new Token();
    TokenServiceService.isAuthorized = false;
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
    {width: 30, height: 30, src: 'assets/img/icons/search.png', alt: 'Поиск'}
  ]

  productCards: ProductCard[] = [];
  productCardsUniqueArticle: ProductCard[] = [];
  bestOffers: ProductCard[] = [];
  offerInCart: ProductCard[] = [];
  countInCart: number[] = [];
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

      ProductCardServiceService.productCards = this.productCards;

      this.bestOffers.push(this.productCardsUniqueArticle[0]);
      this.bestOffers.push(this.productCardsUniqueArticle[1]);
      this.bestOffers.push(this.productCardsUniqueArticle[2]);
      this.bestOffers.push(this.productCardsUniqueArticle[3]);
      this.bestOffers.push(this.productCardsUniqueArticle[4]);
      this.productCardsFilled = true
      this.formEmail = TokenServiceService.email;
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
        console.log(this.token.access_token);
        TokenServiceService.loadRefreshTokenTimer();
        this.getUserInfo();
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
        }, (error) => {
          this.error = error.message
          console.log(error)
        }, () => {
          TokenServiceService.token = this.token;
          TokenServiceService.email = this.email;
          TokenServiceService.isAuthorized = true;
          this.router.navigate(['../authorized'])
        });
    }
  }

  fillCartProductCards(): void {
    for (let currentProductInCart of CartServiceService.cart) {
      for (let currentProductCard of this.productCards) {
        if (currentProductCard.offerId === currentProductInCart[0]) {
          this.cartProductCards.set(currentProductCard, currentProductInCart[1]);

          if (!this.offerInCart.includes(currentProductCard)) {
            this.offerInCart.push(currentProductCard);
          }
        }
      }
    }
    this.supportCartItems = [];

    for (let currentCardInCart of this.cartProductCards) {
      this.countInCart.push(currentCardInCart[1]);
    }

    let counter = 0;
    for (let currentCardInCart of this.cartProductCards) {
      this.supportCartItems.push({productCard: currentCardInCart[0], count: this.countInCart[counter]}
      )
      counter++;
    }
  }

  countTotal(): void {
    this.total = 0;
    for (let currentItem of this.supportCartItems) {
      let realPrice

      if (currentItem.productCard.priceOverride == null) {
        realPrice = currentItem.productCard.price
      } else {
        realPrice = currentItem.productCard.priceOverride
      }
      this.total += realPrice * currentItem.count;
    }
  }

  confirmOrder(): void {
    this.http
      .post<string>('http://localhost:8081/rest/orders/current', null, {
        headers: new HttpHeaders().set('Authorization', 'Bearer ' + TokenServiceService.token.access_token.toString()),
        params: new HttpParams().
        set('customer_id', "" + TokenServiceService.customerId).
        set('commentary', "" + this.commentary).
        set('delivery_address', "" + this.deliveryAddress).
        set('delivery_type', "" + this.deliveryType).
        set('delivery_status', "" + "NOT_SHIPPED").
        set('phone_number', "" + this.phone).
        set('email', "" + this.formEmail).
        set('name', "" + this.name).
        set('pay_type', "" + this.payType)
      })
      .subscribe(result => {

      }, (error) => {
        this.error = error.message
        console.log(error)
      });
  }
}
