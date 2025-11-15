import { Component, OnInit, OnDestroy } from '@angular/core';
import { User } from '../user';
import { Router } from '@angular/router';
import { ModalService } from '../modal-service';

@Component({
  selector: 'app-helper-page',
  standalone: false,
  templateUrl: './helper-page.html',
  styleUrl: './helper-page.css'
})
export class HelperPage implements OnInit, OnDestroy {
  private needsChannel?: BroadcastChannel;

  // localStorage fallback handler
  private storageHandler = (event: StorageEvent) => {
    if (event.key && event.key.startsWith('need-deleted:') && event.newValue) {
      try {
        const msg = JSON.parse(event.newValue);
        console.log('HelperPage received need deleted via storage', msg);
        this.handleNeedDeleted(msg);
      } catch (e) {
        console.error('Failed to parse need deleted payload', e);
      }
    }
  };

  constructor(
    private router: Router,
    public user: User,
    private modal: ModalService
  ) {}

  /**
   * Setup listeners on init
   */
  ngOnInit(): void {
    // Listen for localStorage events from other tabs
    window.addEventListener('storage', this.storageHandler);
    console.log('HelperPage storage listener attached');

    // Also listen via BroadcastChannel
    if ('BroadcastChannel' in window) {
      this.needsChannel = new BroadcastChannel('needs');
      this.needsChannel.onmessage = (ev: MessageEvent) => {
        if (ev.data?.type === 'need-deleted') {
          console.log('HelperPage received need-deleted via BroadcastChannel', ev.data);
          this.handleNeedDeleted(ev.data);
        }
      };
      console.log('HelperPage BroadcastChannel listener attached');
    }
  }

  /**
   * Cleanup listeners on destroy
   */
  ngOnDestroy(): void {
    window.removeEventListener('storage', this.storageHandler);
    if (this.needsChannel) {
      this.needsChannel.close();
    }
    console.log('HelperPage listeners removed');
  }

  /**
   * Navigates to checkout page if user is logged in
   */
  goToCheckout() {
    if (this.user.isLoggedIn()) {
      this.router.navigate(['/checkout']);
    } else {
      this.router.navigate(['/login']);
    }
  }

  /**
   * UI notification of delted need
   */
  private handleNeedDeleted(msg: { id: number; name: string; ts?: number }): void {
    this.modal.infoDialog(
      'Item removed',
      `The need "${msg.name}" has been removed by the administrator and is no longer be available.`,
      'OK'
    ).subscribe(() => {});
  }
}