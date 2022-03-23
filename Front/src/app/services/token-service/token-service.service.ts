import { Injectable } from '@angular/core';
import {Token} from "../../domain/token";

@Injectable({
  providedIn: 'root'
})
export class TokenServiceService {
  public static customerId: bigint;
  public static isAuthorized: boolean = false;
  public static token: Token;
  public static email: string;
  constructor() { }
}
