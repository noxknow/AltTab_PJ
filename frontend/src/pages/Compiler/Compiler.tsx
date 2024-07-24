import { useState } from 'react';

import { LineNumber } from './LineNumber';
import { CompilerSidebar } from './CompilerSidebar';

import styles from './Compiler.module.scss';
import CanvasSection from '@/pages/Canvas/CanvasSection';
import { Button } from '@/components/Button/Button';
import { Modal } from '@/components/Modal/Modal';
import { RunCodeModal } from './RunCodeModal/RunCodeModal';

export function Compiler() {
  const [codeText, setCodeText] = useState('');
  const [modalIsOpen, setModalIsOpen] = useState(false);
  const [runCodeModalIsOpen, setRunCodeModalIsOpen] = useState(false);
  const [canvasIsOpen, setCanvasIsOpen] = useState(false);
  const [modalContent, setModalContent] = useState<JSX.Element>();

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

  const openRunCodeModal = () => {
    setModalContent(RunCodeModal);
    setRunCodeModalIsOpen(true);
  };

  const closeRunCodeModal = () => {
    setRunCodeModalIsOpen(false);
  };

  return (
    <div className={styles.container}>
      {modalIsOpen && (
        <Modal handleModalClose={closeModal} modalContent={modalContent} />
      )}
      {runCodeModalIsOpen && (
        <Modal
          handleModalClose={closeRunCodeModal}
          modalContent={modalContent}
        />
      )}
      <div className={styles.compilerContainer}>
        <CompilerSidebar
          handleModal={openModal}
          setModalContent={setModalContent}
        />
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
