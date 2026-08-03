import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { BankTransactionsComponent } from './bank-transactions.component';

describe('BankTransactionsComponent', () => {
  let component: BankTransactionsComponent;
  let fixture: ComponentFixture<BankTransactionsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [FormsModule],
      declarations: [BankTransactionsComponent]
    });
    fixture = TestBed.createComponent(BankTransactionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
