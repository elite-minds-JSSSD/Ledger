import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AuditReportsComponent } from './audit-reports.component';

describe('AuditReportsComponent', () => {
  let component: AuditReportsComponent;
  let fixture: ComponentFixture<AuditReportsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AuditReportsComponent]
    });
    fixture = TestBed.createComponent(AuditReportsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
