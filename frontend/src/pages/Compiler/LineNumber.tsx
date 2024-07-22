import styles from './LineNumber.module.scss';

export function LineNumber({ codeText }: { codeText: string }) {
  return (
    <div className={styles.lineNumberContainer}>
      {codeText.split('\n').map((_, index) => (
        <div key={index} className={styles.lineNumber}>
          {index + 1}
        </div>
      ))}
    </div>
  );
}
