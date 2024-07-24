import CloseSVG from '@/assets/icons/close.svg?react';

import styles from './Modal.module.scss';

type ModalProps = React.HTMLAttributes<HTMLDivElement> & {
  handleModalClose: () => void;
  modalContent: JSX.Element | undefined;
};

export function Modal({ handleModalClose, modalContent }: ModalProps) {
  return (
    <>
      <div className={styles.modalContainer}>
        <button className={styles.closeButton} onClick={handleModalClose}>
          <CloseSVG width={24} height={24} stroke="#F24242" />
        </button>
        {modalContent}
      </div>
    </>
  );
}
