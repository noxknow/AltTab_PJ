import { Button } from '@/components/Button/Button';
import styles from './RunCodeModal.module.scss';

export function RunCodeModal() {
  return (
    <div className={styles.container}>
      <Button color="green" fill={true} size="small">
        Run
      </Button>
      <div className={styles.content}>
        <div className={styles.textContainer}>
          <div>Input</div>
          <textarea name="input" className={styles.textarea}></textarea>
        </div>
        <div className={styles.textContainer}>
          <div>Output</div>
          <textarea name="output" className={styles.textarea}></textarea>
        </div>
      </div>
    </div>
  );
}
