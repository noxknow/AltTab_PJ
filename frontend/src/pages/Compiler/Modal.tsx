import CloseSVG from '@/assets/icons/close.svg?react';

import styles from './Modal.module.scss';

type ModalProps = {
  handleModal: () => void;
};

export function Modal({ handleModal }: ModalProps) {
  return (
    <>
      <div className={styles.modalContainer}>
        <button className={styles.closeButton} onClick={handleModal}>
          <CloseSVG width={24} height={24} stroke="#F24242" />
        </button>
        <div>Modal</div>
      </div>
    </>
  );
}
