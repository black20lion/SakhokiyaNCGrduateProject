import {Component, Input, OnInit} from '@angular/core';
import {ProductCard} from "../main-page/main-page.component";
import {CartServiceService} from "../../services/cart-service/cart-service.service";

@Component({
  selector: 'app-cart-item',
  templateUrl: './cart-item.component.html',
  styleUrls: ['./cart-item.component.scss']
})
export class CartItemComponent implements OnInit {

  @Input() productCard!: ProductCard;
  @Input() count!: number;
  @Input() index!: number;
  inCart: number = 0;
  total!: number;

  ngOnInit(): void {
  }

  ngDoCheck(): void {
    this.checkInCart();
  }


  add(): void {
    ++this.inCart;
    CartServiceService.cart.set(this.productCard.offerId, this.inCart);
    CartServiceService.cartWasChanged = true;
    CartServiceService.cart.forEach(console.log);
  }

  delete(): void {
    if (this.inCart > 0) {
      --this.inCart;
      if (this.inCart <= 0) {
        CartServiceService.cart.delete(this.productCard.offerId);
      } else {
        CartServiceService.cart.set(this.productCard.offerId, this.inCart);
      }
    }
    CartServiceService.cartWasChanged = true;
    CartServiceService.cart.forEach(console.log);
  }

  checkInCart(): void {
    let isFound = false;

    for (let currentProductInCart of CartServiceService.cart) {
      if (currentProductInCart[0] === this.productCard.offerId) {
        this.inCart = currentProductInCart[1];
        isFound = true;
        break;
      }
    }

    if (!isFound) {
      this.inCart = 0;
      this.total = 0;
    } else {
      if (this.productCard.priceOverride == null) {
        this.total = this.inCart * this.productCard.price;
      } else {
        this.total = this.inCart * this.productCard.priceOverride;
      }
    }
  }


}
