import { Card } from './Card';
import styles from './Section.module.scss';
import { SectionProps } from '@/types/recommend';

export function Section({ title, problems }: SectionProps) {
  return (
    <div className={styles.section}>
      <h2>{title}</h2>
      <div className={styles.cards}>
        {problems.map((problem) => (
          <Card key={problem.problem_id} {...problem} />
        ))}
      </div>
    </div>
  );
}
