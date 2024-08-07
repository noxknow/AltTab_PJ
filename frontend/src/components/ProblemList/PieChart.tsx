import styles from './PieChart.module.scss';
import { Chart as ChartJS, ArcElement } from 'chart.js';
import { Chart } from 'react-chartjs-2';

type PieChartProps = {
  percentage: number;
};

export default function PieChart({ percentage }: PieChartProps) {
  ChartJS.register(ArcElement);

  const data = {
    labels: [],
    datasets: [
      {
        data: [percentage, 100 - percentage],
        backgroundColor: ['#24DB24', '#D4F8D3'],
      },
    ],
  };
  const options = {
    responsive: true,
    plugins: {
      legend: {
        display: false,
      },
      tooltip: {
        enabled: false,
      },
    },
  };

  return (
    <div className={styles.main}>
      <Chart type="doughnut" data={data} options={options} />
    </div>
  );
}
