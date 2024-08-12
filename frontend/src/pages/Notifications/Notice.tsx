import styles from './Notice.module.scss';

type NoticeItemProps = {
  content: string;
  date: string;
};

export function Notice({ content, date }: NoticeItemProps) {
  return (
    <div className={styles.notice}>
      <div className={styles.content}>{content}</div>
      <div className={styles.date}>{date}</div>
    </div>
  );
}