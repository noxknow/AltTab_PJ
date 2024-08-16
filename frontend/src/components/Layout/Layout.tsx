import { Outlet } from 'react-router-dom';
import { Header } from '@/components/Header/Header';

export function Layout() {
  return (
    <>
      <Header />
      <Outlet />
    </>
  );
}
