import { useContext } from 'react';
import { ClickedDateContext } from '@/contexts/clickedDate';

export const useClickedDate = () => {
  const context = useContext(ClickedDateContext);

  if (context === undefined) {
    throw new Error('useClickedDate must be used within a ClickedDateProvider');
  }

  const { clickedDate, setClickedDate } = context;
  return { clickedDate, setClickedDate };
};
