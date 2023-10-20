import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {Router} from "@angular/router";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  title = 'web-ui';

  searchFormGroup!: FormGroup;
  keyword!: string;

  constructor(private fb: FormBuilder,
              private router: Router) {
  }

  ngOnInit(): void {
    this.searchFormGroup=this.fb.group({
      keyword : this.fb.control('')
    });
  }

  searchCustomer(): void {
    this.keyword = this.searchFormGroup.value.keyword;
    this.searchFormGroup.reset();
    this.router.navigate(["list-customers", this.keyword]).then();
  }
}
