import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class CartServiceService {

  constructor() { }

  public static cart = new Map<bigint, number>();

  public static cartWasChanged: boolean = false;

}
