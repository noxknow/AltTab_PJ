import { Section } from './Section'; // Section 컴포넌트 임포트
import styles from './Recommend.module.scss';

const aiProblems = [
  { title: 'Problem 1000', tags: 'Tag 3', level: 3, problemId: 1000 },
  { title: 'Problem 1004', tags: 'Tag 4', level: 7, problemId: 1004 },
  { title: 'Problem 3000', tags: 'Tag 4', level: 30, problemId: 3000 },
  { title: 'Problem 1004', tags: 'Tag 4', level: 15, problemId: 1004 },
  { title: 'Problem 1004', tags: 'Tag 4', level: 20, problemId: 1004 },
  // 나머지 데이터들
];

const leastCommonTagProblems = [
  { title: 'Problem 1000', tags: 'Tag 3', level: 4, problemId: 1000 },
  { title: 'Problem 1001', tags: 'Tag 3', level: 9, problemId: 1001 },
  { title: 'Problem 1004', tags: 'Tag 4', level: 10, problemId: 1004 },
  { title: 'Problem 1004', tags: 'Tag 4', level: 15, problemId: 1004 },
  { title: 'Problem 1004', tags: 'Tag 4', level: 20, problemId: 1004 },
  // 나머지 데이터들
];

const mostCommonTagProblems = [
  { title: 'Problem 1000', tags: 'Tag 3', level: 5, problemId: 1000 },
  { title: 'Problem 1001', tags: 'Tag 3', level: 10, problemId: 1001 },
  { title: 'Problem 1004', tags: 'Tag 4', level: 10, problemId: 1004 },
  { title: 'Problem 1004', tags: 'Tag 4', level: 25, problemId: 1004 },
  { title: 'Problem 1004', tags: 'Tag 4', level: 30, problemId: 1004 },
  // 나머지 데이터들
];

export function Recommend() {
  return (
    <div className={styles.container}>
      <Section title="AI 추천 문제" problems={aiProblems} />  {/* problems prop을 전달 */}
      <Section title="약점 보완" problems={leastCommonTagProblems} />  {/* problems prop을 전달 */}
      <Section title="강점 강화" problems={mostCommonTagProblems} />  {/* problems prop을 전달 */}
    </div>
  );
}
