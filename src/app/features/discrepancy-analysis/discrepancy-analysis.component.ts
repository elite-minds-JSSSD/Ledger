import { AfterViewInit, Component } from '@angular/core';
import {
  Chart,
  DoughnutController,
  ArcElement,
  Tooltip,
  Legend
} from 'chart.js';

Chart.register(
  DoughnutController,
  ArcElement,
  Tooltip,
  Legend
);
@Component({
  selector: 'app-discrepancy-analysis',
  templateUrl: './discrepancy-analysis.component.html',
  styleUrls: ['./discrepancy-analysis.component.css']
})
export class DiscrepancyAnalysisComponent implements AfterViewInit {

  ngAfterViewInit(): void {

    this.createTypeChart();

    this.createPriorityChart();

  }
  private createTypeChart(): void {

  new Chart('typeChart', {

    type: 'doughnut',

    data: {

      labels: [

        'Amount Mismatch',

        'Missing in Ledger',

        'Missing in Bank',

        'Date Mismatch',

        'Duplicate Entry'

      ],

      datasets: [

        {

          data: [586,301,196,112,91],

          backgroundColor: [

            '#ef4444',

            '#f59e0b',

            '#22c55e',

            '#8b5cf6',

            '#2563eb'

          ],

          borderWidth:0,

          hoverOffset:8

        }

      ]

    },

    options: {

      responsive:true,

      maintainAspectRatio:false,

      cutout:'65%',

      plugins:{

        legend:{

          display:false

        }

      }

    }

  });

}
private createPriorityChart(): void {

  new Chart('priorityChart', {

    type:'doughnut',

    data:{

      labels:[

        'High',

        'Medium',

        'Low'

      ],

      datasets:[

        {

          data:[256,678,352],

          backgroundColor:[

            '#ef4444',

            '#f59e0b',

            '#22c55e'

          ],

          borderWidth:0,

          hoverOffset:8

        }

      ]

    },

    options:{

      responsive:true,

      maintainAspectRatio:false,

      cutout:'65%',

      plugins:{

        legend:{

          display:false

        }

      }

    }

  });

}

}