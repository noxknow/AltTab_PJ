import { useState } from 'react';

import { LineNumber } from './LineNumber';
import { CompilerSidebar } from './CompilerSidebar';

import styles from './Compiler.module.scss';

export function Compiler() {
  const [codeText, setCodeText] = useState('');

  function handleChange(event: React.ChangeEvent<HTMLTextAreaElement>) {
    const newCodeText = event.target.value;
    setCodeText(newCodeText);
  }

  return (
    <div className={styles.container}>
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
    </div>
  );
}
