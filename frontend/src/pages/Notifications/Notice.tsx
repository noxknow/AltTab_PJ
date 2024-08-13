import styles from './Notice.module.scss';
import { Button } from '@/components/Button/Button';

type NoticeItemProps = {
  content: string;
  date: Date;
  onAccept: () => void;
  onReject: () => void;
};

export function Notice({ content, date, onAccept, onReject }: NoticeItemProps) {
  return (
    <div className={styles.notice}>
      <div className={styles.content}>{content}</div>
      <div className={styles.date}>{new Date(date).toLocaleString()}</div>
      <div className={styles.actions}>
        <Button onClick={onAccept} color="green" fill={true} size="small">수락</Button>
        <Button onClick={onReject} color="red" fill={true} size="small">거절</Button>
      </div>
    </div>
  );
}