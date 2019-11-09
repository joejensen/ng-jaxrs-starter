import {Component, OnInit} from '@angular/core';
import {UsersService} from "./server/api/users.service";

@Component({
  selector: 'app-root',
  template: `    
      <router-outlet></router-outlet>
  `,
  styles: [`
  `]
})
export class AppComponent{
}
