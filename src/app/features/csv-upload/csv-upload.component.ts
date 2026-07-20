import { Component } from '@angular/core';

@Component({
  selector: 'app-csv-upload',
  templateUrl: './csv-upload.component.html',
  styleUrls: ['./csv-upload.component.css']
})
export class CsvUploadComponent {

  bankFile: File | null = null;
  ledgerFile: File | null = null;

  validationStatus = 'Pending';
  overallStatus = 'Pending';

  // ==========================
  // Bank File
  // ==========================
  onBankFileSelected(event: Event): void {

    const input = event.target as HTMLInputElement;

    if (input.files && input.files.length > 0) {
      this.bankFile = input.files[0];
    }
  }

  // ==========================
  // Ledger File
  // ==========================
  onLedgerFileSelected(event: Event): void {

    const input = event.target as HTMLInputElement;

    if (input.files && input.files.length > 0) {
      this.ledgerFile = input.files[0];
    }

  }

  // ==========================
  // Validate
  // ==========================
  validateFiles(): void {

    if (!this.bankFile) {
      alert('Please select Bank Statement CSV.');
      return;
    }

    if (!this.ledgerFile) {
      alert('Please select Internal Ledger CSV.');
      return;
    }

    if (
      !this.bankFile.name.endsWith('.csv') ||
      !this.ledgerFile.name.endsWith('.csv')
    ) {
      alert('Only CSV files are allowed.');
      return;
    }

    this.validationStatus = 'Validated';
    this.overallStatus = 'Ready';

    alert('Files validated successfully.');

  }

  // ==========================
  // Upload
  // ==========================
  uploadFiles(): void {

    if (this.validationStatus !== 'Validated') {
      alert('Please validate the files first.');
      return;
    }

    this.overallStatus = 'Uploaded';

    alert('Files uploaded successfully.');

  }

  // ==========================
  // Reset
  // ==========================
  resetFiles(): void {

    this.bankFile = null;
    this.ledgerFile = null;

    this.validationStatus = 'Pending';
    this.overallStatus = 'Pending';

  }

}