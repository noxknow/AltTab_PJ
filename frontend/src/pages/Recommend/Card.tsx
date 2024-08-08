import React from 'react';
import styles from './Card.module.scss';

export function Card() {
  return (
    <div className={styles.card}>
      <div className={styles.cardHeader}>
        <span>10158. 개미</span>
        <span className={styles.cardScore}>5</span>
      </div>
      <div className={styles.cardBody}>
        <span>수학, 사례연산, 애드 혹</span>
      </div>
    </div>
  );
}