import { Component } from '@angular/core';

@Component({
  selector: 'app-internal-ledger',
  templateUrl: './internal-ledger.component.html',
  styleUrls: ['./internal-ledger.component.css']
})
export class InternalLedgerComponent {

  selectedFile: File | null = null;

  onLedgerFileSelected(event: Event): void {

    const input = event.target as HTMLInputElement;

    if (input.files && input.files.length > 0) {

      this.selectedFile = input.files[0];

      alert('Selected File: ' + this.selectedFile.name);

    }

  }

}