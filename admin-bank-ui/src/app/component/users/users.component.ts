import {Component, OnInit} from '@angular/core';
import {UserService} from "../../services/user/user.service";
import {Router} from "@angular/router";
import {AuthService} from "../../services/security/auth/auth.service";
import {catchError, Observable, throwError} from "rxjs";
import {PageModel} from "../../dto/security/page.model";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrl: './users.component.css'
})
export class UsersComponent implements OnInit {

  page: number =0;
  size: number = 10;
  usersObservable!: Observable<PageModel>;
  searchUserFormGroup!: FormGroup;
  keyword!: string;

  constructor(private userService: UserService,
              private authService: AuthService,
              private router: Router,
              private fb: FormBuilder,) {
  }

  ngOnInit(): void {
    this.searchUserFormGroup = this.fb.group({
      keyword : this.fb.control(null, [Validators.required, Validators.minLength(1), Validators.maxLength(256)])
    });
  }

  handleSearchCustomer():void {
    this.keyword = this.searchUserFormGroup.value.keyword;
    this.usersObservable = this.userService.search(this.keyword, this.page, this.size).pipe(
      catchError( (err) => throwError(() => new Error(err.message)))
    );
  }



  gotoNextPage(hasNext: boolean, hasContent: boolean, isLast: boolean):void {
    if(hasNext && hasContent && !isLast){
      this.page = this.page + 1;
      this.handleSearchCustomer();
    }
  }

  gotoPreviousPage(hasPrevious: boolean, hasContent: boolean, isFirst: boolean):void {
    if(hasPrevious && hasContent && !isFirst){
      this.page = this.page - 1;
      this.handleSearchCustomer();
    }
  }


  getPreviousButtonStyle(isFirst: boolean): string {
    if(isFirst){
      return "page-item disabled"
    }
    return "page-item";
  }

  getNextButtonStyle(isLast: boolean):string {
    if(isLast){
      return "page-item disabled"
    }
    return "page-item";
  }

  gotoShowUserDetails(id: string):void {
    this.router.navigate(['/user-details', id]).then();
  }

}
