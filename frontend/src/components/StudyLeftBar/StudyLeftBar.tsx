import { useCallback, useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';

import { useGetStudyInfoQuery } from '@/queries/study';
import { useGetUpcomingScheduleQuery } from '@/queries/schedule';
import { useClickedDate } from '@/hooks/useClickedDate';
import { studyProblemDetails } from '@/types/schedule';
import { useStudyState } from '@/hooks/useStudyState';

import { StudyIntro } from './StudyIntro';
import { StudyMembers } from './StudyMembers';
import { StudySchedule } from './StudySchedule';

import styles from './StudyLeftBar.module.scss';

export function StudyLeftBar() {
  const { studyId } = useParams<{ studyId: string }>();
  const [upcomingSchedule, setUpcomingSchedule] = useState<
    studyProblemDetails | undefined
  >();
  const { studyMember } = useStudyState();
  const { clickedDate } = useClickedDate();
  const { data: studyInfo } = useGetStudyInfoQuery(studyId!);
  const { refetch } = useGetUpcomingScheduleQuery(studyId!);

  const getUpcomingSchedule = useCallback(async () => {
    const { data: nextSchedule } = await refetch();
    setUpcomingSchedule(nextSchedule);
  }, [studyId]);

  useEffect(() => {
    getUpcomingSchedule();
  }, [clickedDate]);

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
      {upcomingSchedule ? (
        <StudySchedule date={upcomingSchedule.deadline} />
      ) : (
        <>다음 일정 정보가 없습니다.</>
      )}
      {studyMember ? (
        <StudyMembers members={studyMember} />
      ) : (
        <>스터디원 정보가 없습니다.</>
      )}
    </div>
  );
}
