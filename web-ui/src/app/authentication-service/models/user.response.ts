export class UserResponse {
  id!: string;
  firstname!: string;
  lastname!: string;
  placeOfBirth!: string;
  dateOfBirth!: Date;
  nationality!: string;
  gender!: string;
  cin!: string;
  email!: string;
  username!: string;
  enabled!: boolean;
  passwordNeedToBeModified!: boolean;
  lastLogin!: Date;
  createdBy!: string;
  createdDate!: Date;
  lastModifiedBy!: string;
  lastModifiedDate!: Date;
  roles!: string[];
}
