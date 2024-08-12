import { fabric } from 'fabric';

import EraserCursor from '@/assets/icons/eraserMouseCursor.svg?react';

const useEraserTool = (canvas: fabric.Canvas | null, sendDrawingData: (drawingData: any) => void) => {
  const handleEraser = () => {
    if (!canvas) return () => {};

    canvas.isDrawingMode = false;
    canvas.selection = true;
    canvas.defaultCursor = `url("${EraserCursor}"), auto`;
    canvas.forEachObject((object) => (object.selectable = true));

    const handleMouseUp = (target: fabric.Object | undefined) => {
      if (!target) return;
      canvas.remove(target);
      sendDrawingData(canvas.toJSON(['data']));
    };

    const handleSelectionCreated = (selected: fabric.Object[] | undefined) => {
      selected?.forEach((object) => canvas.remove(object));
      canvas.discardActiveObject().renderAll();
      sendDrawingData(canvas.toJSON(['data']));
    };

    canvas.on('mouse:up', ({ target }) => handleMouseUp(target));
    canvas.on('selection:created', ({ selected }) => handleSelectionCreated(selected));
  };

  return { handleEraser };
};

export default useEraserTool;
