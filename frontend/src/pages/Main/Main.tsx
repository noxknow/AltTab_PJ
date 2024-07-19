import { CommunityProfile } from '@/components/CommunityProfile/CommunityProfile';
import styles from './Main.module.scss';
import { Button } from '../../components/Button/Button';

export function MainPage() {
  return (
    <div className={styles.main}>
      <div className={styles.main_left}>
        <div className={styles.title}>
          <div>ALTTAB</div>
        </div>
        <div className={styles.title}>
          <div>
            <span className={styles.highlighted}>Al</span>gorithm
          </div>
          <div>
            <span className={styles.highlighted}>T</span>raining
          </div>
          <div className={styles.highlighted}>TAB</div>
        </div>

        <div className={styles.comment}>
          동료와 함께 배우고, 토론하고, 성장하세요!
        </div>
        <div>
          <Button color="black" fill={true} size="large">
            스터디 생성
          </Button>
        </div>
        <CommunityProfile />
      </div>
      <div className={styles.main_right}>
        <img
          className={styles.image}
          src="src/assets/images/LandingPage.png"
          alt="No Image"
        />
      </div>
    </div>
  );
}