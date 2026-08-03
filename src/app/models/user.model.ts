export interface User {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  role: 'ADMIN' | 'ACCOUNTANT' | 'AUDITOR' | 'FINANCE_MANAGER';
  phone?: string;
  profileImage?: string;
  isActive: boolean;
  createdAt: Date;
}