import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import CloseSVG from '@/assets/icons/close.svg?react';
import { useCompilerModalState } from '@/hooks/useCompilerState';
import styles from '../Modal.module.scss';

interface Problem {
  problem_id: string;
  description: string;
  input_description: string;
  output_description: string;
  sample_input: string;
  sample_output: string;
}

export function ProblemModal() {
  const { problemId } = useParams<{ problemId: string }>();
  const { setIsModalOpen } = useCompilerModalState();
  const [problemData, setProblemData] = useState<Problem | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchProblemData = async () => {
      try {
        const response = await fetch(
          `http://127.0.0.1:5000/problem/${problemId}`,
        );
        if (!response.ok) {
          throw new Error('Problem not found');
        }
        const data: Problem = await response.json();
        setProblemData(data);
      } catch (error) {
        console.error('Error fetching problem data:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchProblemData();
  }, [problemId]);

  const handleClose = () => {
    setIsModalOpen(false);
  };

  if (loading) {
    return <p>Loading...</p>;
  }

  if (!problemData) {
    return (
      <p>
        문제 데이터가 없어요.. 문의사항에 올려주시면 업데이트 하겠습니다.
        감사합니다.
      </p>
    );
  }

  const handleOpenInNewTab = () => {
    window.open(`https://www.acmicpc.net/problem/${problemId}`, '_blank');
  };

  return (
    <div className={styles.problemModalContainer}>
      <button className={styles.closeButton} onClick={handleClose}>
        <CloseSVG width={24} height={24} stroke="#F24242" />
      </button>
      <h2 className={styles.problemTitle}>문제번호 : {problemId}</h2>
      <div className={styles.problemContent}>
        <h3 className={styles.sectionTitle}>문제 설명</h3>
        <p className={styles.descriptionText}>{problemData.description}</p>

        <h3 className={styles.sectionTitle}>입력</h3>
        <p className={styles.inputDescriptionText}>
          {problemData.input_description}
        </p>

        <h3 className={styles.sectionTitle}>출력</h3>
        <p className={styles.outputDescriptionText}>
          {problemData.output_description}
        </p>

        <h3 className={styles.sectionTitle}>Sample Input</h3>
        <pre className={styles.sampleInputText}>{problemData.sample_input}</pre>

        <h3 className={styles.sectionTitle}>Sample Output</h3>
        <pre className={styles.sampleOutputText}>
          {problemData.sample_output}
        </pre>
      </div>
      <div className={styles.backjunbutton}>
        <button onClick={handleOpenInNewTab}>백준에서 문제 보기</button>
      </div>
    </div>
  );
}
