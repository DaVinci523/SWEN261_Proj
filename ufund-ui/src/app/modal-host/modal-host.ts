import { Component, HostListener } from '@angular/core';
import { ModalService, ModalConfig, ModalState } from '../modal-service';

@Component({
  selector: 'app-modal-host',
  standalone: false,
  templateUrl: './modal-host.html',
  styleUrls: ['./modal-host.css']
})
export class ModalHost {
  visible = false;

  title = 'Notice';
  message = '';
  confirmText = 'OK';
  cancelText = 'Cancel';
  showCancel = true;
  width = '420px';

  constructor(public modal: ModalService) {
    this.modal.state$.subscribe((st: ModalState | null) => {
      if (st && st.open) {
        this.visible = true;
        this.applyConfig(st.config);
      } else {
        this.visible = false;
      }
    });
  }

  private applyConfig(cfg: ModalConfig): void {
    if (cfg.title) {
      this.title = cfg.title;
    } else {
      this.title = 'Notice';
    }

    if (cfg.message) {
      this.message = cfg.message;
    } else {
      this.message = '';
    }

    if (cfg.confirmText) {
      this.confirmText = cfg.confirmText;
    } else {
      this.confirmText = 'OK';
    }

    if (cfg.cancelText) {
      this.cancelText = cfg.cancelText;
    } else {
      this.cancelText = 'Cancel';
    }

    if (cfg.showCancel === false) {
      this.showCancel = false;
    } else {
      this.showCancel = true;
    }

    if (cfg.width) {
      this.width = cfg.width;
    } else {
      this.width = '420px';
    }
  }

  onOverlayClick(e: MouseEvent): void {
    const target = e.target as HTMLElement;
    if (target && target.classList.contains('modal-overlay')) {
      this.modal.cancel();
    }
  }

  @HostListener('document:keydown', ['$event'])
  onKeydown(event: KeyboardEvent): void {
    if (!this.visible) {
      return; // ignore keys when modal is closed
    }

    if (event.key === 'Enter') {
      event.preventDefault();
      this.modal.confirm(); // acts like clicking OK/Yes
    }

    //allow Esc to cancel when there's a Cancel button
    if (event.key === 'Escape' && this.showCancel) {
      event.preventDefault();
      this.modal.cancel();
    }
  }

}
