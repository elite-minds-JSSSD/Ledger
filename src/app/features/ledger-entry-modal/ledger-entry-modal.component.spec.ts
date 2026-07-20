import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LedgerEntryModalComponent } from './ledger-entry-modal.component';

describe('LedgerEntryModalComponent', () => {
  let component: LedgerEntryModalComponent;
  let fixture: ComponentFixture<LedgerEntryModalComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LedgerEntryModalComponent]
    });
    fixture = TestBed.createComponent(LedgerEntryModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
