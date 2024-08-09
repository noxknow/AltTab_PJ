import { memberInfo } from '@/types/study.ts';
import MembersSVG from '@/assets/icons/members.svg?react';
import { MemberProfile } from './MemberProfile';
import styles from './StudyMembers.module.scss';

export function StudyMembers({ memberNames }: memberInfo) {
  if (!memberNames) return null;

  const data = memberNames.map((name) => ({
    url: 'https://fir-rollup.firebaseapp.com/de-sm.jpg',
    name: name,
    point: 1732,
  }));

  return (
    <div className={styles.main}>
      <div className={styles.top}>
        <div>
          <MembersSVG />
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
