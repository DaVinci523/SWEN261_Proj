import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, Subject } from 'rxjs';

export interface ModalConfig {
  title?: string;
  message?: string;
  confirmText?: string;
  cancelText?: string;
  showCancel?: boolean;
  width?: string;
}

export interface ModalState {
  open: boolean;
  config: ModalConfig;
}

@Injectable({ providedIn: 'root' })
export class ModalService {
  private stateSubject = new BehaviorSubject<ModalState | null>(null);
  state$ = this.stateSubject.asObservable();

  private response?: Subject<boolean>;

  open(config: ModalConfig): Observable<boolean> {
    this.response = new Subject<boolean>();
    const st: ModalState = { open: true, config: config };
    this.stateSubject.next(st);
    return this.response.asObservable();
  }

  confirm(): void {
    if (this.response) {
      this.response.next(true);
      this.response.complete();
    }
    this.stateSubject.next(null);
  }

  cancel(): void {
    if (this.response) {
      this.response.next(false);
      this.response.complete();
    }
    this.stateSubject.next(null);
  }

  // simple confirmations
  confirmDialog(title: string, message: string, confirmText?: string, cancelText?: string): Observable<boolean> {
    const cfg: ModalConfig = {};
    cfg.title = title;
    cfg.message = message;
    if (confirmText) {
      cfg.confirmText = confirmText;
    }
    if (cancelText) {
      cfg.cancelText = cancelText;
    }
    cfg.showCancel = true;
    return this.open(cfg);
  }

  // info only popup (OK only)
  infoDialog(title: string, message: string, okText?: string): Observable<boolean> {
    const cfg: ModalConfig = {};
    cfg.title = title;
    cfg.message = message;
    if (okText) {
      cfg.confirmText = okText;
    }
    cfg.showCancel = false;
    return this.open(cfg);
  }
}
