import { Injectable } from '@angular/core';
import {ProductCard} from "../../entity/main-page/main-page.component";

@Injectable({
  providedIn: 'root'
})
export class ProductCardServiceService {

  public static productCards: ProductCard[];

  constructor() { }
}
