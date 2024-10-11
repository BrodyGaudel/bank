import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {Observable} from "rxjs";
import {UserResponse} from "../../models/user.response";
import {PageResponse} from "../../../lang/page.response";
import {AuthService} from "../../service/auth/auth.service";
import {UserService} from "../../service/user/user.service";
import {Router} from "@angular/router";
import {AsyncPipe, NgForOf, NgIf} from "@angular/common";

@Component({
  selector: 'app-users',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    NgIf,
    AsyncPipe,
    NgForOf
  ],
  templateUrl: './users.component.html',
  styleUrl: './users.component.css'
})
export class UsersComponent implements OnInit {

  searchFormGroup!: FormGroup;
  usersObservable!: Observable<PageResponse<UserResponse>>;
  page: number = 0;
  size: number = 9;
  keyword: string = "";

  constructor(private readonly authService: AuthService,
              private readonly userService: UserService,
              private readonly fb: FormBuilder,
              private readonly router: Router) {
  }

  ngOnInit(): void {
    this.authService.security();
    this.searchFormGroup = this.fb.group({
      keyword: this.fb.control('', [Validators.minLength(1), Validators.maxLength(256)]),
    });

    //Load the list of default users
    this.usersObservable = this.searchUser(this.keyword, this.page, this.size);

    //Listen to changes in the form to launch the search automatically (optional)
    this.searchFormGroup.get('keyword')?.valueChanges.subscribe(value => {
      this.keyword = value;
      this.page = 0; //Reset to first page when searching
      this.usersObservable = this.searchUser(this.keyword, this.page, this.size);
    });
  }

  onSearch(): void {
    this.keyword = this.searchFormGroup.value.keyword;
    this.page = 0; //Reset to first page when searching
    this.usersObservable = this.searchUser(this.keyword, this.page, this.size);
  }

  onPageChange(page: number): void {
    this.page = page;
    this.usersObservable = this.searchUser(this.keyword, this.page, this.size);
  }

  searchUser(keyword: string, p: number, s: number): Observable<PageResponse<UserResponse>> {
    return this.userService.search(keyword, p, s).pipe();
  }

  gotoShowUserComponent(id: string) :void {
    this.router.navigate(["/user", id]).then();
  }


  gotoPreviousPage(hasPrevious: boolean, isFirst: boolean, previousPage: number) :void {
    if(hasPrevious && !isFirst) {
      this.onPageChange(previousPage);
    }
  }

  gotoNextPage(hasNext: boolean, isLast: boolean, nextPage: number) :void {
    if(hasNext && !isLast){
      this.onPageChange(nextPage);
    }
  }
}
