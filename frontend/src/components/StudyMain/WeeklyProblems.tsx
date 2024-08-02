import { ProblemIcon } from '../ProblemIcon/ProblemIcon';
import styles from './WeeklyProblems.module.scss';

export default function WeeklyProblems() {
  return (
    <div className={styles.main}>
      <div className={styles.header}>
        <div>
          <ProblemIcon />
        </div>
        <div>이 주의 문제</div>
      </div>
      <div className={styles.table}>
        <table>
          <thead>
            <tr>
              <th>문제 제목</th>
              <th>담당자</th>
              <th>난이도</th>
              <th>Progress</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>구슬 탈출 2</td>
              <td>이재영</td>
              <td>G4</td>
              <td>50</td>
            </tr>
            <tr>
              <td>불</td>
              <td>이치왕</td>
              <td>G3</td>
              <td>75</td>
            </tr>
            <tr>
              <td>미세먼지 안녕!</td>
              <td>이지원</td>
              <td>G2</td>
              <td>100</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  );
}
