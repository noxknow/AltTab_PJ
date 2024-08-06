import { useEffect, useState } from 'react';
import { fabric } from 'fabric';

import CheckIcon from '@/assets/icons/check.svg?react';

import styles from './ColorPanel.module.scss';

type ColorPanelProps = {
  canvas: fabric.Canvas | null;
  className: string;
  penWidth: number;
  changePenWidth: (width: number) => void;
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

const ColorPanel = ({ canvas, className, penWidth, changePenWidth }: ColorPanelProps) => {
  const [penColor, setPenColor] = useState<PenColorTypes>('black');

  useEffect(() => {
    if (!(canvas instanceof fabric.Canvas)) return;
    canvas.freeDrawingBrush.color = COLOR_CODE[penColor];
  }, [penColor]);

  return (
    <div className={`${styles.colorPanel} ${className === 'hidden' ? styles.none : styles.block}`}>
      <div className={styles.colorButtons}>
        {Object.entries(COLOR_CODE).map(([color, code]) => (
          <button
            key={color}
            className={`${styles.colorButton} ${styles[color]} ${penColor === color ? styles.active : ''}`}
            type="button"
            aria-label={`${color} 펜`}
            onClick={() => setPenColor(color as PenColorTypes)}
          >
            <CheckIcon className={`${penColor === color ? '' : styles.hidden}`} />
          </button>
        ))}
      </div>
      <div className={styles.penWidthControl}>
        <input
          type="range"
          min="1"
          max="50"
          value={penWidth}
          onChange={(e) => changePenWidth(Number(e.target.value))}
        />
        <div className={styles.penWidthPreview} style={{ height: `${penWidth}px` }} />
      </div>
    </div>
  );
};

export default ColorPanel;
