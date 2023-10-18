export class AccountModel{
  id!: string;
  customerId!: string;
  balance!: number;
  currency!: string;
  status!: AccountStatus;
  creation!: string;
  lastUpdate!: string;
}

enum AccountStatus {
  ACTIVATED = 'ACTIVATED',
  BLOCKED = 'BLOCKED'
}
