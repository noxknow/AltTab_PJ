import { useState } from 'react';

import styles from './Compiler.module.scss';
import CanvasSection from '@/pages/Canvas/CanvasSection';
import { Button } from '@/components/Button/Button';
import { Modal } from '@/components/Modal/Modal';
import { useCompilerModalState } from '@/hooks/useCompilerState';
import { MODAL } from '@/constants/Modal';

import { LineNumber } from './LineNumber';
import { CompilerSidebar } from './CompilerSidebar';

export function Compiler() {
  const [codeText, setCodeText] = useState('');
  const [canvasIsOpen, setCanvasIsOpen] = useState(false);
  const { isModalOpen, setIsModalOpen, setModal, setIsFill } =
    useCompilerModalState();

  const handleChange = (event: React.ChangeEvent<HTMLTextAreaElement>) => {
    const newCodeText = event.target.value;
    setCodeText(newCodeText);
  };

  const openCanvas = () => {
    setCanvasIsOpen(true);
  };

  const closeCanvas = () => {
    setCanvasIsOpen(false);
  };

  const openRunCodeModal = () => {
    setModal(MODAL.RUN);
    setIsFill(false);
    setIsModalOpen(true);
  };

  return (
    <div className={styles.container}>
      {isModalOpen && (
        <Modal code={codeText} /> //handleModalClose={closeModal} modalContent={modal} />
      )}
      <div className={styles.compilerContainer}>
        <CompilerSidebar />
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
        <div className={styles.buttonContainer}>
          <Button
            color="green"
            fill={true}
            size="large"
            onClick={openRunCodeModal}
          >
            Run Code
          </Button>
          <Button color="green" fill={true} size="large" onClick={openCanvas}>
            Draw
          </Button>
        </div>
      </div>
      {canvasIsOpen && <CanvasSection handleCanvas={closeCanvas} />}
    </div>
  );
}
