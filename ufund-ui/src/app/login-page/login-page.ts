import { Component } from '@angular/core';
import { User } from '../user';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login-page',
  standalone: false,
  templateUrl: './login-page.html',
  styleUrl: './login-page.css'
})
export class LoginPage {
  constructor(private router: Router, private user: User) {}

  username = '';

  onLogin(name: string){
     if (this.user.userExists(this.username)){
      this.user.login(name);
      alert("Welcome " + this.username);
      if (this.user.getCurrentUserRole() == 'admin') {
        this.router.navigate(['/admin']);
      } else {
        this.router.navigate(['./']);
      }
     } else { 
      alert("User does not exist!");
     }
  }
}
