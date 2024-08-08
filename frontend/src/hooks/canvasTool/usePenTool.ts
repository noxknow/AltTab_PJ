import { useState, useEffect, useCallback } from 'react';
import { fabric } from 'fabric';

const usePenTool = (canvas: fabric.Canvas | null, isActive: boolean) => {
  const [penWidth, setPenWidth] = useState(10);

  const handlePen = useCallback(() => {
    if (!canvas) return () => {};

    console.log('Pen tool activated');
    canvas.isDrawingMode = true;
    canvas.freeDrawingBrush.width = penWidth;
  }, [canvas, penWidth]);

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