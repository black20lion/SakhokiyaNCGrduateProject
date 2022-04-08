import { Injectable } from '@angular/core';
import {Token} from "../../domain/token";
import {HttpClient, HttpHeaders} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class TokenServiceService {
  public static customerId: bigint;
  public static isAuthorized: boolean = false;
  public static token: Token;
  public static email: string;
  public static tokenIsOld: boolean = false;
  constructor() { }

  public static refreshToken(http: HttpClient) {
    if (this.token.refresh_token.length > 0) {
      let body = new URLSearchParams();
      body.set('client_id', "springboot-keycloak");
      body.set('grant_type', "refresh_token");
      body.set('refresh_token', this.token.refresh_token);

      let options = {
        headers: new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded')
      };

      http
        .post<Token>('http://localhost:8080/realms/shop/protocol/openid-connect/token', body.toString(), options)
        .subscribe((result) => {
          this.token = result;
        });
    }
    this.tokenIsOld = false;
  }

  public static loadRefreshTokenTimer() {
    setTimeout(() => {
      this.tokenIsOld = true}, 10000);
  }
}
