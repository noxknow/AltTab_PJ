import React from 'react';
import { Content } from './Content';
import styles from './Recommend.module.scss';

export function Recommend() {
  return (
    <div className={styles.container}>
      <Content />
    </div>
  );
}