import MouseIcon from '@/assets/icons/mouse.svg?react';
import PenIcon from '@/assets/icons/pen.svg?react';
import EraserIcon from '@/assets/icons/eraser.svg?react';
import HandIcon from '@/assets/icons/hand.svg?react';
import EraserCursor from '@/assets/icons/eraserMouseCursor.svg?react';
import { fabric } from 'fabric';
import { useEffect, useState } from 'react';
import ToolButton from './ToolButton';
import ColorPanel from './ColorPanel';
import styles from './Toolbar.module.scss';

type ToolbarProps = {
  canvas: fabric.Canvas | null;
};

const Toolbar = ({ canvas }: ToolbarProps) => {
  const [activeTool, setActiveTool] = useState('pen');

  /**
   * @description 화이트 보드에 그려져 있는 요소들을 클릭을 통해 선택 가능한지 여부를 제어하기 위한 함수입니다.
   */
  const setIsObjectSelectable = (isSelectable: boolean) => {
    if (!(canvas instanceof fabric.Canvas)) return;
    canvas.forEachObject((object) => (object.selectable = isSelectable));
  };

  /**
   * @description 캔버스의 옵션을 리셋하는 함수입니다.
   * @description 그래픽 요소 선택 기능: off, 드로잉 모드: off, 드래그 블럭지정모드: off, 커서: 디폴트 포인터
   */
  const resetCanvasOption = () => {
    if (!(canvas instanceof fabric.Canvas)) return;
    setIsObjectSelectable(false);
    canvas.isDrawingMode = false;
    canvas.selection = false;
    canvas.defaultCursor = 'default';
  };

  const handleSelect = () => {
    if (!(canvas instanceof fabric.Canvas)) return;

    setIsObjectSelectable(true);
    canvas.selection = true;
    canvas.defaultCursor = 'default';
  };

  const handlePen = () => {
    if (!(canvas instanceof fabric.Canvas)) return;

    canvas.freeDrawingBrush.width = 10;
    canvas.isDrawingMode = true;
  };

  const handleEraser = () => {
    if (!(canvas instanceof fabric.Canvas)) return;

    setIsObjectSelectable(true);
    canvas.selection = true;

    canvas.defaultCursor = `url("${EraserCursor}"), auto`;

    const handleMouseUp = (target: fabric.Object | undefined) => {
      if (!target) return;
      canvas.remove(target);
    };

    const handleSelectionCreated = (selected: fabric.Object[] | undefined) => {
      if (activeTool === 'eraser') {
        selected?.forEach((object) => canvas.remove(object));
      }
      canvas.discardActiveObject().renderAll();
    };

    canvas.on('mouse:up', ({ target }) => handleMouseUp(target));

    canvas.on('selection:created', ({ selected }) =>
      handleSelectionCreated(selected),
    );
  };

  const handleHand = () => {
    if (!(canvas instanceof fabric.Canvas)) return;

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

  useEffect(() => {
    if (!(canvas instanceof fabric.Canvas)) return;
    canvas.off('mouse:down');
    canvas.off('mouse:move');
    canvas.off('mouse:up');
    canvas.off('selection:created');

    resetCanvasOption();

    switch (activeTool) {
      case 'select':
        handleSelect();
        break;

      case 'pen':
        handlePen();
        break;

      case 'eraser':
        handleEraser();
        break;

      case 'hand':
        handleHand();
        break;
    }
  }, [activeTool]);

  return (
    <div className={styles.toolbar}>
      <ToolButton
        icon={MouseIcon}
        onClick={() => {
          setActiveTool('select');
        }}
        disabled={activeTool === 'select'}
        title="Select Tool"
      />
      <ToolButton
        icon={PenIcon}
        onClick={() => {
          setActiveTool('pen');
        }}
        disabled={activeTool === 'pen'}
        title="Pen Tool"
      />

      <ColorPanel
        canvas={canvas}
        className={`${activeTool === 'pen' ? 'block' : 'hidden'}`}
      />

      <ToolButton
        icon={EraserIcon}
        onClick={() => {
          setActiveTool('eraser');
        }}
        disabled={activeTool === 'eraser'}
        title="Eraser Tool"
      />
      <ToolButton
        icon={HandIcon}
        onClick={() => {
          setActiveTool('hand');
        }}
        disabled={activeTool === 'hand'}
        title="Hand Tool"
      />
    </div>
  );
};

export default Toolbar;
