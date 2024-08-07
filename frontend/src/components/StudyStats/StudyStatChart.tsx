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

export default function StudyStatChart() {
  ChartJS.register(
    PolarAreaController,
    RadialLinearScale,
    PointElement,
    LineElement,
    ArcElement,
    Legend,
  );

  const data = {
    labels: ['Red', 'Green', 'Yellow', 'Grey', 'Blue'],
    datasets: [
      {
        label: 'My First Dataset',
        data: [11, 16, 7, 3, 14],
        backgroundColor: [
          'rgb(255, 99, 132)',
          'rgb(75, 192, 192)',
          'rgb(255, 205, 86)',
          'rgb(201, 203, 207)',
          'rgb(54, 162, 235)',
        ],
      },
    ],
  };
  const options = {
    responsive: true,
    plugins: {
      legend: {
        display: true,
        position: 'right' as 'right',
      },
    },
  };

  return (
    <div className={styles.main}>
      <Chart type="polarArea" data={data} options={options} />
    </div>
  );
}
