import classNames from 'classnames';
import CloseSVG from '@/assets/icons/close.svg?react';

import styles from './Modal.module.scss';

type ModalProps = React.HTMLAttributes<HTMLDivElement> & {
  handleModalClose: () => void;
  modalContent: JSX.Element | undefined;
  fill?: boolean;
};

export function Modal({
  handleModalClose,
  modalContent,
  fill = true,
}: ModalProps) {
  const modalClass = classNames(styles.modalContainer, {
    [styles.fill]: fill,
  });

  return (
    <>
      <div className={modalClass}>
        <button className={styles.closeButton} onClick={handleModalClose}>
          <CloseSVG width={24} height={24} stroke="#F24242" />
        </button>
        {modalContent}
      </div>
    </>
  );
}
