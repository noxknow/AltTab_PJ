import { useState } from 'react';
import classNames from 'classnames';

import ArrowSVG from '@/assets/icons/arrow.svg?react';
import CodeSVG from '@/assets/icons/code.svg?react';
import BookSVG from '@/assets/icons/book.svg?react';
import BoardSVG from '@/assets/icons/board.svg?react';
import styles from './CompilerSidebar.module.scss';

export function CompilerSidebar() {
  const [isOpen, setIsOpen] = useState(false);
  const sidebar = classNames(styles.sidebar, {
    [styles.openSidebar]: isOpen,
  });
  const toggleButton = classNames(styles.button, styles.toggleButton, {
    [styles.flip]: !isOpen,
  });
  const modalButton = classNames(styles.button, styles.modalButton);

  function handleToggleClick() {
    setIsOpen(!isOpen);
  }

  return (
    <div className={sidebar}>
      <button className={toggleButton} onClick={handleToggleClick}>
        <ArrowSVG />
      </button>
      <div className={styles.buttonContainer}>
        <div className={styles.modalButtonContainer}>
          <button className={modalButton}>
            <CodeSVG width={24} height={24} />
          </button>
          <button className={modalButton}>
            <BookSVG width={24} height={24} />
          </button>
          <button className={modalButton}>
            <BoardSVG width={24} height={24} />
          </button>
        </div>
      </div>
    </div>
  );
}
