import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UploadBankStatementComponent } from './upload-bank-statement.component';

describe('UploadBankStatementComponent', () => {
  let component: UploadBankStatementComponent;
  let fixture: ComponentFixture<UploadBankStatementComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UploadBankStatementComponent]
    });
    fixture = TestBed.createComponent(UploadBankStatementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
