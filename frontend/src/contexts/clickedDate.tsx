import { createContext, useState } from 'react';

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
  const [clickedDate, setClickedDate] = useState<string>('');

  return (
    <ClickedDateContext.Provider value={{ clickedDate, setClickedDate }}>
      {children}
    </ClickedDateContext.Provider>
  );
}
