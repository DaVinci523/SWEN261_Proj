import { Component, OnDestroy, OnInit } from '@angular/core';
import { AdminPageService } from '../admin-page-service';
import { Need } from '../need';
import { User } from '../user';
import { FundingBasketService } from '../funding-basket-service';
import { interval, Observable, Subscription } from 'rxjs';
import { DonationService } from '../donation';
import { AccountsService } from '../accounts-service';
import { Account } from '../account';
import { forkJoin, of } from 'rxjs';
import { ModalService } from '../modal-service';

@Component({
  selector: 'app-admin-page',
  standalone: false,
  templateUrl: './admin-page.html',
  styleUrl: './admin-page.css'
})

export class AdminPage implements OnInit, OnDestroy{
  private needsChannel?: BroadcastChannel;
  private usersChannel?: BroadcastChannel;

  usernameToRemove = '';
  users: Account[] = [];
  totalDonated: number = 0;
  donationGoal: number = 0;
  newGoal: number | null = null;
  needFilledColor = "#bbffacff";

  private messageHandler = (event: MessageEvent) => {
    console.log("I GOT A MESSAGE: " + event);
    if (event.data && event.data.startsWith('cupboard-updated')) {
      this.getNeeds();
      this.refresh();
      this.loadDonationStatus();
      console.log("Needs updated");
    }
    if (event.data && event.data.startsWith('user-created')) {
      this.refresh();
      console.log("User Created");
    }
  };


  // private getNeedsSubscription: Subscription;
  //private usersPollSubscription?: Subscription;
 
  constructor(
    private adminPageService:AdminPageService, // adminPageService to connect to REST API back-end      
    private fundingBasketService: FundingBasketService,
    private donationService: DonationService,
    public user: User, // current user of the application
    private accounts: AccountsService,
    private modal: ModalService
  ){
    // Continuously call getNeeds on set intervals (ex: 5000 is 5 seconds) to keep them updated when page is not reloaded.
    // this.getNeedsSubscription = interval(500).subscribe(() => {
    //   //this.getNeeds();
    //   // this.refresh();
    //   // this.loadDonationStatus();
    // })
    //ensure users list is kept fresh even if getNeeds doesnt work
    // this.usersPollSubscription = interval(2000).subscribe(() => {
    //   this.refresh();
    // });
  }  
 
  needs:Need[] = []; // front-end container for all needs in cupboard
  usernames:String[] = [];

  /**
   * Refresh the list of users in the admin page
   */
  refresh(): void {
    this.accounts.getAccounts().subscribe(list => {
      if (Array.isArray(list)) {
        this.users = list as Account[];
      } else {
        this.users = [];
      }
    });
  }

   /** 
   * populates needs once the program is initialized
   */
  ngOnInit(): void {
    // calls the getNeeds function to populate needs
    this.getNeeds();
    this.refresh();
    this.loadDonationStatus();

    window.addEventListener('message', this.messageHandler); // listener for a message

    if ('BroadcastChannel' in window) { // connect broadcast message
      console.log("BRODCAST IN CHANNEL")
      this.needsChannel = new BroadcastChannel('needs');
      this.usersChannel = new BroadcastChannel('users');
      this.needsChannel.onmessage = (ev: MessageEvent) => {
        if (ev.data === 'cupboard-updated') {
          console.log('AdminPage received cupboard-updated via BroadcastChannel', ev.data);
          this.getNeeds();
          this.refresh();
          this.loadDonationStatus();
        }
      };
      console.log('AdminPage needs BroadcastChannel listener attached');

      this.usersChannel.onmessage = (ev: MessageEvent) => {
        if (ev.data === 'user-created') {
          console.log('AdminPage received user-created via BroadcastChannel', ev.data);
          this.refresh();
        }
      };
      console.log('AdminPage user BroadcastChannel listener attached');

    }
  }

  /**
   * Remove subscription if it still exists when this component is destroyed to prevent memory leaks.
   */
  ngOnDestroy(): void{
    window.removeEventListener('message', this.messageHandler);
    if (this.needsChannel) {
      this.needsChannel.close();
    }
  }
 
  /**
   * gets all needs from back-end
   */
  getNeeds(): void {
    // calls the getNeeds function from adminPageService and subscribes to the observable
    this.adminPageService.getNeeds().subscribe(needs => this.needs = needs);
    this.fundingBasketService.getUserNames().subscribe(usernames => this.usernames = usernames); // gets all the usernames
    this.accounts.getUsernames().subscribe(names => {
      if (Array.isArray(names)) {
        this.usernames = names as unknown as String[];
      }
    });
  }

  /**
   * creates a single need
   * appends need to needs list and the back-end
   *
   * @param name - name of the need
   * @param value - value of the need
   * @param quantity - maxQuantity of the need
   * @param type - type of the need
   */
  createNeed(name: string, value: number, quantity: number, type: string): void {
    // clears white space in name input
    name = name.trim();
    // checks if name is empty
    if (value <0 || quantity < 0){
      this.modal.infoDialog('Not allowed', 'You cannot set values to negative.', 'OK')
      .subscribe(() => {
        this.getNeeds();
        this.refresh();
        this.loadDonationStatus();
        this.notifyNeedModified();
      });
    }
    
    else if (!name) {
        return;
    }
    else{
      var changedVisibilty = false; // used to check if a need had its visibility changed so the program only creates or changes visibility and not both.

      const visibilityUpdates: Observable<any>[] = []; // list of updates that occured

      // checks through every cupboard need and if it matches the newly created need's name and that need is not visible, then set the visibility of that need to true.
      this.needs.forEach(need => {
        if(need.name.toLowerCase() == name.toLowerCase() && !need.visible){ // check names ignoring case and whitespace
          changedVisibilty = true;
          // adds the need back in by tagging its visibility to true
          visibilityUpdates.push(this.adminPageService.changeNeedVisibility(need.id, true));
        }
      });

      if(changedVisibilty){ // update if a visibility was changed
        forkJoin(visibilityUpdates).subscribe(() => {
          this.getNeeds();
          this.refresh();
          this.loadDonationStatus();
          this.notifyNeedModified();
        });
      }
      else{
        // calls the createNeed function, passing the parameters as a Need object, subscribing to the observable
        this.adminPageService.createNeed({ name, value, type, currentQuantity: 0, maxQuantity: quantity, visible: true} as Need)
          .subscribe(need => {
            this.needs.push(need);
            this.getNeeds();
            this.refresh();
            this.loadDonationStatus();
            this.notifyNeedModified();
            // window.location.reload();
        });
      }
    }
  }

  /**
   * deletes a need both from the needs list and the back-end
   *
   * @param need - the need to be deleted
   */
  deleteNeed(need: Need): void {
    this.modal.confirmDialog('Delete', 'Are you sure you want to delete this need?', 'Yes', 'No')
    .subscribe(ok => {
      if (ok) {
        // update local list
        this.needs = this.needs.filter(n => n !== need);

        // notify all tabs that this need was deleted
        this.notifyNeedDeleted(need);

        this.adminPageService.changeNeedVisibility(need.id, false).subscribe(() => {
          // this.notifyNeedModified();
          // this.getNeeds();
          // this.refresh();
          // this.loadDonationStatus();
          // window.location.reload();
        });
        this.notifyNeedModified();
        this.getNeeds();
        this.refresh();
        this.loadDonationStatus();
      }
    });
  }


  /**
   *updates a need both from the needs list and the back-end
   *
   * @param need - the need to be updated
   * @param name - the updated name of the need
   * @param value - the updated value of the need
   * @param currentQuantity - the current quantity of the need
   * @param maxQuantity - the updated maxQuantity of the need
   * @param type - the updated type of the need  
   */
  updateNeed(need: Need, name: string, value: number, currentQuantity: number, maxQuantity: number, type: string): void {
   
    if(!name){ // if any of the other variables are not set, then don't change them.
      name = need.name;
    }
    if(!value){
      value = need.value;
    }
    if(!maxQuantity){
      maxQuantity = need.maxQuantity;
    }
    if(!type){
      type = need.type;
    }

    if (value <0 || maxQuantity < 0){
      this.modal.infoDialog('Not allowed', 'You cannot set values to negative.', 'OK')
      .subscribe(() => {});
      return;
    }

    // updates the needs list after changing the parameter need
    this.needs = this.needs.filter(n => n !== need);

    // calls the updateNeed function from adminPageService, subscribing to the observable
    this.adminPageService.updateNeed(need, name, value, currentQuantity, maxQuantity, type, true).subscribe(needs => {
      this.needs = this.needs.filter(n => n !== needs);
      this.getNeeds();
      this.refresh();
      this.loadDonationStatus();
      this.notifyNeedModified();
      // window.location.reload();
    });
   
  }      

  /**
   * Removes a user from the back-end accounts list
   * Does not allow removal of an admin
   * Logs out users who are currently logged in when deleted
   * @returns nothing
   */
  removeAUser(): void {
    const u = this.usernameToRemove.trim();
    if (!u) {
      this.modal.infoDialog('Error', 'Please enter a username.', 'OK')
      .subscribe(() => {});
      return;
    }

    if (u.toLowerCase() === 'admin') {
      this.modal.infoDialog('Not allowed', 'You cannot delete that account.', 'OK')
      .subscribe(() => {});
      return;
    }
    
    this.modal.confirmDialog('Delete user', 'Delete "' + u + '"?', 'Delete', 'Cancel')
    .subscribe(ok => {
      if (ok) {
        
        this.accounts.delete(u).subscribe({
          next: () => {
            // success
            try { localStorage.setItem('force-logout:' + u, String(Date.now())); } catch {}
            try {
              if ('BroadcastChannel' in window) {
                const bc = new BroadcastChannel('auth');
                bc.postMessage({ type: 'force-logout', user: u });
                bc.close();
              }
            } catch {}
            this.usernameToRemove = '';
            this.refresh();
            this.getNeeds();
          },
          error: (err) => {
            if (err && err.status === 404) {
              this.modal.infoDialog('Not found', 'That user does not exist.', 'OK').subscribe(() => {});
              return;
            }
            if (err && err.status === 403) {
              this.modal.infoDialog('Not allowed', 'You cannot delete that account.', 'OK').subscribe(() => {});
              return;
            }
            this.modal.infoDialog('Error', 'Could not delete the user. Please try again.', 'OK').subscribe(() => {});
          }
        });
      
      }
    });
    
  }

  /**
   * Gather the current totalDonated and donationGoals
   */

  loadDonationStatus(): void {
    this.donationService.getStatus().subscribe(status => {
      this.totalDonated = status.totalDonated;
      this.donationGoal = status.donationGoal;
    });
  }

  /**
   * Allow the admin to update the goal but they cannot update it below the current totalDonated
   * @returns 
   */
  updateGoal(): void {
    if (this.newGoal != null && this.newGoal > 0) {

      if (this.newGoal < this.totalDonated) {
        this.modal.infoDialog('Notice', 'New goal cannot be lower than the total donated.', 'OK').subscribe(() => {});
        this.newGoal = this.donationGoal; // reset input
        return;
      }

      this.donationService.setGoal(this.newGoal).subscribe(() => {
        this.loadDonationStatus();
        this.newGoal = null;
        this.notifyNeedModified();
      });
    }
  }

  /**
   * Notify users when a need is deleted
   * @param need The need being deleted
   */
  private notifyNeedDeleted(need: Need): void {
    const payload = {
      type: 'need-deleted',
      id: need.id,
      name: need.name
    };

    // Fallback via localStorage (works across tabs in same browser)
    try {
      localStorage.setItem(
        'need-deleted:' + need.id,
        JSON.stringify({ ts: Date.now(), ...payload })
      );
    } catch {}

    // BroadcastChannel for immediate cross-tab messaging
    try {
      if ('BroadcastChannel' in window) {
        const bc = new BroadcastChannel('needs');
        bc.postMessage(payload);
        bc.close();
      }
    } catch {}
  }

  /**
   * Notify the application when a need is modified
   */
  private notifyNeedModified(): void {
    // BroadcastChannel for immediate cross-tab messaging
    try {
      if ('BroadcastChannel' in window) {
        console.log("BROADCASTED")
        const bc = new BroadcastChannel('needs');
        bc.postMessage('cupboard-updated');
        bc.close();
      }
    } catch {}
  }

}
