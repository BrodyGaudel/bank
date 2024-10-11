export class UserResetPasswordRequest {
  email!: string;
  code!: string;
  password!: string;
  confirmPassword!: string;
}
