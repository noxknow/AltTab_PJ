import { useState } from 'react';

import { LineNumber } from './LineNumber';
import { CompilerSidebar } from './CompilerSidebar';

import styles from './Compiler.module.scss';
import CanvasSection from '@/pages/Canvas/CanvasSection';
import { Button } from '@/components/Button/Button';
import { Modal } from '@/components/Modal/Modal';

export function Compiler() {
  const [codeText, setCodeText] = useState('');
  const [modalIsOpen, setModalIsOpen] = useState(false);
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

  const runCode = () => {
    // TODO : compiler를 통한 코드 실행 연동
  };

  return (
    <div className={styles.container}>
      {modalIsOpen && (
        <Modal handleModalClose={closeModal} modalContent={modalContent} />
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
        <Button color="green" fill={true} size="large" onClick={runCode}>
          Run Code
        </Button>
        <Button color="green" fill={true} size="large" onClick={openCanvas}>
          Draw
        </Button>
      </div>
      {canvasIsOpen && <CanvasSection handleCanvas={closeCanvas} />}
    </div>
  );
}
