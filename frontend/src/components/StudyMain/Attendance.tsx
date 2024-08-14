import AttendanceSVG from '@/assets/icons/attendance.svg?react';
import { Button } from '@/components/Button/Button';
import styles from './Attendance.module.scss';
import { Calendar } from './Calendar';
import { usePostAttendanceQuery } from '@/queries/attendance';
import { useParams } from 'react-router-dom';
import { format } from 'date-fns';

export function Attendance() {
  const { studyId } = useParams<{ studyId: string }>();
  const usePostAttendanceQueryMutation = usePostAttendanceQuery(
    parseInt(studyId!),
    format(new Date(), 'yyyy-MM-dd'),
  );

  const handleClick = () => {
    usePostAttendanceQueryMutation.mutate();
  };

  return (
    <div className={styles.main}>
      <div className={styles.top}>
        <div className={styles.header}>
          <div>
            <AttendanceSVG />
          </div>
          <div>출석부</div>
        </div>
        <div>
          <Button onClick={handleClick} color="green" fill={true} size="small">
            출석하기
          </Button>
        </div>
      </div>
      <div>
        <Calendar />
      </div>
    </div>
  );
}
