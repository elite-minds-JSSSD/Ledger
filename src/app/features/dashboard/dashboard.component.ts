import { AfterViewInit, Component, OnDestroy } from '@angular/core';
import {
  Chart,
  DoughnutController,
  LineController,
  LineElement,
  PointElement,
  LinearScale,
  CategoryScale,
  ArcElement,
  Tooltip,
  Legend,
  Filler
} from 'chart.js';

/* =========================================================
   Register Chart.js Components
========================================================= */

Chart.register(
  DoughnutController,
  LineController,
  LineElement,
  PointElement,
  LinearScale,
  CategoryScale,
  ArcElement,
  Tooltip,
  Legend,
  Filler
);

/* =========================================================
   Interface
========================================================= */

interface ReconciliationItem {

  date: string;

  description: string;

  ledger: string;

  bank: string;

  status: 'Matched' | 'Mismatch';

}

/* =========================================================
   Dashboard Component
========================================================= */

@Component({

  selector: 'app-dashboard',

  templateUrl: './dashboard.component.html',

  styleUrls: ['./dashboard.component.css']

})

export class DashboardComponent
implements AfterViewInit, OnDestroy {

  /* ======================================
     Search
  ====================================== */

  searchText: string = '';

  /* ======================================
     Selected Upload
  ====================================== */

  selectedFileName: string = '';

  /* ======================================
     Charts
  ====================================== */

  reconciliationChart!: Chart;

  trendChart!: Chart;

  /* ======================================
     Dashboard Statistics
  ====================================== */

  dashboardStats = {

    totalTransactions: 12457,

    matched: 9856,

    unmatched: 1963,

    suspicious: 638,

    duplicate: 153,

    reconciliationPercentage: 79.18

  };

  /* ======================================
     Constructor
  ====================================== */

  constructor() {

    console.log('Dashboard Loaded');

  }

  /* ======================================
     Lifecycle
  ====================================== */

  ngAfterViewInit(): void {

    this.createReconciliationChart();

    this.createTrendChart();

  }

  /* ======================================
     Reconciliation Doughnut Chart
  ====================================== */

  createReconciliationChart(): void {

    if (this.reconciliationChart) {

      this.reconciliationChart.destroy();

    }

    this.reconciliationChart = new Chart(

      'reconciliationChart',

      {

        type: 'doughnut',

        data: {

          labels: [

            'Matched',

            'Unmatched',

            'Suspicious',

            'Duplicate'

          ],

          datasets: [

            {

              data: [

                this.dashboardStats.matched,

                this.dashboardStats.unmatched,

                this.dashboardStats.suspicious,

                this.dashboardStats.duplicate

              ],

              backgroundColor: [

                '#22C55E',

                '#EF4444',

                '#F59E0B',

                '#3B82F6'

              ],

              borderWidth: 0,

              hoverOffset: 12

            }

          ]

        },

        options: {

          responsive: true,

          maintainAspectRatio: false,

          cutout: '70%',

          plugins: {

            legend: {

              position: 'bottom',

              labels: {

                padding: 20,

                usePointStyle: true,

                pointStyle: 'circle',

                font: {

                  size: 13

                }

              }

            }

          }

        }

      }

    );

  }
    /* ======================================
     Monthly Trend Chart
  ====================================== */

  createTrendChart(): void {

    if (this.trendChart) {

      this.trendChart.destroy();

    }

    this.trendChart = new Chart(

      'trendChart',

      {

        type: 'line',

        data: {

          labels: [

            'Jan',

            'Feb',

            'Mar',

            'Apr',

            'May',

            'Jun'

          ],

          datasets: [

            {

              label: 'Reconciliation %',

              data: [

                68,

                72,

                79,

                83,

                88,

                91

              ],

              borderColor: '#4F46E5',

              backgroundColor: 'rgba(79,70,229,0.15)',

              fill: true,

              tension: 0.4,

              pointRadius: 5,

              pointHoverRadius: 7,

              pointBackgroundColor: '#4F46E5'

            }

          ]

        },

        options: {

          responsive: true,

          maintainAspectRatio: false,

          plugins: {

            legend: {

              display: false

            }

          },

          scales: {

            y: {

              beginAtZero: false,

              min: 60,

              max: 100,

              ticks: {

                stepSize: 10

              }

            }

          }

        }

      }

    );

  }

  /* ======================================
     File Upload
  ====================================== */

  onFileSelected(event: Event): void {

    const input = event.target as HTMLInputElement;

    if (!input.files || input.files.length === 0) {

      return;

    }

    const file = input.files[0];

    this.selectedFileName = file.name;

    console.log('Selected File:', file);

    // Later this will call your Spring Boot upload API.
    // Example:
    // this.dashboardService.uploadFile(file).subscribe(...);

  }

  /* ======================================
     Sample Transaction Data
  ====================================== */

  readonly reconciliationData: ReconciliationItem[] = [

    {

      date: '01-May',

      description: 'Salary Credit',

      ledger: '₹45,000',

      bank: '₹45,000',

      status: 'Matched'

    },

    {

      date: '03-May',

      description: 'Vendor Payment',

      ledger: '₹12,800',

      bank: '₹12,500',

      status: 'Mismatch'

    },

    {

      date: '05-May',

      description: 'GST Payment',

      ledger: '₹8,600',

      bank: '₹8,600',

      status: 'Matched'

    },

    {

      date: '06-May',

      description: 'Office Rent',

      ledger: '₹30,000',

      bank: '₹30,000',

      status: 'Matched'

    },

    {

      date: '08-May',

      description: 'Internet Bill',

      ledger: '₹2,350',

      bank: '₹2,350',

      status: 'Matched'

    },

    {

      date: '10-May',

      description: 'Electricity Bill',

      ledger: '₹5,250',

      bank: '₹5,400',

      status: 'Mismatch'

    }

  ];

  /* ======================================
     Filtered Data
  ====================================== */

  filteredData: ReconciliationItem[] = [

    ...this.reconciliationData

  ];

  /* ======================================
     Search Transactions
  ====================================== */

  searchTransactions(): void {

    const keyword = this.searchText.trim().toLowerCase();

    if (!keyword) {

      this.filteredData = [

        ...this.reconciliationData

      ];

      return;

    }

    this.filteredData = this.reconciliationData.filter(item =>

      item.description.toLowerCase().includes(keyword)

      ||

      item.status.toLowerCase().includes(keyword)

      ||

      item.date.toLowerCase().includes(keyword)

    );

  }
    /* ======================================
     Reset Search
  ====================================== */

  resetSearch(): void {

    this.searchText = '';

    this.filteredData = [

      ...this.reconciliationData

    ];

  }

  /* ======================================
     Refresh Dashboard
  ====================================== */

  refreshDashboard(): void {

    this.resetSearch();

    this.refreshCharts();

    console.log('Dashboard Refreshed');

  }

  /* ======================================
     Refresh Charts
  ====================================== */

  refreshCharts(): void {

    this.createReconciliationChart();

    this.createTrendChart();

  }

  /* ======================================
     Future Backend Methods
     (Spring Boot Integration)
  ====================================== */

  loadDashboardStatistics(): void {

    // Example:
    // this.dashboardService.getDashboardStats()
    //   .subscribe(response => {
    //       this.dashboardStats = response;
    //       this.refreshCharts();
    //   });

  }

  loadTransactions(): void {

    // Example:
    // this.dashboardService.getTransactions()
    //   .subscribe(response => {
    //       this.reconciliationData = response;
    //       this.filteredData = [...response];
    //   });

  }

  uploadCsvFile(file: File): void {

    // Example:
    // this.dashboardService.uploadCsv(file)
    //   .subscribe(response => {
    //       console.log(response);
    //   });

  }

  /* ======================================
     Cleanup
  ====================================== */

  ngOnDestroy(): void {

    if (this.reconciliationChart) {

      this.reconciliationChart.destroy();

    }

    if (this.trendChart) {

      this.trendChart.destroy();

    }

  }

}