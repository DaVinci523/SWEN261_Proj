import { Component } from '@angular/core';
import { AdminPageService } from '../admin-page-service'; 
import { Need } from '../need';
import { User } from '../user';
import { FundingBasketService } from '../funding-basket-service';

@Component({
  selector: 'app-admin-page',
  standalone: false,
  templateUrl: './admin-page.html',
  styleUrl: './admin-page.css'
})


export class AdminPage {

  constructor(
    private adminPageService:AdminPageService, // adminPageService to connect to REST API back-end      
    private fundingBasketService: FundingBasketService,
    public user: User // current user of the application
  ){}  

  needs:Need[] = []; // front-end container for all needs in cupboard
  usernames:String[] = [];

  /**
   * gets all needs from back-end
   */
  getNeeds(): void {

    // calls the getNeeds function from adminPageService and subscribes to the observable
    this.adminPageService.getNeeds().subscribe(needs => this.needs = needs);
    this.fundingBasketService.getUserNames().subscribe(usernames => this.usernames = usernames);
  }


  /**
   * creates a single need
   * appends need to needs list and the back-end
   *
   * @param name - name of the need
   * @param value - value of the need
   * @param quantity - quantity of the need
   * @param type - type of the need
   */
  createNeed(name: string, value: number, quantity: number, type: string): void {

    // clears white space in name input
    name = name.trim();

    // checks if name is empty
    if (!name) { 
      return; 
    }

    // calls the createNeed function, passing the parameters as a Need object, subscribing to the observable
    this.adminPageService.createNeed({ name, value, type, currentQuantity: 0, maxQuantity: quantity, visible: true} as Need)
      .subscribe(need => {
        this.needs.push(need);
        window.location.reload();;
      });
    }
  

  /** 
   * deletes a need both from the needs list and the back-end
   * 
   * @param need - the need to be deleted
   */
  deleteNeed(need: Need): void { 

    // updates the needs list once the need has been deleted
    this.needs = this.needs.filter(n => n !== need);

    // calls the deleteNeedID function from adminPageService, accessing the need's id and subscribing to the observable
    // this.adminPageService.deleteNeedID(need.id).subscribe( needs => 
    //   {this.needs = this.needs.filter(n => n !== needs);
    //      window.location.reload();
    //   });

    // removes the need by tagging its visibility to false
    this.adminPageService.changeNeedVisibility(need.id, false).subscribe();

    this.usernames.forEach(username => {
      console.log("WORKING");
      
      this.fundingBasketService.getNeeds(username).subscribe(basketNeeds => {
        basketNeeds.forEach((basketNeed:Need) => {
          console.log(this.fundingBasketService.getNeeds(username));
          console.log("LOOPING");
          console.log(need);
          console.log(basketNeed);
          if(need.id == basketNeed.id){
            console.log("CHANGINGNEED");
            this.fundingBasketService.changeNeedVisibility(username, basketNeed.id, false).subscribe();
          }
        })
      });
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
    
    // updates the needs list after changing the parameter need
    this.needs = this.needs.filter(n => n !== need);

    // calls the updateNeed function from adminPageService, subscribing to the observable
    this.adminPageService.updateNeed(need, name, value, currentQuantity, maxQuantity, type, true).subscribe(needs => 
      {this.needs = this.needs.filter(n => n !== needs);
      window.location.reload();});
  }


  /*
   * populates needs once the program is initialized
   */
  ngOnInit(): void {
    
    // calls the getNeeds function to populate needs
    this.getNeeds();
  }

  

  

}
