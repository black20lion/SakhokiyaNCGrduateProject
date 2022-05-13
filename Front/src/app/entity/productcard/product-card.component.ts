import {Component, Input, OnInit} from '@angular/core';
import {ProductCard} from "../../app.component";
import {CartServiceService} from "../../services/cart-service/cart-service.service";

@Component({
  selector: 'app-product-card',
  templateUrl: './product-card.component.html',
  styleUrls: ['./product-card.component.scss']
})
export class ProductCardComponent implements OnInit {

  @Input() productCard!: ProductCard
  @Input() index!: number
  inCart: number = 0


  ngOnInit(): void {
  }


  add(): void {
    ++this.inCart;
    CartServiceService.cart.set(this.productCard.offerId, this.inCart);
    CartServiceService.cart.forEach(console.log);
  }

  delete(): void {
    if (this.inCart != 0) {
      --this.inCart;
      if (this.inCart === 0) {
        CartServiceService.cart.delete(this.productCard.offerId);
      } else {
        CartServiceService.cart.set(this.productCard.offerId, this.inCart);
      }
    }
    CartServiceService.cart.forEach(console.log);
  }
}
