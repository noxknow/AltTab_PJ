import { useState } from 'react';

import { compiler } from '@/services/compiler';
import { Button } from '@/components/Button/Button';
import { requestCompiler } from '@/types/compiler';

import styles from './RunCodeModal.module.scss';

type RunCodeModalProps = {
  code: string;
};

export function RunCodeModal({ code }: RunCodeModalProps) {
  const [inputText, setInputText] = useState('');
  const [outputText, setOutputText] = useState('');

  const handleInputChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    setInputText(e.currentTarget.value);
  };

  const runCode = async () => {
    const form: requestCompiler = {
      code,
      input: inputText,
    };
    const { output } = await compiler.execute(form);
    setOutputText(output !== null ? output : '');
  };

  return (
    <div className={styles.container}>
      <div className={styles.content}>
        <div>
          <div>Input</div>
          <textarea
            name="input"
            className={styles.textarea}
            onChange={handleInputChange}
          ></textarea>
        </div>
        <div>
          <div>Output</div>
          <textarea
            name="output"
            className={styles.textarea}
            readOnly
            value={outputText}
          ></textarea>
        </div>
      </div>
      <Button color="green" fill={true} size="small" onClick={runCode}>
        Run
      </Button>
    </div>
  );
}
