import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '../user';
import { AccountsService } from '../accounts-service';
import { ModalService } from '../modal-service';

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
    private accounts: AccountsService,
    public modal: ModalService
  ) {}

  username = '';
  password = '';

  onLogin(skipConfirm: boolean = false): void {
    if (!this.username || !this.password) {
      return;
    }

    if (!skipConfirm) {
        this.modal.confirmDialog('Confirm login', 'Log in as "' + this.username + '"?', 'Yes', 'No')
        .subscribe(go => {
        if (go) {
            this.accounts.validate(this.username, this.password).subscribe(ok => {
              if (!ok) {
                this.modal.infoDialog('Login failed', 'Invalid username or password.', 'OK').subscribe(() => {});
                return;
              }

              this.user.setSession(this.username);
              const role = this.user.getCurrentUserRole();
              if (role == 'admin') {
                this.router.navigate(['/admin']);
              } else {
                this.router.navigate(['/helper']);
              }
            });
        }
      });
    }
  }

  onCreateUser(): void {
    this.accounts.exists(this.username).subscribe(exists => {
      if (exists) {
        this.modal.infoDialog('Account Creation Error', 'This username already exists.', 'OK').subscribe(() => {});
        return;
      }

      this.modal.confirmDialog('Account Creation', 'Create an account named "' + this.username + '"?', 'Yes', 'No')
      .subscribe(go => {
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
          this.modal.infoDialog('Account Creation', 'Account created, logging you in now...', 'OK')
          .subscribe(go => {
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
      });
    });
  }
}