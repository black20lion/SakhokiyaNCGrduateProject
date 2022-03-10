import {Component, OnInit, DoCheck} from '@angular/core';
import {Category} from "./domain/category";
import {HttpClient} from "@angular/common/http";
import {Product} from "./domain/product";
import {Offer} from "./domain/offer";

export interface Icon {
  width: number
  height: number
  src: string
  alt: string
}

export interface ProductCard {
  productId: bigint
  offerId: bigint
  price: number
  priceOverride: number
  article: string
  productName: string
  imageUrl: string
}

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit, DoCheck {

  maleCategory!: Category[];
  femaleCategory!: Category[];
  products: Product[] = [];
  offers: Offer[] = [];


  maleMenuShow: boolean = false;
  femaleMenuShow: boolean = false;

  constructor(private http: HttpClient) {

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
    let x = 0;
    if ((this.offers.length != 0 && this.products.length != 0)) {
      x++;
    }
    if (x === 1) {
      this.fillProductCards()
    }
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
    {width: 30, height: 30, src: 'assets/img/icons/user.png', alt: 'Личный кабинет'},
    {width: 30, height: 30, src: 'assets/img/icons/cart.png', alt: 'Корзина'}
  ]

  productCards: ProductCard[] = [];
  productCardsUniqueArticle: ProductCard[] = [];
  bestOffers: ProductCard[] = [];
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
      let articleList : string[] = [];

      for (let i = this.productCardsUniqueArticle.length - 1; i >= 0; i--) {
        if (articleList.includes(this.productCardsUniqueArticle[i].article)) {
          this.productCardsUniqueArticle.splice(i, 1);
        } else {
          articleList.push(this.productCardsUniqueArticle[i].article)
        }
      }

      this.bestOffers.push(this.productCardsUniqueArticle[0]);
      this.bestOffers.push(this.productCardsUniqueArticle[1]);
      this.bestOffers.push(this.productCardsUniqueArticle[2]);
      this.bestOffers.push(this.productCardsUniqueArticle[3]);
      this.bestOffers.push(this.productCardsUniqueArticle[4]);
      this.productCardsFilled = true
    }
  }
}
