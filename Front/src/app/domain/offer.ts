export class Offer {
  id!: bigint;
  price!: number;
  price_override!: number;
  start!: Date;
  expire!: Date;
  productId!: bigint;
  sale!: boolean;
  novelty!: boolean;
  priority!: number;
}
