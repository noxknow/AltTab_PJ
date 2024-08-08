import { fabric } from 'fabric';

const useSelectTool = (canvas: fabric.Canvas | null) => {
  const handleSelect = () => {
    if (!canvas) return () => {};

    canvas.isDrawingMode = false;
    canvas.selection = true;
    canvas.defaultCursor = 'default';
    canvas.forEachObject((object) => (object.selectable = true));
  };

  return { handleSelect };
};

export default useSelectTool;