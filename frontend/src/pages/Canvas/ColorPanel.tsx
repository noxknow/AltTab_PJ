import CheckIcon from '@/assets/icons/check.svg?react';
import { useEffect, useState } from 'react';
import { fabric } from 'fabric';
import styles from './ColorPanel.module.scss';

type ColorPanelProps = {
  canvas: fabric.Canvas | null;
  className: string;
};

type PenColorTypes =
  | 'red'
  | 'yellow'
  | 'forsythia'
  | 'lightGreen'
  | 'blue'
  | 'black';

const COLOR_CODE = {
  red: '#DF5536',
  yellow: '#F2C947',
  forsythia: '#FCF467',
  lightGreen: '#D3E660',
  blue: '#5099E9',
  black: '#000000',
};

const ColorPanel = ({ canvas, className }: ColorPanelProps) => {
  const [penColor, setPenColor] = useState<PenColorTypes>('black');

  useEffect(() => {
    if (!(canvas instanceof fabric.Canvas)) return;
    canvas.freeDrawingBrush.color = COLOR_CODE[penColor];
  }, [penColor]);

  return (
    <div className={`${className === 'hidden' ? styles.none : styles.block}`}>
      <button
        className={`${styles.red} ${styles.color}`}
        type="button"
        aria-label="빨간색 펜"
        onClick={() => {
          setPenColor('red');
        }}
      >
        <CheckIcon className={`${penColor === 'red' ? '' : styles.hidden}`} />
      </button>
      <button
        className={`${styles.yellow} ${styles.color}`}
        type="button"
        aria-label="노란색 펜"
        onClick={() => {
          setPenColor('yellow');
        }}
      >
        <CheckIcon
          className={`${penColor === 'yellow' ? '' : styles.hidden}`}
        />
      </button>
      <button
        className={`${styles.forsythia} ${styles.color}`}
        type="button"
        aria-label="개나리색 펜"
        onClick={() => {
          setPenColor('forsythia');
        }}
      >
        <CheckIcon
          className={`${penColor === 'forsythia' ? '' : styles.hidden}`}
        />
      </button>
      <button
        className={`${styles.lightGreen} ${styles.color}`}
        type="button"
        aria-label="연두색 펜"
        onClick={() => {
          setPenColor('lightGreen');
        }}
      >
        <CheckIcon
          className={`${penColor === 'lightGreen' ? '' : styles.hidden}`}
        />
      </button>
      <button
        className={`${styles.blue} ${styles.color}`}
        type="button"
        aria-label="파란색 펜"
        onClick={() => {
          setPenColor('blue');
        }}
      >
        <CheckIcon className={`${penColor === 'blue' ? '' : styles.hidden}`} />
      </button>
      <button
        className={`${styles.black} ${styles.color}`}
        type="button"
        aria-label="검은색 펜"
        onClick={() => {
          setPenColor('black');
        }}
      >
        <CheckIcon
          className={`${penColor === 'black' ? 'block' : styles.hidden}`}
        />
      </button>
    </div>
  );
};

export default ColorPanel;
