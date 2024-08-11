export const URL = {
  API: import.meta.env.VITE_API_URL,
  API_LOCAL: import.meta.env.VITE_API_URL_LOCAL,
  MAIN:
    import.meta.env.MODE === 'development'
      ? import.meta.env.VITE_API_URL_MAIN
      : import.meta.env.VITE_API_URL,
  LOGIN:
    import.meta.env.MODE === 'development'
      ? import.meta.env.VITE_API_URL_LOCAL
      : import.meta.env.VITE_API_URL_LOGIN,
  LOCAL_SOCKET: import.meta.env.VITE_API_URL_LOCAL_SOCKET,
  SERVER_SOCKET: import.meta.env.VITE_API_URL_SERVER_SOCKET,
};
