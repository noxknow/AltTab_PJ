import styles from './Loading.module.scss';

export function Loading() {
  return (
    <div className={styles.loadingContainer}>
      <div className={styles.loader}>
        <div></div>
        <div></div>
        <div></div>
      </div>
    </div>
  );
}
