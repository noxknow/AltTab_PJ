import { createContext, useState } from 'react';
import { format } from 'date-fns';

interface ClickedDateContextType {
  clickedDate: string;
  setClickedDate: (date: string) => void;
}

export const ClickedDateContext = createContext<
  ClickedDateContextType | undefined
>(undefined);

type ClickedDateProviderProps = {
  children: React.ReactNode;
};

export function ClickedDateProvider({ children }: ClickedDateProviderProps) {
  const today = format(new Date(), 'yyyy-MM-dd');
  const [clickedDate, setClickedDate] = useState<string>(today);

  return (
    <ClickedDateContext.Provider value={{ clickedDate, setClickedDate }}>
      {children}
    </ClickedDateContext.Provider>
  );
}
