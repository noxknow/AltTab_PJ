import { useState } from 'react';
import { useParams } from 'react-router-dom';
import stylesSmall from './ProblemListStyleSmall.module.scss';
import stylesBig from './ProblemListStyleBig.module.scss';
import { DisabledButton } from '@/components/DisabledButton/DisabledButton';
import { PieChart } from './PieChart';
import ProgressSVG from '@/assets/icons/progress.svg?react';
import StarSVG from '@/assets/icons/star.svg?react';
import TitleSVG from '@/assets/icons/title.svg?react';
import WeekSVG from '@/assets/icons/week.svg?react';
import CategorySVG from '@/assets/icons/category.svg?react';
import PersonSVG from '@/assets/icons/person.svg?react';
import { ProgressModal } from '@/components/ProgressModal/ProgressModal';
import { studyProblemDetails } from '@/types/schedule';
import { NavLink } from 'react-router-dom';

type ProblemListProps = {
  styleType: 'small' | 'big';
  studyInfo: studyProblemDetails | null | undefined;
};

export function ProblemList({ styleType, studyInfo }: ProblemListProps) {
  const styles = styleType === 'small' ? stylesSmall : stylesBig;
  const [isModal, setIsModal] = useState(false);
  const [modalPosition, setModalPosition] = useState({ top: 0, left: 0 });
  const [modalInfo, setModalInfo] = useState<{
    percentage: number;
    people: string[];
  }>({
    percentage: 0,
    people: [],
  });
  const { studyId } = useParams<{ studyId: string }>();

  const handleMouseEnter = (
    e: React.MouseEvent<HTMLDivElement>,
    progress: number,
    members: string[],
  ) => {
    const rect = e.currentTarget.getBoundingClientRect();
    setModalPosition({
      top: rect.top + window.scrollY,
      left: rect.left + window.scrollX,
    });
    setIsModal(true);
    setModalInfo({ percentage: progress, people: members });
  };

  const handleMouseLeave = () => {
    setIsModal(false);
  };

  return (
    <div className={styles.table}>
      <table>
        <thead>
          <tr>
            <th>
              <div className={styles.icon}>
                <WeekSVG />
                <span>Date</span>
              </div>
            </th>
            <th>
              <div className={styles.icon}>
                <TitleSVG />
                <span>문제 제목</span>
              </div>
            </th>
            <th>
              <div className={styles.icon}>
                <PersonSVG />
                담당자
              </div>
            </th>
            <th>
              <div className={styles.icon}>
                <ProgressSVG />
                Progress
              </div>
            </th>
            <th>
              <div className={styles.icon}>
                <StarSVG />
                난이도
              </div>
            </th>
            <th>
              <div className={styles.icon}>
                <CategorySVG />
                유형
              </div>
            </th>
          </tr>
        </thead>
        <tbody>
          {studyInfo?.studyProblems.map((problem, index) => (
            <tr key={index}>
              <td>
                <DisabledButton>{studyInfo.deadline}</DisabledButton>
              </td>
              <td>
                <NavLink to={`/compiler/${studyId}/${problem.problemId}`}>
                  <div>
                    {problem.problemId}. {problem.title}
                  </div>
                </NavLink>
              </td>
              <td>{problem.presenter}</td>
              <td>
                <div
                  className={styles.progress}
                  onMouseEnter={(e) =>
                    handleMouseEnter(
                      e,
                      (problem.members.length / problem.size) * 100,
                      problem.members,
                    )
                  }
                  onMouseLeave={handleMouseLeave}
                >
                  <div>
                    <PieChart
                      percentage={(problem.members.length / problem.size) * 100}
                    />
                  </div>
                  <div>{(problem.members.length / problem.size) * 100}</div>
                </div>
              </td>
              <td>
                <DisabledButton color="green">{problem.level}</DisabledButton>
              </td>
              <td>
                <div className={styles.category}>
                  {problem.tag.split(';').map((cat, catIndex) => (
                    <span key={catIndex}>
                      <DisabledButton color="blue">{cat.trim()}</DisabledButton>
                    </span>
                  ))}
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      {isModal && (
        <ProgressModal
          setIsModal={setIsModal}
          style={{
            position: 'absolute',
            top: modalPosition.top,
            left: modalPosition.left,
          }}
          modalInfo={modalInfo}
        />
      )}
    </div>
  );
}
