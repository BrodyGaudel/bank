export class UserResponse {
  id!: string;
  firstname!: string;
  lastname!: string;
  username!: string;
  email!: string;
  cin!: string;
  enabled!: boolean;
  createdDate!: Date;
  lastModifiedDate!: Date;
  createBy!: string;
  lastModifiedBy!: string;
  roles!: Set<string>;
}
