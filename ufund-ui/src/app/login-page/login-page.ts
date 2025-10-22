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
  constructor(private router: Router, private user: User) {}

  username = '';
  password = '';
  
  /**
   * When the user clicks the login button, the username entered will store
   * into the user service and direct the user to their corresponding page
   */
  onLogin(): void {
    if (!this.username || !this.password) {
      return;
    }

    const ok = this.user.login(this.username, this.password);

    if (ok) {
      alert('Welcome ' + this.username);

      const role = this.user.getCurrentUserRole();

      if (role === 'admin') {
        this.router.navigate(['/admin']);
      } else {
        this.router.navigate(['/helper']);
      }
    } else {
      alert('Invalid username or password.');
    }
  }
}
