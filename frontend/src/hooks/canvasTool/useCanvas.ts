import { fabric } from 'fabric';

const useCanvas = (sendDrawingData: (drawingData: any) => void) => {
  
  const initializeCanvas = (canvasElement: HTMLCanvasElement, container: HTMLDivElement) => {
    const newCanvas = new fabric.Canvas(canvasElement, {
      width: container.offsetWidth,
      height: container.offsetHeight,
    });

    newCanvas.backgroundColor = 'transparent';
    newCanvas.freeDrawingBrush.width = 10;
    newCanvas.isDrawingMode = true;

    return newCanvas;
  };

  const handleCanvasEvents = (canvas: fabric.Canvas) => {
    canvas.on('mouse:wheel', (opt) => {
      const delta = opt.e.deltaY;
      let zoom = canvas.getZoom();
      zoom *= 0.999 ** delta;
      if (zoom > 10) zoom = 10;
      if (zoom < 0.1) zoom = 0.1;
      canvas.zoomToPoint({ x: opt.e.offsetX, y: opt.e.offsetY }, zoom);
      opt.e.preventDefault();
      opt.e.stopPropagation();
    });

    canvas.on('mouse:up', () => {
      sendDrawingData(canvas.toJSON(['data']));
    });

    const handleResize = () => {
			const element = canvas.getElement();
      const parent = element.parentElement;
      if (parent) {
        canvas.setDimensions({
          width: parent.offsetWidth,
          height: parent.offsetHeight,
        });
      }
    };

    window.addEventListener('resize', handleResize);

    return () => {
      window.removeEventListener('resize', handleResize);
    };
  };

  return { initializeCanvas, handleCanvasEvents };
};

export default useCanvas;