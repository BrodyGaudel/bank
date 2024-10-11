import {Component, OnInit} from '@angular/core';
import {AsyncPipe, NgForOf, NgIf} from "@angular/common";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {Observable} from "rxjs";
import {PageResponse} from "../../../lang/page.response";
import {RoleResponse} from "../../models/role.response";
import {RoleService} from "../../service/role/role.service";
import {Router} from "@angular/router";
import {AuthService} from "../../service/auth/auth.service";

@Component({
  selector: 'app-roles',
  standalone: true,
    imports: [
        AsyncPipe,
        NgForOf,
        NgIf,
        ReactiveFormsModule
    ],
  templateUrl: './roles.component.html',
  styleUrl: './roles.component.css'
})
export class RolesComponent implements OnInit{

  searchFormGroup!: FormGroup;
  rolesObservable!: Observable<PageResponse<RoleResponse>>;
  page: number = 0;
  size: number = 9;
  keyword: string = "";

  constructor(private readonly roleService: RoleService,
              private readonly fb: FormBuilder,
              private readonly router: Router,
              private readonly authService: AuthService) {
  }

  ngOnInit(): void {
    this.authService.security();
    this.initSearchFormGroup();
    this.rolesObservable = this.searchRole(this.keyword, this.page, this.size);
    this.searchFormGroup.get('keyword')?.valueChanges.subscribe(value => {
      this.keyword = value;
      this.page = 0; //Reset to first page when searching
      this.rolesObservable = this.searchRole(this.keyword, this.page, this.size);
    });
  }

  initSearchFormGroup(): void {
    this.searchFormGroup = this.fb.group({
      keyword: this.fb.control('', [Validators.minLength(1), Validators.maxLength(256)]),
    });
  }

  onSearch(): void {
    this.keyword = this.searchFormGroup.value.keyword;
    this.page = 0; //Reset to first page when searching
    this.rolesObservable = this.searchRole(this.keyword, this.page, this.size);
  }

  onPageChange(page: number): void {
    this.page = page;
    this.rolesObservable = this.searchRole(this.keyword, this.page, this.size);
  }

  searchRole(keyword: string, p: number, s: number): Observable<PageResponse<RoleResponse>> {
    return this.roleService.searchRoles(keyword, p, s).pipe();
  }

  gotoShowRoleComponent(id: number) :void {
    this.router.navigate(["/role", id]).then();
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
