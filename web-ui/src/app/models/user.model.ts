import {RoleModel} from "./role.model";

export class UserModel{
  id!: number;
  username!: string;
  password!: string;
  enabled!: boolean;
  roles!: RoleModel[];
}
