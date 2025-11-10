
import { Component } from '@angular/core';
import { AdminPageService } from '../admin-page-service';
import { Need } from '../need';
import { User } from '../user';
import { FundingBasketService } from '../funding-basket-service';
import { interval, Subscription } from 'rxjs';
import { DonationService } from '../donation';
import { AccountsService } from '../accounts-service';
import { Account } from '../account';
import { forkJoin, of } from 'rxjs';

@Component({
  selector: 'app-admin-page',
  standalone: false,
  templateUrl: './admin-page.html',
  styleUrl: './admin-page.css'
})

export class AdminPage {
  usernameToRemove = '';
  users: Account[] = [];
  totalDonated: number = 0;
  donationGoal: number = 0;
  newGoal: number | null = null;


  private getNeedsSubscription: Subscription;
  private usersPollSubscription?: Subscription;
 
  constructor(
    private adminPageService:AdminPageService, // adminPageService to connect to REST API back-end      
    private fundingBasketService: FundingBasketService,
    public user: User, // current user of the application,
    private donationService: DonationService,
    private accounts: AccountsService
  ){
    // Continuously call getNeeds on set intervals (ex: 5000 is 5 seconds) to keep them updated when page is not reloaded.
    this.getNeedsSubscription = interval(500).subscribe(() => {
      this.getNeeds();
      this.refresh();
    })
    //ensure users list is kept fresh even if getNeeds doesnt work
    this.usersPollSubscription = interval(2000).subscribe(() => {
      this.refresh();
    });
  }  
 
  needs:Need[] = []; // front-end container for all needs in cupboard
  usernames:String[] = [];

  refresh(): void {
    this.accounts.getAccounts().subscribe(list => {
      if (Array.isArray(list)) {
        this.users = list as Account[];
      } else {
        this.users = [];
      }
    });
  }

   /*
   * populates needs once the program is initialized
   */
  ngOnInit(): void {
    // calls the getNeeds function to populate needs
    this.getNeeds();
    this.refresh();
    this.loadDonationStatus();
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
    if (!name) {
      return;
    }
    else{
      var changedVisibilty = false; // used to check if a need had its visibility changed so the program only creates or changes visibility and not both.
      // checks through every cupboard need and if it matches the newly created need's name and that need is not visible, then set the visibility of that need to true.
      this.needs.forEach(need => {
        if(need.name.toLowerCase() == name.toLowerCase() && !need.visible){ // check names ignoring case and whitespace
        
          // adds the need back in by tagging its visibility to true
          this.adminPageService.changeNeedVisibility(need.id, true).subscribe();
          changedVisibilty = true;
          window.location.reload();
        }
      });

      if(!changedVisibilty){
        // calls the createNeed function, passing the parameters as a Need object, subscribing to the observable
        this.adminPageService.createNeed({ name, value, type, currentQuantity: 0, maxQuantity: quantity, visible: true} as Need)
          .subscribe(need => {
            this.needs.push(need);
            window.location.reload();
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
    // updates the needs list once the need has been deleted
    this.needs = this.needs.filter(n => n !== need);
    window.location.reload();

    // removes the need by tagging its visibility to false
    this.adminPageService.changeNeedVisibility(need.id, false).subscribe();
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

    // updates the needs list after changing the parameter need
    this.needs = this.needs.filter(n => n !== need);

    // calls the updateNeed function from adminPageService, subscribing to the observable
    this.adminPageService.updateNeed(need, name, value, currentQuantity, maxQuantity, type, true).subscribe(needs =>
      {this.needs = this.needs.filter(n => n !== needs);
      window.location.reload();
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
      alert('Enter a username to remove.');
      return;
    }

    if (u.toLowerCase() === 'admin') {
      alert('The "admin" account cannot be deleted.');
      return;
    }
    
    const ok = window.confirm('Delete user "' + u + '"?');
    if (!ok) { return; }

    this.accounts.delete(u).subscribe(done => {
      if (done !== undefined) {
        // Broadcast via localStorage which fires storage event in other tabs
        try { localStorage.setItem('force-logout:' + u, String(Date.now())); } catch (_e) {}
        // Broadcast via BroadcastChannel
        try {
          if ('BroadcastChannel' in window) {
            const bc = new BroadcastChannel('auth');
            bc.postMessage({ type: 'force-logout', user: u });
            bc.close();
          }
        } catch (_e) {}
        localStorage.setItem('force-logout:' + u, String(Date.now()));
        alert('Deleted "' + u + '"');
        this.usernameToRemove = '';
        this.refresh();
        this.getNeeds();
      } else {
        alert('User not found.');
      }
    });
  }

  clearAll(): void {
    const ok = window.confirm('Delete ALL users? This cannot be undone.');
    if (!ok) { return; }
    //this.user.clearAllUsers();
    alert('All users deleted.');
    this.refresh();
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
   * Allow the admin to update the goal but he cannot update it below the current totalDonated
   * @returns 
   */
  updateGoal(): void {
    if (this.newGoal != null && this.newGoal > 0) {

      if (this.newGoal < this.totalDonated) {
        alert(`Goal cannot be lower than the total donated ($${this.totalDonated}).`);
        this.newGoal = this.donationGoal; // reset input
        return;
      }

      this.donationService.setGoal(this.newGoal).subscribe(() => {
        this.loadDonationStatus();
        this.newGoal = null;
      });
    }
  }
}
