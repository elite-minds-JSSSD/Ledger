import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReconciliationTrendComponent } from './reconciliation-trend.component';

describe('ReconciliationTrendComponent', () => {
  let component: ReconciliationTrendComponent;
  let fixture: ComponentFixture<ReconciliationTrendComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ReconciliationTrendComponent]
    });
    fixture = TestBed.createComponent(ReconciliationTrendComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
