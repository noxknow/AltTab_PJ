import { useState, useEffect } from 'react';

import styles from './Compiler.module.scss';
import CanvasSection from '@/pages/Canvas/CanvasSection';
import { Button } from '@/components/Button/Button';
import { Modal } from '@/components/Modal/Modal';
import { useCompilerModalState } from '@/hooks/useCompilerState';
import { MODAL } from '@/constants/Modal';
import { highlightCode } from '@/utils/highlightCode';

import { LineNumber } from './LineNumber';
import { CompilerSidebar } from './CompilerSidebar';

export function Compiler() {
  const [codeText, setCodeText] = useState('');
  const [highlightedCode, setHighlightedCode] = useState('');
  const [canvasIsOpen, setCanvasIsOpen] = useState(false);
  const { isModalOpen, setIsModalOpen, setModal, setIsFill } =
    useCompilerModalState();

  const handleChange = (event: React.ChangeEvent<HTMLTextAreaElement>) => {
    const newCodeText = event.target.value;
    setCodeText(newCodeText);
  };

  useEffect(() => {
    setHighlightedCode(highlightCode(codeText, 'java'));
  }, [codeText]);

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
      {isModalOpen && <Modal code={codeText} />}
      <div className={styles.compilerContainer}>
        <CompilerSidebar />
        <div className={styles.compilerTitle}>
          <div>Code Snippet</div>
          <div>Java</div>
        </div>
        <div className={styles.compiler}>
          <div className={styles.compilerBody}>
            <LineNumber codeText={codeText} />
            <div className={styles.codeContainer}>
              <textarea
                className={styles.textArea}
                onChange={handleChange}
              ></textarea>
              <pre className={styles.codeArea}>
                <code
                  className={styles.code}
                  dangerouslySetInnerHTML={{ __html: highlightedCode }}
                />
              </pre>
            </div>
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
      {<CanvasSection/>}
    </div>
  );
}
