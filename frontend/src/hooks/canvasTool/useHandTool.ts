import { fabric } from 'fabric';

const useHandTool = (canvas: fabric.Canvas | null) => {
  const handleHand = () => {
    if (!canvas) return;

    canvas.defaultCursor = 'move';

    let panning = false;
    const handleMouseDown = () => {
      panning = true;
    };

    const handleMouseMove = (event: fabric.IEvent<MouseEvent>) => {
      if (panning) {
        const delta = new fabric.Point(event.e.movementX, event.e.movementY);
        canvas.relativePan(delta);
      }
    };

    const handleMouseUp = () => {
      panning = false;
    };

    canvas.on('mouse:down', handleMouseDown);
    canvas.on('mouse:move', handleMouseMove);
    canvas.on('mouse:up', handleMouseUp);
  };

  return { handleHand };
};

export default useHandTool;