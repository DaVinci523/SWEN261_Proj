import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
//Using service to acces the login pages username for funding basket
export class Username {
    private userName: string = '';
    
    setUserName(name:string):void {
      this.userName = name;
    }

    getUsername(): String{
        return this.userName;
    }

}
