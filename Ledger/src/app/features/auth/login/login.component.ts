import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  loginForm: FormGroup;

  showPassword = false;

  constructor(
    private fb: FormBuilder,
    private router: Router
  ) {

    this.loginForm = this.fb.group({

      email: ['', [
        Validators.required,
        Validators.email
      ]],

      password: ['', Validators.required]

    });

  }

  togglePassword() {

    this.showPassword = !this.showPassword;

  }

  login() {

    if (this.loginForm.invalid) {

      this.loginForm.markAllAsTouched();
      return;

    }

    alert("Login Successful");

    this.router.navigate(['/app/dashboard']);

  }

}