import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { BillingService } from '../../services/billing';
import { Bill } from '../../models/bill.model';

@Component({
  selector: 'app-bills',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './bills.html',
  styleUrl: './bills.css'
})
export class BillsComponent implements OnInit {
  bills: Bill[] = [];
  selectedBill: Bill | null = null;
  billId: number = 1;
  isLoading = false;
  error: string | null = null;

  constructor(private billingService: BillingService) {}

  ngOnInit(): void {
    // Charger quelques factures au démarrage
    this.loadBills([1, 2, 3]);
  }

  loadBills(ids: number[]): void {
    this.bills = [];
    ids.forEach(id => {
      this.loadBill(id);
    });
  }

  loadBill(id: number): void {
    this.isLoading = true;
    this.error = null;
    this.billingService.getBillById(id).subscribe({
      next: (bill) => {
        this.bills.push(bill);
        if (!this.selectedBill) {
          this.selectedBill = bill;
        }
        this.isLoading = false;
      },
      error: (err) => {
        this.error = `Erreur lors du chargement de la facture ${id}`;
        console.error(err);
        this.isLoading = false;
      }
    });
  }

  searchBill(): void {
    if (!this.billId || this.billId <= 0) {
      this.error = 'Veuillez entrer un ID valide';
      return;
    }

    // Vérifier si la facture est déjà chargée
    const existingBill = this.bills.find(b => b.id === this.billId);
    if (existingBill) {
      this.selectedBill = existingBill;
      return;
    }

    this.loadBill(this.billId);
  }

  selectBill(bill: Bill): void {
    this.selectedBill = bill;
  }

  calculateTotal(bill: Bill): number {
    if (!bill.productItems) return 0;
    return bill.productItems.reduce((total, item) => {
      return total + (item.price * item.quantity);
    }, 0);
  }
}
