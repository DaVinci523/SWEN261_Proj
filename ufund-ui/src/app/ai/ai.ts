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
    
    response: string = "response goes here"; //container for API-generated response

    getResponse(prompt:string ): void {

      this.aiService.createResponse(prompt).subscribe((response: APIResponse) => {

      this.response = response.response;

      console.log(response.response);
        
      }
    );
    

    } 

}
