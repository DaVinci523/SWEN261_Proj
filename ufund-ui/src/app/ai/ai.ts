import { Component } from '@angular/core';
import { AiService } from '../ai-service';
import { APIResponse } from '../response';

@Component({
  selector: 'app-ai',
  standalone: false,
  templateUrl: './ai.html',
  styleUrl: './ai.css'
})

export class Ai {
  
    
    constructor(
      private aiService:AiService
    ){}
    
    //container for API-generated response
    response: string = ""; 

    isVisible = "hidden";
    isLoading: boolean = false;


    createResponse(prompt:string ): void {

      this.aiService.createResponse(prompt).subscribe((response: APIResponse) => {

      this.response = response.response;

      console.log(response.response);
        
      }
    );

    } 
    
  toggleVisibility(){ // toggle the ai module's visibility
    if(this.isVisible != "hidden"){
      this.isVisible = "hidden";
    }
    else{
      this.isVisible = "shown";
    }
  };


  triggerCursorChange() { // change the cursor to a loading and revert back after 2 seconds
    this.isLoading = true;
    setTimeout(() => {
      this.isLoading = false;
    }, 1000);
  }

}
