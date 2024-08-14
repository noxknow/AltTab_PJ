import { useState, useCallback, useEffect } from 'react';
import ProblemSVG from '@/assets/icons/problem.svg?react';
import PlusSVG from '@/assets/icons/plus.svg?react';
import { ProblemList } from '@/components/ProblemList/ProblemList';
import styles from './WeeklyProblems.module.scss';
import { ProblemInputModal } from '@/components/ProblemInputModal/ProblemInputModal';
import {
  useGetScheduleQuery,
  useGetUpcomingScheduleQuery,
} from '@/queries/schedule';
import { studyProblemDetails } from '@/types/schedule';
import { useParams } from 'react-router-dom';
import { useClickedDate } from '@/hooks/useClickedDate';

export function WeeklyProblems() {
  const [isModal, setIsModal] = useState(false);
  const [studyInfo, setStudyInfo] = useState<studyProblemDetails | null>();
  const { studyId } = useParams<{ studyId: string }>();
  const { clickedDate, setClickedDate } = useClickedDate();

  const { refetch: refetchGetSchedule } = useGetScheduleQuery(
    studyId!,
    clickedDate,
  );
  const { data: initialStudyInfo } = useGetUpcomingScheduleQuery();

  useEffect(() => {
    if (initialStudyInfo) {
      setStudyInfo(initialStudyInfo);
      setClickedDate(initialStudyInfo.deadline);
    }
  }, [initialStudyInfo]);

  const refetchSchedule = useCallback(async () => {
    const { data } = await refetchGetSchedule();
    if (data) {
      setStudyInfo(data);
    }
  }, [refetchGetSchedule]);

  useEffect(() => {
    refetchSchedule();
  }, [clickedDate]);

  return (
    <div className={styles.main}>
      <div className={styles.top}>
        <div className={styles.header}>
          <ProblemSVG />
          <div>이 주의 문제</div>
        </div>
        <button className={styles.button} onClick={() => setIsModal(true)}>
          <PlusSVG />
        </button>
      </div>
      <ProblemList styleType="small" studyInfo={studyInfo} />
      <ProblemInputModal
        open={isModal}
        onClose={() => setIsModal(false)}
        refetchSchedule={refetchSchedule}
      />
    </div>
  );
}
