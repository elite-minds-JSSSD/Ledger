import { Component } from '@angular/core';

import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})

export class RegisterComponent {

  registerForm: FormGroup;
showPassword = false;
showConfirmPassword = false;
togglePassword() {
  this.showPassword = !this.showPassword;
}

toggleConfirmPassword() {
  this.showConfirmPassword = !this.showConfirmPassword;
}
  constructor(
    private fb: FormBuilder,

    private router: Router
  ) {

    this.registerForm = this.fb.group({

      fullName: ['', Validators.required],

      email: ['', [
        Validators.required,
        Validators.email
      ]],

      company: ['', Validators.required],

      password: ['', [
        Validators.required,
        Validators.minLength(6)
      ]],

      confirmPassword: ['', Validators.required]

    });

  }

  register() {

    if (this.registerForm.invalid) {

      this.registerForm.markAllAsTouched();
      return;

    }

    if (
      this.registerForm.value.password !==
      this.registerForm.value.confirmPassword
    ) {

      alert('Passwords do not match');
      return;

    }

    console.log(this.registerForm.value);

    alert('Registration Successful');

    this.router.navigate(['/login']);

  }

}