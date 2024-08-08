import { useEffect, useRef, useState } from 'react';
import { useParams } from 'react-router-dom';
import { fabric } from 'fabric';

import useWebSocket from '@/hooks/useWebSocket';
import useCanvas from '@/hooks/canvasTool/useCanvas';

import Toolbar from './Toolbar';
import styles from './CanvasSection.module.scss';

type CanvasProps = {
  closeCanvas?: () => void;
};

export function CanvasSection({ closeCanvas }: CanvasProps) {
  const { studyId, problemId } = useParams();
  const canvasContainerRef = useRef<HTMLDivElement>(null);
  const canvasRef = useRef<HTMLCanvasElement>(null);
  const [canvas, setCanvas] = useState<fabric.Canvas | null>(null);
  const [isCanvasVisible, setIsCanvasVisible] = useState(true);

  const { sendDrawingData } = useWebSocket(studyId, problemId, canvas);
  const { initializeCanvas, handleCanvasEvents } = useCanvas(sendDrawingData);

  useEffect(() => {
    if (!canvasContainerRef.current || !canvasRef.current) return;

    const newCanvas = initializeCanvas(canvasRef.current, canvasContainerRef.current);
    setCanvas(newCanvas);

    return () => {
      newCanvas.dispose();
    };
  }, []);

  useEffect(() => {
    if (!canvas) return;
    handleCanvasEvents(canvas);
  }, [canvas]);

  const handleCloseCanvas = () => {
    setIsCanvasVisible(false);
    if (closeCanvas) {
      closeCanvas();
    }
  };

  if (!isCanvasVisible) return null;

  return (
    <div>
      <div className={styles.canvas} ref={canvasContainerRef}>
        <canvas ref={canvasRef} />
        <Toolbar 
          canvas={canvas} 
          sendDrawingData={sendDrawingData} 
          closeCanvas={handleCloseCanvas}
        />
      </div>
    </div>
  );
}