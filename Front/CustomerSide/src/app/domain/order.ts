export class Order {
  id!: bigint;
  customerId!: bigint;
  dateTime!: string;
  comments!: string;
  deliveryAddress!: string;
  totalPrice!: number;
  deliveryType!: string;
  deliveryStatus!: string;
  payStatus!: string;
  orderStatus!: string;
  phoneNumber!: string;
  email!: string;
}
