import { ProblemIcon } from '@/components/ProblemIcon/ProblemIcon';
import { PlusIcon } from '@/components/PlusIcon/PlusIcon';
import ProblemList from '../ProblemList/ProblemList';
import styles from './WeeklyProblems.module.scss';
import { useState } from 'react';
import ProblemInputModal from '../ProblemInputModal/ProblemInputModal';

export default function WeeklyProblems() {
  const [isModal, setIsModal] = useState(false);

  return (
    <div className={styles.main}>
      <div className={styles.top}>
        <div className={styles.header}>
          <ProblemIcon />
          <div>이 주의 문제</div>
        </div>
        <button className={styles.button} onClick={() => setIsModal(true)}>
          <PlusIcon />
        </button>
      </div>
      <ProblemList styleType="small" />
      <ProblemInputModal open={isModal} onClose={() => setIsModal(false)} />
    </div>
  );
}
