import { useParams } from 'react-router-dom';

import styles from './Study.module.scss';

export function Study() {
  const { studyId } = useParams();
  return <div className={styles.container}>Study ID : {studyId}</div>;
}
