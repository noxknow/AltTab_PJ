import { useParams, NavLink } from 'react-router-dom';
import styles from './ProblemListStyleBig.module.scss';
import { DisabledButton } from '@/components/DisabledButton/DisabledButton';
import StarSVG from '@/assets/icons/star.svg?react';
import TitleSVG from '@/assets/icons/title.svg?react';
import WeekSVG from '@/assets/icons/week.svg?react';
import CategorySVG from '@/assets/icons/category.svg?react';
import PersonSVG from '@/assets/icons/person.svg?react';
import { studyProblemsDetails } from '@/types/problems';
import { useChangeColor } from '@/hooks/useChangeColor';

type ProblemListProps = {
  problemsInfo: studyProblemsDetails | null | undefined;
};

export function ProblemList({ problemsInfo }: ProblemListProps) {
  const { studyId } = useParams<{ studyId: string }>();
  const {
    tagColors,
    getDifficultyColor,
    getDifficultyLabel,
    getDeadlineColor,
  } = useChangeColor(problemsInfo);

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
          {problemsInfo?.problemList.map((problem, index) => (
            <tr key={index}>
              <td>
                <DisabledButton color={getDeadlineColor()}>
                  {problem.deadline}
                </DisabledButton>
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
    </div>
  );
}
