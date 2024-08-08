import { useState, useEffect, useCallback } from 'react';
import { fabric } from 'fabric';

const usePenTool = (canvas: fabric.Canvas | null, isActive: boolean, sendDrawingData: (drawingData: any) => void) => {
  const [penWidth, setPenWidth] = useState(10);

  const handlePen = useCallback(() => {
    if (!canvas) return () => {};

    canvas.isDrawingMode = true;
    canvas.freeDrawingBrush.width = penWidth;

    canvas.off('path:created');

    const onPathCreated = () => {
      sendDrawingData(canvas.toJSON(['data']));
    };
    canvas.on('path:created', onPathCreated);

    return () => {
      canvas.off('path:created', onPathCreated);
    };
  }, [canvas, penWidth, sendDrawingData]);

  useEffect(() => {
    if (isActive) {
      handlePen();
    }

    return () => {
      if (canvas) {
        canvas.isDrawingMode = false;
      }
    };
  }, [isActive, canvas, handlePen]);

  const changePenWidth = useCallback((width: number) => {
    setPenWidth(width);
    if (canvas) {
      canvas.freeDrawingBrush.width = width;
    }
  }, [canvas]);

  return { handlePen, penWidth, changePenWidth };
};

export default usePenTool;