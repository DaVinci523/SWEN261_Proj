import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '../user';
import { AccountsService } from '../accounts-service';

@Component({
  selector: 'app-login-page',
  standalone: false,
  templateUrl: './login-page.html',
  styleUrls: ['./login-page.css']
})
export class LoginPage {
  constructor(
    private router: Router,
    public user: User,
    private accounts: AccountsService
  ) {}

  username = '';
  password = '';

  onLogin(skipConfirm: boolean = false): void {
    if (!this.username || !this.password) {
      return;
    }

    if (!skipConfirm) {
      const proceed = window.confirm('Log in as "' + this.username + '"?');
      if (!proceed) {
        return;
      }
    }

    this.accounts.validate(this.username, this.password).subscribe(ok => {
      if (!ok) {
        alert('Invalid username or password, or the account does not exist.');
        return;
      }

      this.user.setSession(this.username);
      const role = this.user.getCurrentUserRole();
      if (role == 'admin') {
        this.router.navigate(['/admin']);
      } else {
        this.router.navigate(['/helper']);// or your desired default
      }
    });
  }

  onCreateUser(): void {
    if (!this.username) {
      alert('Please enter a username to create an account.');
      return;
    }
    if (!this.password) { 
      alert('Please enter a password to create an account.');
      return;
    }

    this.accounts.exists(this.username).subscribe(exists => {
      if (exists) {
        alert('That username already exists. Please choose another.');
        return;
      }

      const proceed = window.confirm(`Create a new account for "${this.username}" and save this password?`);
      if (!proceed) {
        return;
      }

      let role: 'admin' | 'helper';
      const lower = this.username.toLowerCase();
      if (lower === 'admin') {
        role = 'admin';
      } else {
        role = 'helper';
      }

      this.accounts.create({ username: this.username, password: this.password, role }).subscribe(created => {
        if (!created) {
          alert('Account could not be created.');
          return;
        }
        alert('Account created! Logging you in now.');
        // set session locally; backend already created the account (and funding basket via your server-side hook)
        this.user.setSession(this.username);

        const r = this.user.getCurrentUserRole();
        if (r === 'admin') {
          this.router.navigate(['/admin']);
        } else {
          this.router.navigate(['/helper']);
        }
      });
    });
  }
}