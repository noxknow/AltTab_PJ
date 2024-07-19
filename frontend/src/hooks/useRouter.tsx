import { createBrowserRouter } from 'react-router-dom';

import { Layout } from '../components/Layout/Layout';
import { MainPage } from '@/pages/Main/Main';

export const useRouter = () =>
  createBrowserRouter([
    {
      path: '/',
      element: <Layout />,
      children: [{ index: true, element: <MainPage /> }],
    },
  ]);