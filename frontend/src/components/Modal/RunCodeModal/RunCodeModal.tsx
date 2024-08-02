import { useEffect, useRef, useState } from 'react';
import { useParams } from 'react-router-dom';

import { compiler } from '@/services/compiler';
import { Button } from '@/components/Button/Button';
import { requestCompiler } from '@/types/compiler';
import { EXECUTOR } from '@/constants/executor';

import styles from './RunCodeModal.module.scss';

type RunCodeModalProps = {
  code: string;
  problemTab?: number;
};

export function RunCodeModal({ code, problemTab }: RunCodeModalProps) {
  const { studyId, problemId } = useParams();
  const [inputText, setInputText] = useState('');
  const [outputText, setOutputText] = useState<string | null>('');
  const [isLoading, setIsLoading] = useState(false);
  const polling = useRef<number | null>();

  const handleInputChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    setInputText(e.currentTarget.value);
  };

  const runCode = async () => {
    setOutputText('');
    const form: requestCompiler = {
      studyGroupId: studyId!,
      problemId: problemId!,
      problemTab: `${problemTab!}`,
      code,
      input: inputText,
    };
    const { status } = await compiler.execute(form);
    if (status === EXECUTOR.NOT_START || status === EXECUTOR.IN_PROGRESS) {
      setIsLoading(true);
    }
  };

  const getStatus = async () => {
    const { status, output, errorMessage } = await compiler.status(
      studyId!,
      problemId!,
      `${problemTab!}`,
    );
    if (status === EXECUTOR.DONE || status === EXECUTOR.FAIL) {
      setIsLoading(false);
      setOutputText(output !== null ? output : errorMessage);
    }
  };

  useEffect(() => {
    if (isLoading) {
      polling.current = setInterval(() => {
        getStatus();
      }, 500);
    }
    return () => clearInterval(polling.current!);
  }, [isLoading]);

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
      {isLoading ? (
        <Button color="black" fill={true} size="small" disabled>
          Running
        </Button>
      ) : (
        <Button
          color="green"
          fill={true}
          size="small"
          onClick={runCode}
          disabled={isLoading}
        >
          Run
        </Button>
      )}
    </div>
  );
}
