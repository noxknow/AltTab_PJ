import { PieChart } from '@/components/ProblemList/PieChart';
import styles from './ProgressModal.module.scss';
import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import CheckedBoxSVG from '@/assets/icons/checkedbox.svg?react';
import UnCheckedBoxSVG from '@/assets/icons/uncheckedbox.svg?react';
import { useSolveProblemQuery } from '@/queries/problems';
import { useGetMyInfoQuery } from '@/queries/member';

type ProgressModalProps = {
  style?: React.CSSProperties;
  setIsModal: (isOpen: boolean) => void;
  modalInfo: {
    percentage: number;
    people: string[];
    check: boolean;
    problemId: number;
  };
  refetchSchedule: () => Promise<void>;
};

export function ProgressModal({
  style,
  setIsModal,
  modalInfo,
  refetchSchedule,
}: ProgressModalProps) {
  const { studyId } = useParams<{ studyId: string }>();
  const { data: userInfo } = useGetMyInfoQuery();
  const [isSolved, setIsSolved] = useState(modalInfo.check);
  const solveProblemMutation = useSolveProblemQuery(
    userInfo!.memberId,
    parseInt(studyId!),
    modalInfo.problemId,
  );
  const handleSolved = async () => {
    await solveProblemMutation.mutateAsync();
    refetchSchedule();
    setIsSolved(true);
  };

  useEffect(() => {
    setIsSolved(modalInfo.check);
  }, [modalInfo]);

  return (
    <div
      className={styles.overlay}
      onMouseEnter={() => setIsModal(true)}
      onMouseLeave={() => setIsModal(false)}
    >
      <div style={style} className={styles.modal}>
        <div className={styles.chart}>
          <div>
            <PieChart percentage={modalInfo.percentage}></PieChart>
          </div>
          <div>{Math.round(modalInfo.percentage)}</div>
          {isSolved ? (
            <CheckedBoxSVG fill="#66CD00" />
          ) : (
            <UnCheckedBoxSVG onClick={handleSolved} fill="#66CD00" />
          )}
        </div>
        <div className={styles.people}>
          {modalInfo.people.map((person, index) => (
            <div key={index}>{person}</div>
          ))}
        </div>
      </div>
    </div>
  );
}
