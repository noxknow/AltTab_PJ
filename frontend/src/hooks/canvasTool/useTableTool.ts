import { fabric } from 'fabric';

const useTableTool = (canvas: fabric.Canvas | null, sendDrawingData: (drawingData: any) => void, arraySize: { rows: number, cols: number }) => {
  
  const handleTable = () => {
    if (!canvas) return;

    canvas.isDrawingMode = false;
    canvas.selection = false;
    canvas.defaultCursor = 'crosshair';
    canvas.forEachObject((object) => (object.selectable = false));

    const drawArray = (options: fabric.IEvent) => {
      if (!canvas) return;

      const pointer = canvas.getPointer(options.e);

      const cellWidth = 55;
      const cellHeight = 55;
      const padding = 5;

      const totalWidth = arraySize.cols * (cellWidth + padding) - padding;
      const totalHeight = arraySize.rows * (cellHeight + padding) - padding;

      const group = new fabric.Group([], {
        originX: 'center',
        originY: 'center',
        selectable: true,
      });

      for (let i = 0; i < arraySize.rows; i++) {
        for (let j = 0; j < arraySize.cols; j++) {
          const rect = new fabric.Rect({
            left: j * (cellWidth + padding) - totalWidth / 2,
            top: i * (cellHeight + padding) - totalHeight / 2,
            width: cellWidth,
            height: cellHeight,
            fill: 'white',
            stroke: 'black',
            strokeWidth: 1,
            selectable: false,
          });

          const text = new fabric.Text(`[${i}][${j}]`, {
            left: j * (cellWidth + padding) - totalWidth / 2 + cellWidth / 2,
            top: i * (cellHeight + padding) - totalHeight / 2 + cellHeight / 2,
            fontSize: 14,
            originX: 'center',
            originY: 'center',
            selectable: false,
          });

          group.addWithUpdate(rect);
          group.addWithUpdate(text);
        }
      }

      group.setPositionByOrigin(new fabric.Point(pointer.x, pointer.y), 'center', 'center');

      canvas.add(group);
      canvas.renderAll();

      sendDrawingData(canvas.toJSON(['data']));
    };

    canvas.on('mouse:down', drawArray);
  };

  return { handleTable };
};

export default useTableTool;