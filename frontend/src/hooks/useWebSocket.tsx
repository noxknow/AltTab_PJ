import { useRef, useEffect, useState } from 'react';
import SockJS from 'sockjs-client';

import { Client } from '@stomp/stompjs';
import { compressData, decompressData } from '@/utils/CompressUtil';
import { socketURL } from '@/services/api';
import { fabric } from 'fabric';

const useWebSocket = (studyId: string | undefined, problemId: string | undefined, canvas: fabric.Canvas | null) => {
  const stompClient = useRef<Client | null>(null);
  const reconnectAttemptsRef = useRef(0);
  const maxReconnectAttempts = 5;
  const [pendingDrawingData, setPendingDrawingData] = useState<any>(null);

  useEffect(() => {
    connect();
    return () => disconnect();
  }, [studyId, problemId]);

  useEffect(() => {
    if (canvas && pendingDrawingData) {
      applyDrawingData(pendingDrawingData);
      setPendingDrawingData(null);
    }
  }, [canvas, pendingDrawingData]);

  const connect = () => {
    if (stompClient.current && stompClient.current.connected) return;

    const socket = new SockJS(`${socketURL}`);
    stompClient.current = new Client({
      webSocketFactory: () => socket as any,
      reconnectDelay: 1000,
      heartbeatIncoming: 2000,
      heartbeatOutgoing: 2000,
    });

    stompClient.current.onConnect = () => {
      if (stompClient.current) {
        stompClient.current.subscribe(
          `/sub/api/v1/rooms/${studyId}/${problemId}`,
          (message) => {
            const newMessage = JSON.parse(message.body);
            updateCanvas(newMessage);
          },
        );
      }``
      reconnectAttemptsRef.current = 0;
    };

    stompClient.current.onStompError = (frame) => {
      console.error('Broker reported error: ' + frame.headers['message']);
      console.error('Additional details: ' + frame.body);
    };

    stompClient.current.onWebSocketClose = () => {
      console.log("Socket Closed");
      if (reconnectAttemptsRef.current < maxReconnectAttempts) {
        reconnectAttemptsRef.current += 1;
        setTimeout(
          () => {
            connect();
          },
          Math.min(5000 * reconnectAttemptsRef.current, 60000),
        );
      } else {
        console.log('Max reconnect attempts reached.');
      }
    };

    stompClient.current.activate();
  };

  const disconnect = () => {
    if (stompClient.current) {
      stompClient.current.deactivate();
    }
  };

  const sendDrawingData = (drawingData: any) => {
    if (!stompClient.current?.connected) return;

    try {
      const compressedData = compressData(JSON.stringify(drawingData));
      
      const payload = {
        studyId,
        problemId,
        drawingData: compressedData,
      };

      stompClient.current.publish({
        destination: `/pub/api/v1/rooms/${studyId}/${problemId}`,
        body: JSON.stringify(payload),
      });
    } catch (error) {
      console.error('Error compressing data:', error);
    }
  };

  const updateCanvas = (newMessage: any) => {
    try {
      const decompressedData = decompressData(newMessage.drawingData);
      const drawingData = JSON.parse(decompressedData);
      
      if (canvas) {
        applyDrawingData(drawingData);
      } else {
        setPendingDrawingData(drawingData);
      }
    } catch (error) {
      console.error('Error decompressing or parsing data:', error);
    }
  };

  const applyDrawingData = async (drawingData: any) => {
    if (!canvas) return;

    try {
      if (Array.isArray(drawingData.objects)) {
        canvas.clear();
        for (const obj of drawingData.objects) {
          await new Promise<void>((resolve) => {
            fabric.util.enlivenObjects(
              [obj],
              (enlivenedObjects: fabric.Object[]) => {
                enlivenedObjects.forEach((enlivenedObj) => {
                  canvas.add(enlivenedObj);
                });
                resolve();
              },
              'fabric',
            );
          });
        }
        canvas.renderAll();
      }
    } catch (error) {
      console.error('Error updating canvas:', error);
    }
  };

  return { sendDrawingData };
};

export default useWebSocket;