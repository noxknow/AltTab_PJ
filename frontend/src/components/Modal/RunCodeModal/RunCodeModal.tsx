import { useEffect, useRef, useState } from 'react';
import { useParams } from 'react-router-dom';

import { Button } from '@/components/Button/Button';
import { requestExecutor } from '@/types/executor';
import { EXECUTOR } from '@/constants/executor';
import { useExecuteQuery, useGetExecutorStatusQuery } from '@/queries/executor';
import CloseSVG from '@/assets/icons/close.svg?react';
import { useCompilerModalState } from '@/hooks/useCompilerState';

import styles from './RunCodeModal.module.scss';

type RunCodeModalProps = {
  code: string;
  memberId?: number;
};

export function RunCodeModal({ code, memberId }: RunCodeModalProps) {
  const { studyId, problemId } = useParams();
  const [inputText, setInputText] = useState('');
  const [outputText, setOutputText] = useState<string | null>('');
  const [isLoading, setIsLoading] = useState(false);
  const polling = useRef<number | null>();
  const executeCode = useExecuteQuery();
  const { refetch } = useGetExecutorStatusQuery(
    studyId!,
    problemId!,
    memberId!.toString(),
  );
  const { setIsModalOpen } = useCompilerModalState();

  const handleClose = () => {
    setIsModalOpen(false);
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    setInputText(e.currentTarget.value);
  };

  const runCode = async () => {
    setOutputText('');
    const form: requestExecutor = {
      studyId: studyId!,
      problemId: problemId!,
      memberId: memberId!.toString(),
      code,
      input: inputText,
    };
    const { status } = await executeCode.mutateAsync(form);
    if (status === EXECUTOR.NOT_START || status === EXECUTOR.IN_PROGRESS) {
      setIsLoading(true);
    }
  };

  const getStatus = async () => {
    const { data } = await refetch();
    const { status, output, errorMessage } = data!;
    if (status === EXECUTOR.DONE || status === EXECUTOR.FAIL) {
      setIsLoading(false);
      setOutputText(output !== null ? output : errorMessage);
    }
  };

  useEffect(() => {
    if (isLoading) {
      polling.current = setInterval(() => {
        getStatus();
      }, 300);
    }
    return () => clearInterval(polling.current!);
  }, [isLoading]);

  return (
    <div className={styles.container}>
      <button className={styles.closeButton} onClick={handleClose}>
        <CloseSVG width={24} height={24} stroke="#F24242" />
      </button>
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
