import axios from 'axios';

import { URL } from '@/constants/url';

export const baseURL =
  import.meta.env.MODE === 'development' ? URL.API_LOCAL : URL.API;

export const socketURL =
  import.meta.env.MODE === 'development' ? URL.LOCAL_SOCKET : URL.SERVER_SOCKET;

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
