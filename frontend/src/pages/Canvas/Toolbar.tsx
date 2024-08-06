import React from 'react';

import MouseIcon from '@/assets/icons/mouse.svg?react';
import PenIcon from '@/assets/icons/pen.svg?react';
import EraserIcon from '@/assets/icons/eraser.svg?react';
import HandIcon from '@/assets/icons/hand.svg?react';
import TableIcon from '@/assets/icons/table.svg?react';
import useToolManager from '@/hooks/useToolManager';

import ToolButton from './ToolButton';
import ColorPanel from './ColorPanel';
import styles from './Toolbar.module.scss';

type ToolbarProps = {
  canvas: fabric.Canvas | null;
  sendDrawingData: (drawingData: any) => void;
};

const Toolbar: React.FC<ToolbarProps> = ({ canvas, sendDrawingData }) => {
  const {
    activeTool,
    setActiveTool,
    arraySize,
    handleArraySizeChange
  } = useToolManager(canvas, sendDrawingData);

  return (
    <div className={styles.toolbar}>
      <ToolButton
        icon={MouseIcon}
        onClick={() => setActiveTool('select')}
        disabled={activeTool === 'select'}
        title="Select Tool"
      />
      <ToolButton
        icon={PenIcon}
        onClick={() => setActiveTool('pen')}
        disabled={activeTool === 'pen'}
        title="Pen Tool"
      />
      <ColorPanel
        canvas={canvas}
        className={`${activeTool === 'pen' ? 'block' : 'hidden'}`}
      />
      <ToolButton
        icon={EraserIcon}
        onClick={() => setActiveTool('eraser')}
        disabled={activeTool === 'eraser'}
        title="Eraser Tool"
      />
      <ToolButton
        icon={HandIcon}
        onClick={() => setActiveTool('hand')}
        disabled={activeTool === 'hand'}
        title="Hand Tool"
      />
      <ToolButton
        icon={EraserIcon}
        onClick={() => setActiveTool('tree')}
        disabled={activeTool === 'tree'}
        title="Tree Tool"
      />
      <ToolButton
        icon={TableIcon}
        onClick={() => setActiveTool('array')}
        disabled={activeTool === 'array'}
        title="Array Tool"
      />
      {activeTool === 'array' && (
        <div className={styles.arraySizeInputs}>
          <input
            type="number"
            name="rows"
            value={arraySize.rows}
            onChange={handleArraySizeChange}
            min="1"
            max="10"
          />
          <span>x</span>
          <input
            type="number"
            name="cols"
            value={arraySize.cols}
            onChange={handleArraySizeChange}
            min="1"
            max="10"
          />
        </div>
      )}
    </div>
  );
};

export default Toolbar;