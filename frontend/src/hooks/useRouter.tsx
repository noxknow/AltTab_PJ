import { createBrowserRouter } from 'react-router-dom';

import { Layout } from '../components/Layout/Layout';
import { MainPage } from '@/pages/Main/Main';
import { NoStudy } from '../pages/NoStudy/NoStudy';
import { Compiler } from '@/pages/Compiler/Compiler';
import { StudySetting } from '../pages/StudySetting/StudySetting';

export const useRouter = () =>
  createBrowserRouter([
    {
      path: '/',
      element: <Layout />,
      children: [
        { index: true, element: <MainPage /> },
        { path: 'compiler', element: <Compiler /> },
      ],
    },
    {
      path: '/nostudy',
      element: <Layout />,
      children: [{ index: true, element: <NoStudy /> }],
    },
    {
      path: '/studysetting',
      element: <Layout />,
      children: [{ index: true, element: <StudySetting /> }],
    },
  ]);
