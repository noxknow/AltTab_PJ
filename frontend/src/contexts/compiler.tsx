import { createContext, useState } from 'react';

interface CompilerModalProps {
  isModalOpen: boolean;
  setIsModalOpen: React.Dispatch<React.SetStateAction<boolean>>;
  modal: string;
  setModal: React.Dispatch<React.SetStateAction<string>>;
  isFill: boolean;
  setIsFill: React.Dispatch<React.SetStateAction<boolean>>;
}

export const CompilerModalContext = createContext<
  CompilerModalProps | undefined
>(undefined);

type CompilerProviderProps = {
  children: React.ReactNode;
};

export function CompilerProvider({ children }: CompilerProviderProps) {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [modal, setModal] = useState('');
  const [isFill, setIsFill] = useState(true);

  return (
    <CompilerModalContext.Provider
      value={{
        isModalOpen,
        setIsModalOpen,
        modal,
        setModal,
        isFill,
        setIsFill,
      }}
    >
      {children}
    </CompilerModalContext.Provider>
  );
}
