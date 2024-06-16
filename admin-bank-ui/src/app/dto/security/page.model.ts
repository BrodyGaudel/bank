import {UserResponse} from "./user.response";

export class PageModel {
  page!: number;
  totalPages!: number;
  size!: number;
  totalElements!: number;
  numberOfElements!: number;
  number!: number;
  hasContent!: boolean;
  hasNext!: boolean;
  hasPrevious!: boolean;
  isFirst!: boolean;
  isLast!: boolean;
  content!: UserResponse[];
}
