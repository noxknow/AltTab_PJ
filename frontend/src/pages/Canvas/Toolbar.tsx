import React, { useState } from 'react';

import MouseIcon from '@/assets/icons/mouse.svg?react';
import PenIcon from '@/assets/icons/pen.svg?react';
import EraserIcon from '@/assets/icons/eraser.svg?react';
import HandIcon from '@/assets/icons/hand.svg?react';
import TableIcon from '@/assets/icons/table.svg?react';
import TreeIcon from '@/assets/icons/tree.svg?react';
import CloseDrawIcon from '@/assets/icons/closeDraw.svg?react';
import useToolManager from '@/hooks/useToolManager';
import usePenTool from '@/hooks/canvasTool/usePenTool';
import { useCompilerModalState } from '@/hooks/useCompilerState';

import ToolButton from './ToolButton';
import ColorPanel from './ColorPanel';
import styles from './Toolbar.module.scss';

type ToolbarProps = {
  canvas: fabric.Canvas | null;
  sendDrawingData: (drawingData: any) => void;
  closeCanvas: () => void;
};

const Toolbar: React.FC<ToolbarProps> = ({ canvas, sendDrawingData, closeCanvas }) => {
  const [isToolbarVisible, setIsToolbarVisible] = useState(true);
  
  const {
    activeTool,
    setActiveTool,
    arraySize,
    treeDepth,
    handleTreeDepthChange,
    handleTableSizeChange
  } = useToolManager(canvas, sendDrawingData);

  const { penWidth, changePenWidth } = usePenTool(canvas);
  const { setIsModalOpen } = useCompilerModalState();

  const handleClose = () => {
    setActiveTool('close');
    setIsModalOpen(false);
    setIsToolbarVisible(false);
    closeCanvas();
  };

  if (!isToolbarVisible) return null;

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
        penWidth={penWidth}
        changePenWidth={changePenWidth}
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
        icon={TreeIcon}
        onClick={() => setActiveTool('tree')}
        disabled={activeTool === 'tree'}
        title="Tree Tool"
      />
      {activeTool === 'tree' && (
        <div className={styles.treeDepthInput}>
          <input
            type="number"
            name="treeDepth"
            value={treeDepth}
            onChange={handleTreeDepthChange}
            min="1"
            max="5"
          />
        </div>
      )}
      <ToolButton
        icon={TableIcon}
        onClick={() => setActiveTool('table')}
        disabled={activeTool === 'table'}
        title="Table Tool"
      />
      {activeTool === 'table' && (
        <div className={styles.arraySizeInputs}>
          <input
            type="number"
            name="rows"
            value={arraySize.rows}
            onChange={handleTableSizeChange}
            min="1"
            max="10"
          />
          <span>x</span>
          <input
            type="number"
            name="cols"
            value={arraySize.cols}
            onChange={handleTableSizeChange}
            min="1"
            max="10"
          />
        </div>
      )}
      <ToolButton
        icon={CloseDrawIcon}
        onClick={handleClose}
        disabled={false}
        title="Close Modal"
      />
    </div>
  );
};

export default Toolbar;