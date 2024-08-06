import { RouterProvider } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';

import { useRouter } from './hooks/useRouter';
import './App.css';

const queryClient = new QueryClient();

function App() {
  const router = useRouter();

  return (
    <QueryClientProvider client={queryClient}>
      <RouterProvider router={router} />
    </QueryClientProvider>
  );
}

export default App;
