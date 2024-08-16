import { useContext } from 'react';
import { CompilerModalContext } from '@/contexts/compiler';

export function useCompilerModalState() {
  const context = useContext(CompilerModalContext);
  if (context === undefined) {
    throw new Error(
      'useCompilerModalState should be used within CompilerModalContext',
    );
  }
  const { isModalOpen, setIsModalOpen, modal, setModal, isFill, setIsFill } =
    context;
  return { isModalOpen, setIsModalOpen, modal, setModal, isFill, setIsFill };
}
