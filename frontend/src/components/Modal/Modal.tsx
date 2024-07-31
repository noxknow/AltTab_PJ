import classNames from 'classnames';

import CloseSVG from '@/assets/icons/close.svg?react';
import { useCompilerModalState } from '@/hooks/useCompilerState';
import { MODAL } from '@/constants/modal';

import { ProblemModal } from './ProblemModal/ProblemModal';
import { SolutionModal } from './SolutionModal/SolutionModal';
import { CanvasModal } from './CanvasModal/CanvasModal';
import { RunCodeModal } from './RunCodeModal/RunCodeModal';
import styles from './Modal.module.scss';

type ModalProps = React.HTMLAttributes<HTMLDivElement> & {
  code: string;
};

export function Modal({ code }: ModalProps) {
  const { setIsModalOpen, modal, isFill } = useCompilerModalState();

  const modalClass = classNames(styles.modalContainer, {
    [styles.fill]: isFill,
  });

  const handleModalClose = () => {
    setIsModalOpen(false);
  };

  return (
    <>
      <div className={modalClass}>
        <button className={styles.closeButton} onClick={handleModalClose}>
          <CloseSVG width={24} height={24} stroke="#F24242" />
        </button>
        {modal === MODAL.PROBLEM && <ProblemModal />}
        {modal === MODAL.SOLUTION && <SolutionModal />}
        {modal === MODAL.CANVAS && <CanvasModal />}
        {modal === MODAL.RUN && <RunCodeModal code={code} />}
      </div>
    </>
  );
}
