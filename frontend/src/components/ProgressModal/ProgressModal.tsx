import { PieChart } from '@/components/ProblemList/PieChart';
import styles from './ProgressModal.module.scss';
import { useState } from 'react';
import CheckedBoxSVG from '@/assets/icons/checkedbox.svg?react';
import UnCheckedBoxSVG from '@/assets/icons/uncheckedbox.svg?react';

type ProgressModalProps = {
  style?: React.CSSProperties;
  setIsModal: (isOpen: boolean) => void;
  modalInfo: {
    percentage: number;
    people: string[];
  };
};

export function ProgressModal({
  style,
  setIsModal,
  modalInfo,
}: ProgressModalProps) {
  const [isSolved, setIsSolved] = useState(false);
  const handleSolved = () => {
    setIsSolved(!isSolved);
  };

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
          <div>{modalInfo.percentage}</div>
          {isSolved ? (
            <CheckedBoxSVG onClick={handleSolved} fill="#66CD00" />
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
