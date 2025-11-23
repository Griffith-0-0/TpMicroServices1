import { Customer } from './customer.model';
import { Product } from './product.model';

export interface ProductItem {
  id: number;
  productId: number;
  price: number;
  quantity: number;
  product?: Product;
}

export interface Bill {
  id: number;
  billDate: string;
  customerId: number;
  customer?: Customer;
  productItems?: ProductItem[];
}

