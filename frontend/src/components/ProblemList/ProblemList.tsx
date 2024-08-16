import React, { useState, useEffect, useCallback } from 'react';
import { useParams, NavLink } from 'react-router-dom';
import styles from './ProblemListStyleSmall.module.scss';
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
import { useChangeColor } from '@/hooks/useChangeColor';
import { useStudyState } from '@/hooks/useStudyState';

type ProblemListProps = {
  studyInfo: studyProblemDetails | null | undefined;
  refetchSchedule: () => Promise<void>;
};

export function ProblemList({ studyInfo, refetchSchedule }: ProblemListProps) {
  const [isModal, setIsModal] = useState(false);
  const [modalPosition, setModalPosition] = useState({ top: 0, left: 0 });
  const [modalInfo, setModalInfo] = useState<{
    percentage: number;
    people: string[];
    check: boolean;
    problemId: number;
  } | null>(null);
  const { studyId } = useParams<{ studyId: string }>();
  const { isMember } = useStudyState();

  const {
    tagColors,
    getDifficultyColor,
    getDifficultyLabel,
    getDeadlineColor,
  } = useChangeColor(studyInfo);

  const handleMouseEnter = useCallback(
    (
      e: React.MouseEvent<HTMLDivElement>,
      progress: number,
      members: string[],
      check: boolean,
      problemId: number,
    ) => {
      const rect = e.currentTarget.getBoundingClientRect();
      setModalPosition({
        top: rect.top + window.scrollY,
        left: rect.left + window.scrollX,
      });
      setIsModal(true);
      setModalInfo({
        percentage: progress,
        people: members,
        check: check,
        problemId: problemId,
      });
    },
    [],
  );

  const handleMouseLeave = useCallback(() => {
    setIsModal(false);
  }, []);

  useEffect(() => {
    if (studyInfo && isModal && modalInfo) {
      const updatedProblem = studyInfo.studyProblems.find(
        (p) => p.problemId === modalInfo.problemId,
      );
      if (updatedProblem) {
        setModalInfo((prevInfo) => {
          if (
            prevInfo &&
            (prevInfo.percentage !==
              (updatedProblem.members.length / updatedProblem.size) * 100 ||
              prevInfo.check !== updatedProblem.check ||
              JSON.stringify(prevInfo.people) !==
                JSON.stringify(updatedProblem.members))
          ) {
            return {
              percentage:
                (updatedProblem.members.length / updatedProblem.size) * 100,
              people: updatedProblem.members,
              check: updatedProblem.check,
              problemId: updatedProblem.problemId,
            };
          }
          return prevInfo;
        });
      }
    }
  }, [studyInfo, isModal, modalInfo]);

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
                <DisabledButton color={getDeadlineColor()}>
                  {studyInfo.deadline}
                </DisabledButton>
              </td>
              <td>
                <NavLink
                  to={`/compiler/${studyId}/${problem.problemId}`}
                  onClick={() => {
                    localStorage.setItem('presenter', problem.presenter);
                  }}
                >
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
                      problem.check,
                      problem.problemId,
                    )
                  }
                  onMouseLeave={handleMouseLeave}
                >
                  <div>
                    <PieChart
                      percentage={(problem.members.length / problem.size) * 100}
                    />
                  </div>
                  <div>
                    {((problem.members.length / problem.size) * 100).toFixed(0)}
                    %
                  </div>
                </div>
              </td>
              <td>
                <DisabledButton color={getDifficultyColor(problem.level)}>
                  {getDifficultyLabel(problem.level)}
                </DisabledButton>
              </td>
              <td>
                <div className={styles.category}>
                  {problem.tag.split(';').map((cat, catIndex) => (
                    <span key={catIndex}>
                      <DisabledButton color={tagColors[cat.trim()]}>
                        {cat.trim()}
                      </DisabledButton>
                    </span>
                  ))}
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      {isMember && isModal && modalInfo && (
        <ProgressModal
          setIsModal={setIsModal}
          style={{
            position: 'absolute',
            top: modalPosition.top,
            left: modalPosition.left,
          }}
          modalInfo={modalInfo}
          refetchSchedule={refetchSchedule}
        />
      )}
    </div>
  );
}
