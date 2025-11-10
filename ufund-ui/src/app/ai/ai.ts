import { Component } from '@angular/core';
import { AiService } from '../ai-service';

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
    
    response:String = "response goes here"; //container for API-generated response

    getResponse(prompt:String ): void {

      this.aiService.getResponse(prompt).subscribe((response: String) => this.response = response);

    } 

}
