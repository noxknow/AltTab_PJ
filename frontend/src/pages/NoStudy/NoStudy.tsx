import { Button } from '../../components/Button/Button';
import styles from './NoStudy.module.scss';

export function NoStudy() {
  return (
    <div className={styles.main}>
      <div className={styles.title}>나만의 알고리즘 스터디</div>
      <div className={styles.comment}>
        스터디원을 모집하고, 알고리즘을 함께 리뷰하며 성장할 수 있습니다.
      </div>
      <div>
        <Button color="green" fill={true} size="large">
          스터디 생성
        </Button>
      </div>
      <div>
        <img
          className={styles.image}
          src="src/assets/images/NoStudy.png"
          alt="No Image"
        />
      </div>
    </div>
  );
}
