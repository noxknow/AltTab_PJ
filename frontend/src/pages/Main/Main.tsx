import { Alarm } from '@/components/Alarm/Alarm';
import styles from './Main.module.scss';

export function MainPage() {
  return (
    <>
      <Alarm />
      <div className={styles.title}>메인페이지</div>
    </>
  );
}
