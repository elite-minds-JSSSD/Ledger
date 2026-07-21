import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DiscrepancyAnalysisComponent } from './discrepancy-analysis.component';

describe('DiscrepancyAnalysisComponent', () => {
  let component: DiscrepancyAnalysisComponent;
  let fixture: ComponentFixture<DiscrepancyAnalysisComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DiscrepancyAnalysisComponent]
    });
    fixture = TestBed.createComponent(DiscrepancyAnalysisComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
