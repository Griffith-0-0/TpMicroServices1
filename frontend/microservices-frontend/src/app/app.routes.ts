import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    redirectTo: '/customers',
    pathMatch: 'full'
  },
  {
    path: 'customers',
    loadComponent: () => import('./components/customers/customers').then(m => m.CustomersComponent)
  },
  {
    path: 'products',
    loadComponent: () => import('./components/products/products').then(m => m.ProductsComponent)
  },
  {
    path: 'bills',
    loadComponent: () => import('./components/bills/bills').then(m => m.BillsComponent)
  }
];
