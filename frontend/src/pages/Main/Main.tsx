import { useNavigate } from 'react-router-dom';

import { CommunityProfile } from '@/components/CommunityProfile/CommunityProfile';
import styles from './Main.module.scss';
import { Button } from '@/components/Button/Button';
import MAIN_IMAGE from '@/assets/images/LandingPage.png';

export function MainPage() {
  const navigate = useNavigate();

  const handleClick = () => {
    navigate('newStudy');
  };

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
          <Button color="black" fill={true} size="large" onClick={handleClick}>
            스터디 생성
          </Button>
        </div>
        <CommunityProfile />
      </div>
      <div className={styles.main_right}>
        <img className={styles.image} src={MAIN_IMAGE} alt="No Image" />
      </div>
    </div>
  );
}
