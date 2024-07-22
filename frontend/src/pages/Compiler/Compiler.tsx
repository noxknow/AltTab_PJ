import { useState } from 'react';

import { LineNumber } from './LineNumber';
import { CompilerSidebar } from './CompilerSidebar';
import { Modal } from './Modal';

import styles from './Compiler.module.scss';

export function Compiler() {
  const [codeText, setCodeText] = useState('');
  const [modalIsOpen, setModalIsOpen] = useState(false);

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

  return (
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
      </div>
    </div>
  );
}
