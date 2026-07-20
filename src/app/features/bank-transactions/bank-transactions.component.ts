import { Component } from '@angular/core';
import * as Papa from 'papaparse';

@Component({
  selector: 'app-bank-transactions',
  templateUrl: './bank-transactions.component.html',
  styleUrls: ['./bank-transactions.component.css']
})
export class BankTransactionsComponent {

  // ============================================
  // File Upload
  // ============================================

  selectedFile: File | null = null;

  // ============================================
  // Transaction Data
  // ============================================

  transactions: any[] = [];
  filteredTransactions: any[] = [];

  selectedTransaction: any = null;

  // ============================================
  // Summary Cards
  // ============================================

  totalTransactions = 0;
  totalCredits = 0;
  totalDebits = 0;
  closingBalance = 0;

  // ============================================
  // Filters
  // ============================================

  searchText = '';

  fromDate = '';

  toDate = '';

  selectedType = '';

  selectedCategory = '';

  categories: string[] = [];

  // ============================================
  // Pagination
  // ============================================

  currentPage = 1;

  pageSize = 10;

  // ============================================
  // Upload CSV
  // ============================================

  onFileSelected(event: Event): void {

    const input = event.target as HTMLInputElement;

    if (!input.files?.length) {
      return;
    }

    this.selectedFile = input.files[0];

    this.readCSV();

  }

  // ============================================
  // Read CSV
  // ============================================

  readCSV(): void {

    if (!this.selectedFile) {
      return;
    }

    Papa.parse(this.selectedFile, {

      header: true,

      skipEmptyLines: true,

      complete: (result: any) => {

        this.transactions = result.data;

        this.filteredTransactions = [...this.transactions];

        this.extractCategories();

        this.calculateSummary();

      },

      error: (error) => {

        console.error(error);

      }

    });

  }

  // ============================================
  // Summary Cards
  // ============================================

  calculateSummary(): void {

    this.totalTransactions = this.filteredTransactions.length;

    this.totalCredits = 0;

    this.totalDebits = 0;

    this.closingBalance = 0;

    this.filteredTransactions.forEach((transaction: any) => {

      const amount = Number(transaction.Amount) || 0;

      if ((transaction.Type || '').toLowerCase() === 'credit') {

        this.totalCredits += amount;

      } else {

        this.totalDebits += amount;

      }

      this.closingBalance = Number(transaction.Balance) || 0;

    });

  }

  // ============================================
  // Categories
  // ============================================

  extractCategories(): void {

    const unique = new Set<string>();

    this.transactions.forEach((transaction: any) => {

      if (transaction.Category) {

        unique.add(transaction.Category);

      }

    });

    this.categories = Array.from(unique).sort();

  }

  // ============================================
  // Apply Filters
  // ============================================

  applyFilters(): void {

    this.filteredTransactions = this.transactions.filter((transaction: any) => {

      const matchesSearch = !this.searchText ||

        (transaction.Description || '')
          .toLowerCase()
          .includes(this.searchText.toLowerCase()) ||

        (transaction.Reference || '')
          .toLowerCase()
          .includes(this.searchText.toLowerCase());

      const matchesType = !this.selectedType ||

        transaction.Type === this.selectedType;

      const matchesCategory = !this.selectedCategory ||

        transaction.Category === this.selectedCategory;

      let matchesFromDate = true;

      let matchesToDate = true;

      if (this.fromDate) {

        matchesFromDate =
          new Date(transaction.Date) >= new Date(this.fromDate);

      }

      if (this.toDate) {

        matchesToDate =
          new Date(transaction.Date) <= new Date(this.toDate);

      }

      return (

        matchesSearch &&

        matchesType &&

        matchesCategory &&

        matchesFromDate &&

        matchesToDate

      );

    });

    this.calculateSummary();

  }

  // ============================================
  // Reset Filters
  // ============================================

  resetFilters(): void {

    this.searchText = '';

    this.selectedType = '';

    this.selectedCategory = '';

    this.fromDate = '';

    this.toDate = '';

    this.filteredTransactions = [...this.transactions];

    this.calculateSummary();

  }

  // ============================================
  // Transaction Details
  // ============================================

  viewTransaction(transaction: any): void {

    this.selectedTransaction = transaction;

  }

  // ============================================
  // Export CSV
  // ============================================

  exportCSV(): void {

    const csv = Papa.unparse(this.filteredTransactions);

    const blob = new Blob([csv], {

      type: 'text/csv;charset=utf-8;'

    });

    const url = window.URL.createObjectURL(blob);

    const link = document.createElement('a');

    link.href = url;

    link.download = 'bank-transactions.csv';

    link.click();

    window.URL.revokeObjectURL(url);

  }

  // ============================================
  // Pagination
  // ============================================

  previousPage(): void {

    if (this.currentPage > 1) {

      this.currentPage--;

    }

  }

  nextPage(): void {

    const totalPages = Math.ceil(
      this.filteredTransactions.length / this.pageSize
    );

    if (this.currentPage < totalPages) {

      this.currentPage++;

    }

  }

}