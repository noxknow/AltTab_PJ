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

      const totalWidth = arraySize.cols * cellWidth;
      const totalHeight = arraySize.rows * cellHeight

      const group = new fabric.Group([], {
        originX: 'center',
        originY: 'center',
        selectable: true,
      });

      for (let i = 0; i < arraySize.rows; i++) {
        for (let j = 0; j < arraySize.cols; j++) {
          const rect = new fabric.Rect({
            left: j * cellWidth - totalWidth / 2,
            top: i * cellHeight - totalHeight / 2,
            width: cellWidth,
            height: cellHeight,
            fill: 'white',
            stroke: 'black',
            strokeWidth: 1,
            selectable: false,
          });
          
          group.addWithUpdate(rect);
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