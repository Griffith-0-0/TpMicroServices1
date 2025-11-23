import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { InventoryService } from '../../services/inventory';
import { Product } from '../../models/product.model';

@Component({
  selector: 'app-products',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './products.html',
  styleUrl: './products.css'
})
export class ProductsComponent implements OnInit {
  products: Product[] = [];
  selectedProduct: Product | null = null;
  newProduct: Partial<Product> = { name: '', price: 0, quantity: 0 };
  isEditing = false;
  isLoading = false;
  error: string | null = null;

  constructor(private inventoryService: InventoryService) {}

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts(): void {
    this.isLoading = true;
    this.error = null;
    this.inventoryService.getAllProducts().subscribe({
      next: (response: any) => {
        this.products = response._embedded?.products || [];
        this.isLoading = false;
      },
      error: (err) => {
        this.error = `Erreur lors du chargement des produits: ${err.message || err.statusText || 'Erreur inconnue'}`;
        console.error('Erreur détaillée:', err);
        this.isLoading = false;
      }
    });
  }

  selectProduct(product: Product): void {
    this.selectedProduct = product;
    this.isEditing = false;
  }

  createProduct(): void {
    if (!this.newProduct.name || !this.newProduct.price || !this.newProduct.quantity) {
      this.error = 'Veuillez remplir tous les champs';
      return;
    }

    this.isLoading = true;
    this.inventoryService.createProduct(this.newProduct).subscribe({
      next: () => {
        this.loadProducts();
        this.newProduct = { name: '', price: 0, quantity: 0 };
        this.error = null;
      },
      error: (err) => {
        this.error = `Erreur lors de la création du produit: ${err.message || err.statusText || 'Erreur inconnue'}`;
        console.error('Erreur détaillée:', err);
        this.isLoading = false;
      }
    });
  }

  updateProduct(): void {
    if (!this.selectedProduct) return;

    this.isLoading = true;
    this.inventoryService.updateProduct(this.selectedProduct.id, this.selectedProduct).subscribe({
      next: () => {
        this.loadProducts();
        this.isEditing = false;
        this.error = null;
      },
      error: (err) => {
        this.error = 'Erreur lors de la mise à jour du produit';
        console.error(err);
        this.isLoading = false;
      }
    });
  }

  deleteProduct(id: number): void {
    if (!confirm('Êtes-vous sûr de vouloir supprimer ce produit ?')) return;

    this.isLoading = true;
    this.inventoryService.deleteProduct(id).subscribe({
      next: () => {
        this.loadProducts();
        this.selectedProduct = null;
        this.error = null;
      },
      error: (err) => {
        this.error = 'Erreur lors de la suppression du produit';
        console.error(err);
        this.isLoading = false;
      }
    });
  }

  editProduct(): void {
    this.isEditing = true;
  }

  cancelEdit(): void {
    this.isEditing = false;
    this.loadProducts();
  }
}
