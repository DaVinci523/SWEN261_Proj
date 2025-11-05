import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '../user';

@Component({
  selector: 'app-login-page',
  standalone: false,
  templateUrl: './login-page.html',
  styleUrls: ['./login-page.css']   // note: styleUrls (plural)
})
export class LoginPage {
  constructor(private router: Router, public user: User) {}

  username = '';
  password = '';
  
  /**
   * When the user clicks the login button, the username entered will store
   * into the user service and direct the user to their corresponding page
   */
  onLogin(): void {
    if (!this.username) {
      return;
    }
    if (!this.password) {
      return;
    }

    const proceed = window.confirm('Log in as "' + this.username + '"?');
    if (!proceed) {
      return;
    }

    const ok = this.user.login(this.username, this.password);

    if (ok) {
      alert('Welcome, ' + this.username + '!');
      const role = this.user.getCurrentUserRole();
      if (role === 'admin') {
        this.router.navigate(['/admin']);
      } else {
        this.router.navigate(['/helper']);
      }
    } else {
      alert('Invalid username or password, or the account does not exist.');
    }
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

    if (this.user.userExists(this.username)) {
      alert('That username already exists. Please choose another.');
      return;
    }

    const proceed = window.confirm('Create a new account for "' + this.username + '" and save this password?');
    if (!proceed) {
      return;
    }

    const created = this.user.createUser(this.username, this.password);
    if (created) {
      alert('Account created! You can now log in.');
    } else {
      alert('Account could not be created.');
    }
  }
}