import { memberInfo } from '@/types/study.ts';

import { MembersIcon } from '../MembersIcon/MembersIcon';
import MemberProfile from './MemberProfile';
import styles from './StudyMembers.module.scss';

export default function StudyMembers({ memberNames }: memberInfo) {
  if (!memberNames) return null;
  
  const data = memberNames.map(name => ({
    url: 'https://fir-rollup.firebaseapp.com/de-sm.jpg',
    name: name,
    point: 1732,
  }));

  return (
    <div className={styles.main}>
      <div className={styles.top}>
        <div>
          <MembersIcon />
        </div>
        <div>Members</div>
      </div>

      {data.map((member, index) => (
        <MemberProfile
          key={index}
          url={member.url}
          name={member.name}
          point={member.point}
        />
      ))}
    </div>
  );
}
