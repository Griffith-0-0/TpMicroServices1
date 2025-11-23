import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CustomerService } from '../../services/customer';
import { Customer } from '../../models/customer.model';

@Component({
  selector: 'app-customers',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './customers.html',
  styleUrl: './customers.css'
})
export class CustomersComponent implements OnInit {
  customers: Customer[] = [];
  selectedCustomer: Customer | null = null;
  newCustomer: Partial<Customer> = { name: '', email: '' };
  isEditing = false;
  isLoading = false;
  error: string | null = null;

  constructor(private customerService: CustomerService) {}

  ngOnInit(): void {
    this.loadCustomers();
  }

  loadCustomers(): void {
    this.isLoading = true;
    this.error = null;
    this.customerService.getAllCustomers().subscribe({
      next: (response: any) => {
        // Spring Data REST retourne les données dans _embedded
        this.customers = response._embedded?.customers || [];
        this.isLoading = false;
      },
      error: (err) => {
        this.error = `Erreur lors du chargement des clients: ${err.message || err.statusText || 'Erreur inconnue'}`;
        console.error('Erreur détaillée:', err);
        this.isLoading = false;
      }
    });
  }

  selectCustomer(customer: Customer): void {
    this.selectedCustomer = customer;
    this.isEditing = false;
  }

  createCustomer(): void {
    if (!this.newCustomer.name || !this.newCustomer.email) {
      this.error = 'Veuillez remplir tous les champs';
      return;
    }

    this.isLoading = true;
    this.customerService.createCustomer(this.newCustomer).subscribe({
      next: () => {
        this.loadCustomers();
        this.newCustomer = { name: '', email: '' };
        this.error = null;
      },
      error: (err) => {
        this.error = `Erreur lors de la création du client: ${err.message || err.statusText || 'Erreur inconnue'}`;
        console.error('Erreur détaillée:', err);
        this.isLoading = false;
      }
    });
  }

  updateCustomer(): void {
    if (!this.selectedCustomer) return;

    this.isLoading = true;
    this.customerService.updateCustomer(this.selectedCustomer.id, this.selectedCustomer).subscribe({
      next: () => {
        this.loadCustomers();
        this.isEditing = false;
        this.error = null;
      },
      error: (err) => {
        this.error = 'Erreur lors de la mise à jour du client';
        console.error(err);
        this.isLoading = false;
      }
    });
  }

  deleteCustomer(id: number): void {
    if (!confirm('Êtes-vous sûr de vouloir supprimer ce client ?')) return;

    this.isLoading = true;
    this.customerService.deleteCustomer(id).subscribe({
      next: () => {
        this.loadCustomers();
        this.selectedCustomer = null;
        this.error = null;
      },
      error: (err) => {
        this.error = 'Erreur lors de la suppression du client';
        console.error(err);
        this.isLoading = false;
      }
    });
  }

  editCustomer(): void {
    this.isEditing = true;
  }

  cancelEdit(): void {
    this.isEditing = false;
    this.loadCustomers();
  }
}
