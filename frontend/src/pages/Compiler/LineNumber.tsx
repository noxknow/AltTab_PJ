import styles from './LineNumber.module.scss';

export function LineNumber({ codeText }: { codeText: string }) {
  const code = codeText === undefined ? '' : codeText;

  return (
    <div className={styles.lineNumberContainer}>
      {code.split('\n').map((_, index) => (
        <div key={index} className={styles.lineNumber}>
          {index + 1}
        </div>
      ))}
    </div>
  );
}
