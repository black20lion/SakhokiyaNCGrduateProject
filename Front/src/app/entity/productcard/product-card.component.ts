import {Component, Input, OnInit} from '@angular/core';
import {ProductCard} from "../../app.component";

@Component({
  selector: 'app-product-card',
  templateUrl: './product-card.component.html',
  styleUrls: ['./product-card.component.scss']
})
export class ProductCardComponent implements OnInit {

  @Input() productCard!: ProductCard
  @Input() index!: number


  ngOnInit(): void {
  }

}
