import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthInterceptor } from './core/interceptors/auth.interceptor';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { MainLayoutComponent } from './layout/main-layout/main-layout.component';
import { HeaderComponent } from './layout/header/header.component';
import { SidebarComponent } from './layout/sidebar/sidebar.component';
import { FooterComponent } from './layout/footer/footer.component';
import { LoginComponent } from './features/auth/login/login.component';
import { DashboardComponent } from './features/dashboard/dashboard.component';
import { CsvUploadComponent } from './features/csv-upload/csv-upload.component';
import { BankTransactionsComponent } from './features/bank-transactions/bank-transactions.component';
import { InternalLedgerComponent } from './features/internal-ledger/internal-ledger.component';
import { ReconciliationResultsComponent } from './features/reconciliation-results/reconciliation-results.component';
import { DiscrepancyAnalysisComponent } from './features/discrepancy-analysis/discrepancy-analysis.component';
import { AuditReportsComponent } from './features/audit-reports/audit-reports.component';
import { ProfileComponent } from './features/profile/profile.component';
import { LoaderComponent } from './shared/components/loader/loader.component';
import { PaginationComponent } from './shared/components/pagination/pagination.component';
import { StatusBadgeComponent } from './shared/components/status-badge/status-badge.component';
import { StatCardComponent } from './shared/components/stat-card/stat-card.component';
import { ReconciliationSummaryComponent } from './shared/components/reconciliation-summary/reconciliation-summary.component';
import { ReconciliationTrendComponent } from './shared/components/reconciliation-trend/reconciliation-trend.component';
import { UploadBankStatementComponent } from './shared/components/upload-bank-statement/upload-bank-statement.component';
import { RegisterComponent } from './features/auth/register/register.component';
import { ReactiveFormsModule } from '@angular/forms';
import { LandingComponent } from './pages/landing/landing.component';
import { LedgerEntryModalComponent } from './features/ledger-entry-modal/ledger-entry-modal.component';

@NgModule({
  declarations: [
    AppComponent,
    MainLayoutComponent,
    HeaderComponent,
    SidebarComponent,
    FooterComponent,
    LoginComponent,
    DashboardComponent,
    CsvUploadComponent,
    BankTransactionsComponent,
    InternalLedgerComponent,
    ReconciliationResultsComponent,
    DiscrepancyAnalysisComponent,
    AuditReportsComponent,
    ProfileComponent,
    LoaderComponent,
    PaginationComponent,
    StatusBadgeComponent,
    StatCardComponent,
    ReconciliationSummaryComponent,
    ReconciliationTrendComponent,
    UploadBankStatementComponent,
    RegisterComponent,
    LandingComponent,
    LedgerEntryModalComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    FormsModule,
    HttpClientModule
  ]
  ,
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
