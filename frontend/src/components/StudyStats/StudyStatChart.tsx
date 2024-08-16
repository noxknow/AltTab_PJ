import styles from './StudyStatChart.module.scss';
import {
  Chart as ChartJS,
  PolarAreaController,
  RadialLinearScale,
  PointElement,
  LineElement,
  ArcElement,
  Legend,
} from 'chart.js/auto';
import { Chart } from 'react-chartjs-2';

import { useGetStudyScoreQuery } from '@/queries/study';

export function StudyStatChart({ studyId }: { studyId: string }) {
  ChartJS.register(
    PolarAreaController,
    RadialLinearScale,
    PointElement,
    LineElement,
    ArcElement,
    Legend,
  );

  const { data: scoreData, isLoading } = useGetStudyScoreQuery(studyId);

  if (isLoading) {
    return <div>Loading...</div>;
  }

  const data = {
    labels: [
      '수학',
      'DP',
      '자료 구조',
      '구현',
      '그래프 이론',
      '탐색',
      '정렬',
      '문자열',
    ],
    datasets: [
      {
        label: 'Problem Tags',
        data: scoreData?.tagCount || [],
        backgroundColor: [
          'rgb(255, 99, 132)',
          'rgb(75, 192, 192)',
          'rgb(255, 205, 86)',
          'rgb(201, 203, 207)',
          'rgb(54, 162, 235)',
          'rgb(153, 102, 255)',
          'rgb(255, 159, 64)',
          'rgb(59, 220, 159)',
        ],
      },
    ],
  };

  const options = {
    responsive: true,
    plugins: {
      legend: {
        display: true,
        position: 'right' as const,
      },
    },
  };

  return (
    <div className={styles.main}>
      <Chart type="polarArea" data={data} options={options} />
    </div>
  );
}
