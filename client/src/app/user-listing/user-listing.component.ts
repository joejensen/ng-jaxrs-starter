import { Component, OnInit } from '@angular/core';
import {UsersService} from "../server/api/users.service";
import {UserDto} from "../server/model/user-dto";

@Component({
  selector: 'app-user-listing',
  template: `
    <ul>
        <li *ngFor="let user of users">
            {{user.id}} - {{user.name}}
        </li>
    </ul>
  `,
  styles: [`
  `]
})
export class UserListingComponent implements OnInit {

  public users: UserDto[] = [];

  constructor( private userService : UsersService) {
  }

  ngOnInit(): void {
    this.userService.listUsers(10).subscribe(v => {
      this.users = v;
    });
  }
}
