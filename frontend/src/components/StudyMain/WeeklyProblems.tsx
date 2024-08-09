import ProblemSVG from '@/assets/icons/problem.svg?react';
import PlusSVG from '@/assets/icons/plus.svg?react';
import { ProblemList } from '@/components/ProblemList/ProblemList';
import styles from './WeeklyProblems.module.scss';
import { useState } from 'react';
import { ProblemInputModal } from '@/components/ProblemInputModal/ProblemInputModal';

export function WeeklyProblems() {
  const [isModal, setIsModal] = useState(false);

  return (
    <div className={styles.main}>
      <div className={styles.top}>
        <div className={styles.header}>
          <ProblemSVG />
          <div>이 주의 문제</div>
        </div>
        <button className={styles.button} onClick={() => setIsModal(true)}>
          <PlusSVG />
        </button>
      </div>
      <ProblemList styleType="small" />
      <ProblemInputModal open={isModal} onClose={() => setIsModal(false)} />
    </div>
  );
}
