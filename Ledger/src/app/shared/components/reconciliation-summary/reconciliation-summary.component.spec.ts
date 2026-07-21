import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReconciliationSummaryComponent } from './reconciliation-summary.component';

describe('ReconciliationSummaryComponent', () => {
  let component: ReconciliationSummaryComponent;
  let fixture: ComponentFixture<ReconciliationSummaryComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ReconciliationSummaryComponent]
    });
    fixture = TestBed.createComponent(ReconciliationSummaryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
