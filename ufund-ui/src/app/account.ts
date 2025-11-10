export interface Account {
  username: string;
  password: string;
  role: 'admin' | 'helper';
}
