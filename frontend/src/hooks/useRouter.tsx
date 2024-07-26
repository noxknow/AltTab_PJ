import { createBrowserRouter } from 'react-router-dom';

import { Layout } from '@/components/Layout/Layout';
import { MainPage } from '@/pages/Main/Main';
import { NoStudy } from '@/pages/NoStudy/NoStudy';
import { Compiler } from '@/pages/Compiler/Compiler';
import { StudySetting } from '@/pages/StudySetting/StudySetting';
import { CompilerProvider } from '@/context/compiler';

export const useRouter = () =>
  createBrowserRouter([
    {
      path: '/',
      element: <Layout />,
      children: [
        { index: true, element: <MainPage /> },
        {
          path: 'compiler',
          element: (
            <CompilerProvider>
              <Compiler />
            </CompilerProvider>
          ),
        },
        { path: 'nostudy', element: <NoStudy /> },
        { path: 'studysetting', element: <StudySetting /> },
      ],
    },
  ]);
