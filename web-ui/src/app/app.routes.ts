import { Routes } from '@angular/router';
import {HomeComponent} from "./authentication-service/components/home/home.component";
import {AuthenticationComponent} from "./authentication-service/components/authentication/authentication.component";
import {UsersComponent} from "./authentication-service/components/users/users.component";
import {UserComponent} from "./authentication-service/components/user/user.component";
import {UserCreateComponent} from "./authentication-service/components/user-create/user-create.component";
import {UserUpdateComponent} from "./authentication-service/components/user-update/user-update.component";
import {RolesComponent} from "./authentication-service/components/roles/roles.component";
import {RoleComponent} from "./authentication-service/components/role/role.component";
import {RoleUpdateComponent} from "./authentication-service/components/role-update/role-update.component";
import {RoleCreateComponent} from "./authentication-service/components/role-create/role-create.component";
import {
  UserPasswordUpdateComponent
} from "./authentication-service/components/user-password-update/user-password-update.component";
import {RequestCodeComponent} from "./authentication-service/components/request-code/request-code.component";
import {
  UserResetPasswordComponent
} from "./authentication-service/components/user-reset-password/user-reset-password.component";


export const routes: Routes = [
  {path: '', redirectTo: 'home', pathMatch: 'full' },
  {path: 'authentication', component: AuthenticationComponent},
  {path: 'home', component: HomeComponent},
  {path: 'users', component: UsersComponent},
  {path: 'user/:id', component: UserComponent},
  {path: 'user-create', component: UserCreateComponent},
  {path: 'user-update/:id', component: UserUpdateComponent},
  {path: 'user-update-password', component: UserPasswordUpdateComponent},
  {path: 'user-request-code', component: RequestCodeComponent},
  {path: 'reset', component: UserResetPasswordComponent},
  {path: 'roles', component: RolesComponent},
  {path: 'role/:id', component: RoleComponent},
  {path: 'role-create', component: RoleCreateComponent},
  {path: 'role-update/:id', component: RoleUpdateComponent},
];
