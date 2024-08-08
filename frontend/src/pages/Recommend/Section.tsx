import React from 'react';
import { Card } from './Card';
import styles from './Section.module.scss';

interface SectionProps {
  title: string;
}

export function Section({ title }: SectionProps) {
  return (
    <div className={styles.section}>
      <h2>{title}</h2>
      <div className={styles.cards}>
        <Card />
        <Card />
        <Card />
      </div>
    </div>
  );
}
