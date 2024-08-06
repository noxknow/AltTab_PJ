import { fabric } from 'fabric';

const useArrayTool = (canvas: fabric.Canvas | null, sendDrawingData: (drawingData: any) => void, arraySize: { rows: number, cols: number }) => {
  const handleArray = () => {
    if (!canvas) return;

    canvas.isDrawingMode = false;
    canvas.selection = false;
    canvas.defaultCursor = 'crosshair';
    canvas.forEachObject((object) => (object.selectable = false));

    const drawArray = (options: fabric.IEvent) => {
      const pointer = canvas.getPointer(options.e);
      
      const cellWidth = 50;
      const cellHeight = 50;
      const padding = 5;
      
      const group = new fabric.Group([], {
        left: pointer.x,
        top: pointer.y,
      });

      for (let i = 0; i < arraySize.rows; i++) {
        for (let j = 0; j < arraySize.cols; j++) {
          const rect = new fabric.Rect({
            left: j * (cellWidth + padding),
            top: i * (cellHeight + padding),
            width: cellWidth,
            height: cellHeight,
            fill: 'white',
            stroke: 'black',
            strokeWidth: 1,
          });

          const text = new fabric.Text(`[${i}][${j}]`, {
            left: j * (cellWidth + padding) + cellWidth / 2,
            top: i * (cellHeight + padding) + cellHeight / 2,
            fontSize: 14,
            originX: 'center',
            originY: 'center',
          });

          group.addWithUpdate(rect);
          group.addWithUpdate(text);
        }
      }

      canvas.add(group);
      canvas.renderAll();
      sendDrawingData(canvas.toJSON(['data']));
    };

    canvas.on('mouse:down', drawArray);
  };

  return { handleArray };
};

export default useArrayTool;