import React from 'react';
import { Card } from './Card';
import styles from './Section.module.scss';

interface SectionProps {
  title: string;
  problems: { title: string; tags: string; level: number; problemId: number }[];
}

export function Section({ title, problems }: SectionProps) {
  return (
    <div className={styles.section}>
      <h2>{title}</h2>
      <div className={styles.cards}>
        {problems.map((problem, index) => (
          <Card
            key={index}
            title={problem.title}
            tags={problem.tags}
            level={problem.level}
            problemId={problem.problemId}
          />
        ))}
      </div>
    </div>
  );
}