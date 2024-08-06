import { fabric } from 'fabric';

const usePenTool = (canvas: fabric.Canvas | null) => {
  const handlePen = () => {
    if (!canvas) return;

    canvas.isDrawingMode = true;
    canvas.freeDrawingBrush.width = 10;
  };

  return { handlePen };
};

export default usePenTool;