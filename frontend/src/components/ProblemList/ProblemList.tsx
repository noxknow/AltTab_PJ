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

type ProblemListProps = {
  styleType: 'small' | 'big';
};

export function ProblemList({ styleType }: ProblemListProps) {
  const styles = styleType === 'small' ? stylesSmall : stylesBig;

  const data = [
    {
      week: 7,
      title: '구슬 탈출 2',
      person: '이재영',
      progress: 50,
      difficulty: 'G4',
      category: ['구현', '그래프 탐색', '시뮬레이션'],
    },
    {
      week: 7,
      title: '구슬 탈출 2',
      person: '이재영',
      progress: 60,
      difficulty: 'G4',
      category: ['구현', '그래프 탐색', '시뮬레이션'],
    },
    {
      week: 7,
      title: '구슬 탈출 2',
      person: '이재영',
      progress: 70,
      difficulty: 'G4',
      category: ['구현', '그래프 탐색', '시뮬레이션'],
    },
    {
      week: 7,
      title: '구슬 탈출 2',
      person: '이재영',
      progress: 80,
      difficulty: 'G4',
      category: ['구현', '그래프 탐색', '시뮬레이션'],
    },
    {
      week: 7,
      title: '구슬 탈출 2',
      person: '이재영',
      progress: 50,
      difficulty: 'G4',
      category: ['구현', '그래프 탐색', '시뮬레이션'],
    },
    {
      week: 7,
      title: '구슬 탈출 2',
      person: '이재영',
      progress: 50,
      difficulty: 'G4',
      category: ['구현', '그래프 탐색', '시뮬레이션'],
    },
  ];

  return (
    <div className={styles.table}>
      <table>
        <thead>
          <tr>
            <th>
              <div className={styles.icon}>
                <WeekSVG />
                <span>Week</span>
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
          {data.map((problem, index) => (
            <tr key={index}>
              <td>
                <DisabledButton>Week {problem.week}</DisabledButton>
              </td>
              <td>{problem.title}</td>
              <td>{problem.person}</td>
              <td>
                <div className={styles.progress}>
                  <div>
                    <PieChart percentage={problem.progress} />
                  </div>
                  <div>{problem.progress}</div>
                </div>
              </td>
              <td>
                <DisabledButton color="green">
                  {problem.difficulty}
                </DisabledButton>
              </td>
              <td>
                <div className={styles.category}>
                  {problem.category.map((cat, catIndex) => (
                    <span key={catIndex}>
                      <DisabledButton color="blue">{cat}</DisabledButton>
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
