import { Component, OnInit, OnDestroy } from '@angular/core';
import { User } from '../user';
import { ModalService } from '../modal-service';

@Component({
  selector: 'app-checkout-page',
  standalone: false,
  templateUrl: './checkout-page.html',
  styleUrl: './checkout-page.css'
})
export class CheckoutPage implements OnInit, OnDestroy {
  private needsChannel?: BroadcastChannel;

  // localStorage fallback handler
  private storageHandler = (event: StorageEvent) => {
    if (event.key && event.key.startsWith('need-deleted:') && event.newValue) {
      try {
        const msg = JSON.parse(event.newValue);
        console.log('CheckoutPage received need deleted via storage', msg);
        this.handleNeedDeleted(msg);
      } catch (e) {
        console.error('CheckoutPage failed to parse need deleted payload', e);
      }
    }
  };

  constructor(
    public user: User,
    private modal: ModalService
  ) {}

  /**
   * Setup listeners on init
   */
  ngOnInit(): void {
    window.addEventListener('storage', this.storageHandler);
    console.log('CheckoutPage storage listener attached');

    // Also listen via bc
    if ('BroadcastChannel' in window) {
      this.needsChannel = new BroadcastChannel('needs');
      this.needsChannel.onmessage = (ev: MessageEvent) => {
        if (ev.data?.type === 'need-deleted') {
          console.log('CheckoutPage received need-deleted via BroadcastChannel', ev.data);
          this.handleNeedDeleted(ev.data);
        }
      };
      console.log('CheckoutPage BroadcastChannel listener attached');
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
    console.log('CheckoutPage listeners removed');
  }

  /**
   * Handle a need being deleted notification
   * @param msg the need being deleted
   */
  private handleNeedDeleted(msg: { id: number; name: string; ts?: number; type?: string }): void {
    this.modal.infoDialog(
      'Item removed',
      `The need "${msg.name}" has been removed by the administrator and is no longer available.`,
      'OK'
    ).subscribe(() => {});
  }
}
