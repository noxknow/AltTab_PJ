import { memberInfo } from '@/types/study.ts';
import MembersSVG from '@/assets/icons/members.svg?react';
import { MemberProfile } from './MemberProfile';
import styles from './StudyMembers.module.scss';

type studyMemberProps = { members: memberInfo[] };

export function StudyMembers({ members }: studyMemberProps) {
  return (
    <div className={styles.main}>
      <div className={styles.top}>
        <div>
          <MembersSVG />
        </div>
        <div>Members</div>
      </div>
      {members &&
        members.map((member, index) => (
          <MemberProfile
            key={index}
            url={member.avatarUrl}
            name={member.name}
            point={member.point}
          />
        ))}
    </div>
  );
}
