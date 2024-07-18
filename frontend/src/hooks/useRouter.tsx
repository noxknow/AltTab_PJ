import { createBrowserRouter } from 'react-router-dom';

import { MainPage } from '@/pages/Main/Main';

export const useRouter = () =>
  createBrowserRouter([
    {
      path: '/',
      element: <MainPage />,
    },
  ]);
