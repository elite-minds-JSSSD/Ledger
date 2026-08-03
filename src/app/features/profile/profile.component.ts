import { Component } from '@angular/core';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})

export class ProfileComponent {

  stats = [
    {
      title: 'Reports Generated',
      value: 56,
      subtitle: 'This Month',
      icon: 'bi-file-earmark-bar-graph',
      color: 'primary'
    },
    {
      title: 'CSV Uploads',
      value: 18,
      subtitle: 'This Month',
      icon: 'bi-cloud-upload',
      color: 'success'
    },
    {
      title: 'Last Login',
      value: 'Today, 10:30 AM',
      subtitle: '16 Jul 2025',
      icon: 'bi-calendar-event',
      color: 'warning'
    },
    {
      title: 'Account Status',
      value: 'Active',
      subtitle: 'Since Jan 2024',
      icon: 'bi-shield-check',
      color: 'purple'
    }
  ];

}