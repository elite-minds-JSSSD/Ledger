import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { MainLayoutComponent } from './layout/main-layout/main-layout.component';

import { DashboardComponent } from './features/dashboard/dashboard.component';
import { CsvUploadComponent } from './features/csv-upload/csv-upload.component';
import { BankTransactionsComponent } from './features/bank-transactions/bank-transactions.component';
import { InternalLedgerComponent } from './features/internal-ledger/internal-ledger.component';
import { ReconciliationResultsComponent } from './features/reconciliation-results/reconciliation-results.component';
import { DiscrepancyAnalysisComponent } from './features/discrepancy-analysis/discrepancy-analysis.component';
import { AuditReportsComponent } from './features/audit-reports/audit-reports.component';
import { ProfileComponent } from './features/profile/profile.component';
import { LoginComponent } from './features/auth/login/login.component';
import { RegisterComponent } from './features/auth/register/register.component';
import { LandingComponent } from './pages/landing/landing.component';
const routes: Routes = [

  // Public Pages
  {
    path: '',
    component: LandingComponent
  },

  {
    path: 'login',
    component: LoginComponent
  },

  {
    path: 'register',
    component: RegisterComponent
  },

  // Main Application
  {
    path: 'app',
    component: MainLayoutComponent,
    children: [

      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
      },

      {
        path: 'dashboard',
        component: DashboardComponent
      },

      {
        path: 'csv-upload',
        component: CsvUploadComponent
      },

      {
        path: 'bank-transactions',
        component: BankTransactionsComponent
      },

      {
        path: 'internal-ledger',
        component: InternalLedgerComponent
      },

      {
        path: 'reconciliation-results',
        component: ReconciliationResultsComponent
      },

      {
        path: 'discrepancy-analysis',
        component: DiscrepancyAnalysisComponent
      },

      {
        path: 'audit-reports',
        component: AuditReportsComponent
      },

      {
        path: 'profile',
        component: ProfileComponent
      }

    ]
  },

  {
    path: '**',
    redirectTo: ''
  }

];
@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }