import { expect, test } from 'vitest';
import { authHeader } from './api';

test('creates basic auth header', () => {
  expect(authHeader({ username: 'user', password: 'password' })).toBe('Basic dXNlcjpwYXNzd29yZA==');
});
