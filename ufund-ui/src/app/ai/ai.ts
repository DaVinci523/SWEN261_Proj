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

    // creates a response from the GeminiAPI from a prompt
    createResponse(prompt:string ): void {

      this.aiService.createResponse(prompt).subscribe((response: APIResponse) => {
          this.response = response.response;        
        }
      );
    } 

}
