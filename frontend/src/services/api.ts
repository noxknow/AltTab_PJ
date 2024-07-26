import axios from 'axios';

import { URL } from '@/constants/url';

const headers = {
  'Content-Type': 'application/json',
  Accept: 'application/json',
};

export const API = axios.create({
  baseURL:
    import.meta.env.MODE === 'development'
      ? 'http://localhost:8080/api/v1'
      : URL.API,
  headers,
  withCredentials: true,
});
