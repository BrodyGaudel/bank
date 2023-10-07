import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css']
})
export class NavBarComponent implements OnInit{

  searchCustomerFormGroup!: FormGroup;

  constructor(private router: Router,
              private fb: FormBuilder) {
  }
  ngOnInit(): void {
    this.searchCustomerFormGroup = this.fb.group( {
      keyword : this.fb.control(null, [Validators.required, Validators.minLength(2), Validators.maxLength(256)])
    });
  }

  searchCustomer() : void {
    let keyword: string = this.searchCustomerFormGroup.value.keyword;
    this.router.navigate(['list-customer', keyword]).then();
  }
}
