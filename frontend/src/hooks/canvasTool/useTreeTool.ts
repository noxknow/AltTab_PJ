import { fabric } from 'fabric';
import { useCallback } from 'react';

const useTreeTool = (canvas: fabric.Canvas | null, sendDrawingData: (drawingData: any) => void, treeDepth: number) => {
  
  const drawTree = useCallback((options: fabric.IEvent) => {
    if (!canvas) return;
    const pointer = canvas.getPointer(options.e);
    const nodeRadius = 20;
    const levelHeight = 80;
    const group = new fabric.Group([], {
      originX: 'center',
      originY: 'top',
      selectable: true,
    });

    const drawNode = (x: number, y: number, level: number) => {
      const circle = new fabric.Circle({
        left: x,
        top: y,
        radius: nodeRadius,
        fill: 'white',
        stroke: 'black',
        strokeWidth: 2,
        originX: 'center',
        originY: 'center',
      });

      group.addWithUpdate(circle);

      if (level < treeDepth) {
        const leftChildX = x - Math.pow(2, treeDepth - level - 1) * nodeRadius * 2;
        const rightChildX = x + Math.pow(2, treeDepth - level - 1) * nodeRadius * 2;
        const childY = y + levelHeight;

        const leftLine = new fabric.Line([
          x, 
          y + nodeRadius,
          leftChildX, 
          childY - nodeRadius
        ], {
          stroke: 'black',
          strokeWidth: 2,
          originX: 'center',
          originY: 'center',
        });
        
        const rightLine = new fabric.Line([
          x, 
          y + nodeRadius,
          rightChildX, 
          childY - nodeRadius
        ], {
          stroke: 'black',
          strokeWidth: 2,
          originX: 'center',
          originY: 'center',
        });
        
        group.addWithUpdate(leftLine);
        group.addWithUpdate(rightLine);

        drawNode(leftChildX, childY, level + 1);
        drawNode(rightChildX, childY, level + 1);
      }
    };

    drawNode(0, 0, 1);
    const groupWidth = Math.pow(2, treeDepth - 1) * nodeRadius * 4;
    const groupHeight = (treeDepth - 1) * levelHeight + nodeRadius * 2;

    group.set({
      left: pointer.x,
      top: pointer.y,
      width: groupWidth,
      height: groupHeight,
    });

    canvas.add(group);
    canvas.renderAll();
    sendDrawingData(canvas.toJSON(['data']));
  }, [canvas, treeDepth, sendDrawingData]);

  const handleTree = useCallback(() => {
    if (!canvas) return () => {};

    canvas.isDrawingMode = false;
    canvas.selection = false;
    canvas.defaultCursor = 'crosshair';
    canvas.forEachObject((object) => (object.selectable = false));
    canvas.on('mouse:down', drawTree);
  }, [canvas, drawTree]);

  return { handleTree };
};

export default useTreeTool;