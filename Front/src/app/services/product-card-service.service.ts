import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ProductCardServiceService {

  consoleText(arg: string) {
    console.log(arg)
  }

  constructor() { }
}
