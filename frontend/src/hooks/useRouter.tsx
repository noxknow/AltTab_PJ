import { lazy, Suspense } from 'react';
import { createBrowserRouter } from 'react-router-dom';

import { Layout } from '@/components/Layout/Layout';
import { MainPage } from '@/pages/Main/Main';
import { NoStudy } from '@/pages/NoStudy/NoStudy';
import { NewStudy } from '@/pages/NewStudy/NewStudy';
import { CompilerProvider } from '@/contexts/compiler';
import { NotFound } from '@/pages/NotFound/NotFound';
import { Community } from '@/pages/Community/Community';
import { Notifications } from '@/pages/Notifications/Notifications';
import { StudyProvider } from '@/contexts/study';
import { Loading } from '@/components/Loading/Loading';

const Study = lazy(() => import('@/pages/Study/Study'));
const StudyProblems = lazy(() => import('@/pages/StudyProblems/StudyProblems'));
const Compiler = lazy(() => import('@/pages/Compiler/Compiler'));
const Recommend = lazy(() => import('@/pages/Recommend/Recommend'));

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
            <Suspense fallback={<Loading />}>
              <StudyProvider>
                <CompilerProvider>
                  <Compiler />
                </CompilerProvider>
              </StudyProvider>
            </Suspense>
          ),
          errorElement: <MainPage />,
        },
        {
          path: 'study/:studyId',
          element: (
            <Suspense fallback={<Loading />}>
              <Study />
            </Suspense>
          ),
          errorElement: <NoStudy />,
        },
        {
          path: 'problems/:studyId',
          element: (
            <Suspense fallback={<Loading />}>
              <StudyProblems />
            </Suspense>
          ),
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
          element: (
            <Suspense fallback={<Loading />}>
              <Recommend />
            </Suspense>
          ),
          errorElement: <MainPage />,
        },
      ],
    },
  ]);
