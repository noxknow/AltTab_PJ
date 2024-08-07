import { fabric } from 'fabric';
import { useState } from 'react';

const useTreeTool = (canvas: fabric.Canvas | null, sendDrawingData: (drawingData: any) => void) => {
  const [currentNode, setCurrentNode] = useState<fabric.Circle | null>(null);
  const [isDrawingTree, setIsDrawingTree] = useState(false);
  const [tempLine, setTempLine] = useState<fabric.Line | null>(null);

  const handleTree = () => {
    if (!canvas) return;

    canvas.isDrawingMode = false;
    canvas.selection = false;
    canvas.defaultCursor = 'crosshair';
    canvas.forEachObject((object) => (object.selectable = false));

    const startDrawingTree = (options: fabric.IEvent) => {
      const pointer = canvas.getPointer(options.e);
      const circle = new fabric.Circle({
        left: pointer.x - 20,
        top: pointer.y - 20,
        radius: 20,
        fill: 'white',
        stroke: 'black',
        strokeWidth: 2,
      });
      canvas.add(circle);
      setCurrentNode(circle);
      setIsDrawingTree(true);
    };
    
    const drawTree = (options: fabric.IEvent) => {
      if (!isDrawingTree || !currentNode) return;
      const pointer = canvas.getPointer(options.e);
      if (tempLine) {
        canvas.remove(tempLine);
      }
      const line = new fabric.Line([
        currentNode.left! + currentNode.radius!,
        currentNode.top! + currentNode.radius!,
        pointer.x,
        pointer.y
      ], {
        stroke: 'black',
        strokeWidth: 2,
      });
      canvas.add(line);
      setTempLine(line);
      canvas.renderAll();
    };
    
    const endDrawingTree = (options: fabric.IEvent) => {
      if (!isDrawingTree) return;
      const pointer = canvas.getPointer(options.e);
      const newNode = new fabric.Circle({
        left: pointer.x - 20,
        top: pointer.y - 20,
        radius: 20,
        fill: 'white',
        stroke: 'black',
        strokeWidth: 2,
      });
      canvas.add(newNode);
      if (currentNode && tempLine) {
        tempLine.set({
          x2: newNode.left! + newNode.radius!,
          y2: newNode.top! + newNode.radius!,
        });
      }
      setIsDrawingTree(false);
      setCurrentNode(null);
      setTempLine(null);
      canvas.renderAll();
      sendDrawingData(canvas.toJSON(['data']));
    };

    canvas.on('mouse:down', startDrawingTree);
    canvas.on('mouse:move', drawTree);
    canvas.on('mouse:up', endDrawingTree);
  };

  return { handleTree };
};

export default useTreeTool;