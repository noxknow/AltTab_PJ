import { Button } from '@/components/Button/Button';
import { Tool } from '@/components/Tool/Tool';
import { People } from '@/components/People/People';
import { Info } from '@/components/Info/Info';
import { Input } from '@/components/Input/Input';

import styles from './NewStudy.module.scss';
import { Check } from '@/components/Check/Check';

export function NewStudy() {
  return (
    <div className={styles.main}>
      <div className={styles.title}>스터디 생성</div>
      <div className={styles.main_mid}>
        <div className={styles.option}>
          <Tool />
          <div>
            <div className={styles.small_title}>팀명 생성</div>
            <Input placeholder="팀명을 입력하세요" maxLength={15} />
          </div>
        </div>
        <div className={styles.option}>
          <People />
          <div>
            <div className={styles.small_title}>팀 초대</div>
            <Input type="email" placeholder="팀원을 초대하세요" />
          </div>
        </div>
        <div className={styles.option}>
          <Info />
          <div>
            <div className={styles.small_title}>Info</div>
            <Input placeholder="스터디를 소개해 주세요" maxLength={25} />
          </div>
        </div>
      </div>
      <Button color="green" fill={true} size="long">
        <Check />
        <span>생성</span>
      </Button>
    </div>
  );
}
