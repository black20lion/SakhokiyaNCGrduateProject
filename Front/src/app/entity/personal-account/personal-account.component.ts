import {Component, OnInit} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {Advanceduserinfo} from "../../domain/advanceduserinfo";
import {TokenServiceService} from "../../services/token-service/token-service.service";
import {Category} from "../../domain/category";
import {Product} from "../../domain/product";
import {Offer} from "../../domain/offer";
import {UserInfo} from "../../domain/userInfo";
import {Token} from "../../domain/token";
import {Router} from "@angular/router";
import {Icon} from "../authorized-page/authorized-page.component";

@Component({
  selector: 'app-personal-account',
  templateUrl: './personal-account.component.html',
  styleUrls: ['./personal-account.component.scss']
})
export class PersonalAccountComponent implements OnInit {

  advancedUserInfo!: Advanceduserinfo;
  methodHasBeenCalled: boolean = false;
  ready: boolean = false;
  email!: string;

  constructor(private http: HttpClient, private router: Router) {

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
          TokenServiceService.customerId = this.advancedUserInfo.customerId;
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
      }, 0);
    }

    if (TokenServiceService.isAuthorized === false) {
      this.router.navigate(['..']);
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
    {width: 30, height: 30, src: 'assets/img/icons/user.png', alt: 'Личный кабинет'}
  ]

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
