import { useState } from 'react';

import { LineNumber } from './LineNumber';
import { CompilerSidebar } from './CompilerSidebar';
import { Modal } from './Modal';

import styles from './Compiler.module.scss';
import CanvasSection from '@/pages/Canvas/CanvasSection';
import { Button } from '@/components/Button/Button';

export function Compiler() {
  const [codeText, setCodeText] = useState('');
  const [modalIsOpen, setModalIsOpen] = useState(false);
  const [canvasIsOpen, setCanvasIsOpen] = useState(false);

  const handleChange = (event: React.ChangeEvent<HTMLTextAreaElement>) => {
    const newCodeText = event.target.value;
    setCodeText(newCodeText);
  };

  const openModal = () => {
    setModalIsOpen(true);
  };

  const closeModal = () => {
    setModalIsOpen(false);
  };

  const openCanvas = () => {
    setCanvasIsOpen(true);
  };

  const closeCanvas = () => {
    setCanvasIsOpen(false);
  };

  return (
    <div>
      <div className={styles.container}>
        {modalIsOpen && <Modal handleModal={closeModal} />}
        <div className={styles.compilerContainer}>
          <CompilerSidebar handleModal={openModal} />
          <div className={styles.compilerTitle}>Code Snippet</div>
          <div className={styles.compiler}>
            <div className={styles.compilerBody}>
              <LineNumber codeText={codeText} />
              <textarea
                className={styles.textArea}
                onChange={handleChange}
              ></textarea>
            </div>
          </div>
          {!canvasIsOpen && (
            <Button
              className={styles.drawButton}
              color="green"
              fill={true}
              size="large"
              onClick={openCanvas}
            >
              Draw
            </Button>
          )}
        </div>
      </div>
      {canvasIsOpen && <CanvasSection handleCanvas={closeCanvas} />}
    </div>
  );
}
