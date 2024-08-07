import stylesSmall from './ProblemListStyleSmall.module.scss';
import stylesBig from './ProblemListStyleBig.module.scss';
import { DisabledButton } from '@/components/DisabledButton/DisabledButton';
import PieChart from './PieChart';
import { ProgressIcon } from '@/components/ProgressIcon/ProgressIcon';
import { StarIcon } from '@/components/StarIcon/StarIcon';
import { TitleIcon } from '@/components/TitleIcon/TitleIcon';
import { WeekIcon } from '@/components/WeekIcon/WeekIcon';
import { CategoryIcon } from '@/components/CategoryIcon/CategoryIcon';
import { PersonIcon } from '@/components/PersonIcon/PersonIcon';

type ProblemListProps = {
  styleType: 'small' | 'big';
};

export default function ProblemList({ styleType }: ProblemListProps) {
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
                <WeekIcon />
                <span>Week</span>
              </div>
            </th>
            <th>
              <div className={styles.icon}>
                <TitleIcon />
                <span>문제 제목</span>
              </div>
            </th>
            <th>
              <div className={styles.icon}>
                <PersonIcon />
                담당자
              </div>
            </th>
            <th>
              <div className={styles.icon}>
                <ProgressIcon />
                Progress
              </div>
            </th>
            <th>
              <div className={styles.icon}>
                <StarIcon />
                난이도
              </div>
            </th>
            <th>
              <div className={styles.icon}>
                <CategoryIcon />
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
