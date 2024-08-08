import { useState, useEffect } from 'react';
import { fabric } from 'fabric';

import useSelectTool from './canvasTool/useSelectTool';
import usePenTool from './canvasTool/usePenTool';
import useEraserTool from './canvasTool/useEraserTool';
import useHandTool from './canvasTool/useHandTool';
import useTreeTool from './canvasTool/useTreeTool';
import useTableTool from './canvasTool/useTableTool';

const useToolManager = (canvas: fabric.Canvas | null, sendDrawingData: (drawingData: any) => void) => {
  const [activeTool, setActiveTool] = useState('pen');
  const [arraySize, setArraySize] = useState({ rows: 2, cols: 3 });
  const [treeDepth, setTreeDepth] = useState(3);

  const { handleSelect } = useSelectTool(canvas);
  const { handlePen } = usePenTool(canvas, activeTool === 'pen', sendDrawingData);
  const { handleEraser } = useEraserTool(canvas);
  const { handleHand } = useHandTool(canvas);
  const { handleTree } = useTreeTool(canvas, sendDrawingData, treeDepth);
  const { handleTable } = useTableTool(canvas, sendDrawingData, arraySize);

  const handleClose = () => {
    if (!canvas) return () => {};

    canvas.clear();
  };

  useEffect(() => {
    if (!canvas) return () => {};

    let cleanup: (() => void) | undefined;

    canvas.off('mouse:down');
    canvas.off('mouse:move');
    canvas.off('mouse:up');
    canvas.off('selection:created');

    canvas.isDrawingMode = false;
    canvas.selection = true;
    canvas.defaultCursor = 'default';
    canvas.forEachObject((object) => (object.selectable = true));

    switch (activeTool) {
      case 'select':
        cleanup = handleSelect();
        break;
      case 'pen':
        canvas.isDrawingMode = true;
        cleanup = handlePen();
        break;
      case 'eraser':
        cleanup = handleEraser();
        break;
      case 'hand':
        cleanup = handleHand();
        break;
      case 'tree':
        cleanup = handleTree();
        break;
      case 'table':
        cleanup = handleTable();
        break;
      case 'close':
        handleClose();
        break;
    }
    
    return () => {
      canvas.off('mouse:down');
      canvas.off('mouse:move');
      canvas.off('mouse:up');
      canvas.off('selection:created');
    };
  }, [activeTool, arraySize, canvas]);

  const handleTableSizeChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setArraySize(prev => ({ ...prev, [name]: parseInt(value) || 0 }));
  };

  const handleTreeDepthChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setTreeDepth(parseInt(e.target.value) || 1);
  };

  return {
    activeTool,
    setActiveTool,
    arraySize,
    treeDepth,
    handleTreeDepthChange,
    handleTableSizeChange,
  };
};

export default useToolManager;