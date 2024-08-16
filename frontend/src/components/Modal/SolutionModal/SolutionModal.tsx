import CloseSVG from '@/assets/icons/close.svg?react';
import { useCompilerModalState } from '@/hooks/useCompilerState';
import { EditorPage } from '@/components/Editor/EditorPage';

import styles from '../Modal.module.scss';

export function SolutionModal() {
  const { setIsModalOpen } = useCompilerModalState();

  const handleClose = () => {
    setIsModalOpen(false);
  };

  return (
    <div className={styles.modal}>
      <button className={styles.closeButton} onClick={handleClose}>
        <CloseSVG width={24} height={24} stroke="#F24242" />
      </button>
      <EditorPage />
    </div>
  );
}
