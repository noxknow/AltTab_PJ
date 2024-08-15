import classNames from 'classnames';

import { useCompilerModalState } from '@/hooks/useCompilerState';
import { MODAL } from '@/constants/modal';

import { ProblemModal } from './ProblemModal/ProblemModal';
import { SolutionModal } from './SolutionModal/SolutionModal';
import { CanvasModal } from './CanvasModal/CanvasModal';
import { RunCodeModal } from './RunCodeModal/RunCodeModal';
import styles from './Modal.module.scss';

type ModalProps = React.HTMLAttributes<HTMLDivElement> & {
  code: string;
  selected?: number;
};

export function Modal({ code, selected }: ModalProps) {
  const { modal, isFill } = useCompilerModalState();

  const modalClass = classNames(styles.modalContainer, {
    [styles.fill]: isFill,
  });

  return (
    <>
      <div className={modalClass}>
        {modal === MODAL.PROBLEM && <ProblemModal />}
        {modal === MODAL.SOLUTION && (
          <div className={styles.solutionModal}>
            <SolutionModal />
          </div>
        )}
        {modal === MODAL.CANVAS && <CanvasModal />}
        {modal === MODAL.RUN && (
          <RunCodeModal code={code} memberId={selected} />
        )}
      </div>
    </>
  );
}
