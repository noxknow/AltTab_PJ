import { fabric } from 'fabric';

const useSelectTool = (canvas: fabric.Canvas | null, sendDrawingData: (drawingData: any) => void) => {
  const handleSelect = () => {
    if (!canvas) return () => {};

    canvas.isDrawingMode = false;
    canvas.selection = true;
    canvas.defaultCursor = 'default';
    canvas.forEachObject((object) => (object.selectable = true));

    const handleObjectModified = () => {
      sendDrawingData(canvas.toJSON(['data']));
    };

    canvas.on('object:modified', handleObjectModified);

    return () => {
      canvas.off('object:modified', handleObjectModified);
    };
  };

  return { handleSelect };
};

export default useSelectTool;
