export class PageResponse<T> {
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
  content!: Array<T>;
}
