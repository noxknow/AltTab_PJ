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
  const [outputText, setOutputText] = useState<string | null>('');

  const handleInputChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    setInputText(e.currentTarget.value);
  };

  const runCode = async () => {
    const form: requestCompiler = {
      code,
      input: inputText,
    };
    const { output, errorMessage } = await compiler.execute(form);
    setOutputText(output !== null ? output : errorMessage);
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
            value={outputText ? outputText : ''}
          ></textarea>
        </div>
      </div>
      <Button color="green" fill={true} size="small" onClick={runCode}>
        Run
      </Button>
    </div>
  );
}
