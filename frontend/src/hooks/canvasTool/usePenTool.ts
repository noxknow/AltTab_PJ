import { fabric } from 'fabric';
import { useState } from 'react';

const usePenTool = (canvas: fabric.Canvas | null) => {
  const [penWidth, setPenWidth] = useState(10);

  const handlePen = () => {
    if (!canvas) return;

    canvas.isDrawingMode = true;
    canvas.freeDrawingBrush.width = penWidth;
  };

  const changePenWidth = (width: number) => {
    setPenWidth(width);
    if (canvas) {
      canvas.freeDrawingBrush.width = width;
    }
  };

  return { handlePen, penWidth, changePenWidth };
};

export default usePenTool;