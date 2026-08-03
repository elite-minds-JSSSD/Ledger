import { Component, AfterViewInit } from '@angular/core';
import {
  Chart,
  DoughnutController,
  ArcElement,
  LineController,
  LineElement,
  PointElement,
  LinearScale,
  CategoryScale,
  Tooltip,
  Legend,
  Filler,
  Title
} from 'chart.js';

Chart.register(
  DoughnutController,
  ArcElement,
  LineController,
  LineElement,
  PointElement,
  LinearScale,
  CategoryScale,
  Tooltip,
  Legend,
  Filler,
  Title
);

@Component({
  selector: 'app-reconciliation-results',
  templateUrl: './reconciliation-results.component.html',
  styleUrls: ['./reconciliation-results.component.css']
})
export class ReconciliationResultsComponent implements AfterViewInit {

  summaryChart!: Chart;
  trendChart!: Chart;
  gaugeChart!: Chart;

ngAfterViewInit(): void {
  this.createSummaryChart();
  this.createTrendChart();
  this.createGaugeChart();


  // We'll add this in Part 3
  // this.createGaugeChart();
}

  createSummaryChart(): void {

    this.summaryChart = new Chart("summaryChart", {

      type: 'doughnut',

      data: {

        labels: [
          'Matched',
          'Unmatched',
          'Mismatched',
          'Duplicates'
        ],

        datasets: [
          {
            data: [8742, 3214, 1286, 184],

            backgroundColor: [
              '#22c55e',
              '#f59e0b',
              '#ef4444',
              '#7c3aed'
            ],

            borderColor: '#ffffff',
            borderWidth: 2,
            hoverOffset: 10
          }
        ]

      },

      options: {

        responsive: true,

        maintainAspectRatio: false,

        plugins: {

          legend: {

            position: 'bottom',

            labels: {

              boxWidth: 14,

              padding: 20,

              font: {

                size: 12

              }

            }

          }

        }

      }

    });

  }
createTrendChart(): void {

  this.trendChart = new Chart("trendChart", {

    type: 'line',

    data: {

      labels: [
        '10 Jul',
        '11 Jul',
        '12 Jul',
        '13 Jul',
        '14 Jul',
        '15 Jul',
        '16 Jul'
      ],

      datasets: [

        {

          label: 'Matched',

          data: [1200, 1500, 1700, 1600, 1800, 2100, 2300],

          borderColor: '#16a34a',

          backgroundColor: 'rgba(22,163,74,0.15)',

          fill: true,

          tension: 0.4,

          pointRadius: 5,

          pointHoverRadius: 7

        },

        {

          label: 'Unmatched',

          data: [500, 450, 430, 410, 380, 340, 300],

          borderColor: '#ef4444',

          backgroundColor: 'rgba(239,68,68,0.08)',

          fill: true,

          tension: 0.4,

          pointRadius: 5,

          pointHoverRadius: 7

        }

      ]

    },

    options: {

      responsive: true,

      maintainAspectRatio: false,

      interaction: {

        mode: 'index',

        intersect: false

      },

      plugins: {

        legend: {

          position: 'top'

        }

      },

      scales: {

        y: {

          beginAtZero: true,

          grid: {

            color: '#eeeeee'

          }

        },

        x: {

          grid: {

            display: false

          }

        }

      }

    }

  });

}
createGaugeChart(): void {

  if (this.gaugeChart) {
    this.gaugeChart.destroy();
  }

  this.gaugeChart = new Chart<'doughnut'>('gaugeChart', {

    type: 'doughnut',

    data: {
      labels: ['Matched', 'Remaining'],
      datasets: [
        {
          data: [65.24, 34.76],
          backgroundColor: ['#16a34a', '#e5e7eb'],
          borderWidth: 0
        }
      ]
    },

    options: {
      responsive: true,
      maintainAspectRatio: false,

      cutout: '75%',

      rotation: 270,

      circumference: 180,

      plugins: {
        legend: {
          display: false
        },
        tooltip: {
          enabled: false
        }
      }
    }

  });

}
}
