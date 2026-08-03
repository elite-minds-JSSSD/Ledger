import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InternalLedgerComponent } from './internal-ledger.component';

describe('InternalLedgerComponent', () => {
  let component: InternalLedgerComponent;
  let fixture: ComponentFixture<InternalLedgerComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [InternalLedgerComponent]
    });
    fixture = TestBed.createComponent(InternalLedgerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
