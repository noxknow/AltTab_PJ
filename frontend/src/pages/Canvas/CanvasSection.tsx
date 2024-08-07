import { useEffect, useRef, useState } from 'react';
import { useParams } from 'react-router-dom';
import { fabric } from 'fabric';

import CloseSVG from '@/assets/icons/close.svg?react';
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

  return (
    <div>
      <div className={styles.canvas} ref={canvasContainerRef}>
        <button className={styles.closeButton} onClick={closeCanvas}>
          <CloseSVG width={24} height={24} stroke="#F24242" />
        </button>
        <canvas ref={canvasRef} />
        <Toolbar canvas={canvas} sendDrawingData={sendDrawingData} />
      </div>
    </div>
  );
}