import { useParams } from 'react-router-dom';

import { useGetStudyInfoQuery, useGetStudyMemberQuery } from '@/queries/study';

import { StudyIntro } from './StudyIntro';
import { StudyMembers } from './StudyMembers';
import { StudySchedule } from './StudySchedule';

import styles from './StudyLeftBar.module.scss';

export function StudyLeftBar() {
  const { studyId } = useParams<{ studyId: string }>();
  const { data: studyInfo } = useGetStudyInfoQuery(studyId!);
  const { data: studyMember } = useGetStudyMemberQuery(studyId!);

  return (
    <div className={styles.main}>
      {studyInfo ? (
        <StudyIntro
          studyName={studyInfo.studyName}
          studyDescription={studyInfo.studyDescription}
        />
      ) : (
        <>스터디 정보가 없습니다.</>
      )}
      <StudySchedule date={new Date('2024-08-05 20:00:00')} />
      {studyMember ? (
        <StudyMembers members={studyMember} />
      ) : (
        <>스터디원 정보가 없습니다.</>
      )}
    </div>
  );
}
