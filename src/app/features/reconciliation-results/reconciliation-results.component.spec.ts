import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReconciliationResultsComponent } from './reconciliation-results.component';

describe('ReconciliationResultsComponent', () => {
  let component: ReconciliationResultsComponent;
  let fixture: ComponentFixture<ReconciliationResultsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ReconciliationResultsComponent]
    });
    fixture = TestBed.createComponent(ReconciliationResultsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
