import { useNavigate } from 'react-router-dom';

import { CommunityProfile } from '@/components/CommunityProfile/CommunityProfile';
import { Button } from '@/components/Button/Button';
import MAIN_IMAGE from '@/assets/images/LandingPage.png';
import backgroundImage from '@/assets/images/background-removebg.png';

import styles from './Main.module.scss';

export function MainPage() {
  const navigate = useNavigate();

  const handleClick = () => {
    navigate('newStudy');
  };

  return (
    <div className={styles.main}>
      <img src={backgroundImage} alt="Background" className={styles.backgroundImage} />
      <div className={styles.overlay}></div>
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
        <CommunityProfile
          study={{
            studyId: 1,
            studyName: '알고하이',
            studyDescription: '자바를 활용한 알고리즘 스터디입니다.',
            follower: 1000,
            like: 1000,
            view: 500,
            leaderMemberDto: {
              name: 'Lana Steiner',
              avatarUrl: 'https://fir-rollup.firebaseapp.com/de-sm.jpg',
            },
            check: true,
          }}
          isLink={false}
        />
      </div>
      <div className={styles.main_right}>
        <img className={styles.image} src={MAIN_IMAGE} alt="No Image" />
      </div>
    </div>
  );
}
