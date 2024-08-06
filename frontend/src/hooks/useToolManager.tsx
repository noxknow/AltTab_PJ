import { useState, useEffect } from 'react';
import { fabric } from 'fabric';

import useSelectTool from './canvasTool/useSelectTool';
import usePenTool from './canvasTool/usePenTool';
import useEraserTool from './canvasTool/useEraserTool';
import useHandTool from './canvasTool/useHandTool';
import useTreeTool from './canvasTool/useTreeTool';
import useArrayTool from './canvasTool/useArrayTool';

const useToolManager = (canvas: fabric.Canvas | null, sendDrawingData: (drawingData: any) => void) => {
  const [activeTool, setActiveTool] = useState('pen');
  const [arraySize, setArraySize] = useState({ rows: 2, cols: 3 });

  const { handleSelect } = useSelectTool(canvas);
  const { handlePen } = usePenTool(canvas);
  const { handleEraser } = useEraserTool(canvas);
  const { handleHand } = useHandTool(canvas);
  const { handleTree } = useTreeTool(canvas, sendDrawingData);
  const { handleArray } = useArrayTool(canvas, sendDrawingData, arraySize);

  useEffect(() => {
    if (!canvas) return;

    canvas.off('mouse:down');
    canvas.off('mouse:move');
    canvas.off('mouse:up');
    canvas.off('selection:created');

    switch (activeTool) {
      case 'select':
        handleSelect();
        break;
      case 'pen':
        handlePen();
        break;
      case 'eraser':
        handleEraser();
        break;
      case 'hand':
        handleHand();
        break;
      case 'tree':
        handleTree();
        break;
      case 'array':
        handleArray();
        break;
    }
  }, [activeTool, arraySize, canvas]);

  const handleArraySizeChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setArraySize(prev => ({ ...prev, [name]: parseInt(value) || 0 }));
  };

  return {
    activeTool,
    setActiveTool,
    arraySize,
    handleArraySizeChange,
  };
};

export default useToolManager;