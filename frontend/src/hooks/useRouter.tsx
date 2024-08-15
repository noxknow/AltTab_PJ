import { createBrowserRouter } from 'react-router-dom';

import { Layout } from '@/components/Layout/Layout';
import { MainPage } from '@/pages/Main/Main';
import { NoStudy } from '@/pages/NoStudy/NoStudy';
import { Compiler } from '@/pages/Compiler/Compiler';
import { NewStudy } from '@/pages/NewStudy/NewStudy';
import { Study } from '@/pages/Study/Study';
import { CompilerProvider } from '@/contexts/compiler';
import { NotFound } from '@/pages/NotFound/NotFound';
import { StudyProblems } from '@/pages/StudyProblems/StudyProblems';
import { Community } from '@/pages/Community/Community';
import { Recommend } from '@/pages/Recommend/Recommend';
import { Notifications } from '@/pages/Notifications/Notifications';

export const useRouter = () =>
  createBrowserRouter([
    {
      path: '/',
      element: <Layout />,
      errorElement: <NotFound />,
      children: [
        { index: true, element: <MainPage /> },
        {
          path: 'compiler/:studyId/:problemId',
          element: (
            <CompilerProvider>
              <Compiler />
            </CompilerProvider>
          ),
          errorElement: <MainPage />,
        },
        {
          path: 'study/:studyId',
          element: <Study />,
          errorElement: <NoStudy />,
        },
        {
          path: 'problems/:studyId',
          element: <StudyProblems />,
          errorElement: <MainPage />,
        },
        { path: 'noStudy', element: <NoStudy /> },
        { path: 'newStudy', element: <NewStudy /> },
        { path: 'community', element: <Community /> },
        {
          path: 'notifications',
          element: <Notifications />,
          errorElement: <MainPage />,
        },
        {
          path: 'Recommend/:studyId',
          element: <Recommend />,
          errorElement: <MainPage />,
        },
      ],
    },
  ]);
