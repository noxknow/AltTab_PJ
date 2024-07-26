import axios from 'axios';

import { URL } from '@/constants/url';

const baseURL =
  import.meta.env.MODE === 'development' ? URL.API_LOCAL : URL.API;

const apiVersion = '/v1';

const headers = {
  'Content-Type': 'application/json',
  Accept: 'application/json',
};

export const API = axios.create({
  baseURL: baseURL + apiVersion,
  headers,
  withCredentials: true,
});
