import { useState, useEffect, useRef, useCallback } from 'react';
import { useParams } from 'react-router-dom';
import { v4 } from 'uuid';
import { EditorBlock } from './EditorBlock';
import styles from './EditorPage.module.scss';
import {
  DragDropContext,
  Draggable,
  Droppable,
  DropResult,
} from '@hello-pangea/dnd';
import ReactDOM from 'react-dom';
import { useCreateBlocksQuery, useGetBlocksQuery } from '@/queries/solutions';
import { Block } from '@/types/solution';
import { useGetMyInfoQuery } from '@/queries/member';

export function EditorPage() {
  const initialBlock = { id: v4(), text: '', option: 'content' };
  const [blocks, setBlocks] = useState<Block[]>([initialBlock]);
  const isAdd = useRef(true);
  const isChanged = useRef(false);
  const index = useRef(-1);
  const { studyId, problemId } = useParams();
  const { refetch } = useGetBlocksQuery(studyId!, problemId!);
  const { data: userInfo } = useGetMyInfoQuery();

  const createBlocksMutation = useCreateBlocksQuery(studyId!, problemId!, {
    blocks: blocks,
  });

  const refetchBlocks = useCallback(async () => {
    const { data } = await refetch();
    if (data) {
      setBlocks(data.blocks);
    }
  }, []);

  useEffect(() => {
    refetchBlocks();
    return () => {
      if (userInfo?.name === localStorage.getItem('presenter')) {
        console.log('저장');
        createBlocksMutation.mutate();
      }
    };
  }, []);

  const handleChange = (result: DropResult) => {
    if (!result.destination) return;

    const { destination } = result;

    setBlocks((prevBlocks) => {
      const [newBlocks] = prevBlocks.splice(result.source.index, 1);
      prevBlocks.splice(destination.index, 0, newBlocks);
      return prevBlocks;
    });
  };

  const addBlock = (id: string) => {
    const newBlock: Block = { id: v4(), text: '', option: 'content' };

    setBlocks((prevBlocks) => {
      index.current = prevBlocks.findIndex((block) => block.id === id);

      const newBlocks = [
        ...prevBlocks.slice(0, index.current + 1),
        newBlock,
        ...prevBlocks.slice(index.current + 1),
      ];

      return newBlocks;
    });

    isAdd.current = true;
    isChanged.current = true;
  };

  const deleteBlock = (id: string) => {
    setBlocks((prevBlocks) => {
      index.current = prevBlocks.findIndex((block) => block.id === id);

      if (prevBlocks.length === 1) {
        return prevBlocks;
      }

      const newBlocks = [
        ...prevBlocks.slice(0, index.current),
        ...prevBlocks.slice(index.current + 1),
      ];
      return newBlocks;
    });
    isAdd.current = false;
    isChanged.current = true;
  };

  const updateBlock = (id: string, text: string, option: string) => {
    setBlocks((prevBlocks) =>
      prevBlocks.map((block) =>
        block.id === id ? { ...block, text, option } : block,
      ),
    );
  };

  useEffect(() => {
    if (!isChanged.current) {
      return;
    }
    isChanged.current = false;

    const selection = window.getSelection();
    const newRange = document.createRange();

    let startContainer = null;

    if (isAdd.current) {
      startContainer =
        document.querySelector('.block')!.childNodes[index.current + 1]
          .childNodes[0].childNodes[1].childNodes[0];
    } else {
      if (index.current == 0) return;
      startContainer =
        document.querySelector('.block')!.childNodes[index.current - 1]
          .childNodes[0].childNodes[1].childNodes[0];
    }

    if (startContainer && selection) {
      if (startContainer.textContent && startContainer.firstChild) {
        if (!isAdd.current) {
          if (startContainer.firstChild.nodeName === 'TABLE') {
            newRange.setStart(startContainer.firstChild, 0);
          } else {
            newRange.setStart(
              startContainer.firstChild,
              startContainer.textContent.length,
            );
          }
        }
      } else {
        newRange.setStart(startContainer, 0);
      }

      selection.removeAllRanges();
      selection.addRange(newRange);
    }
  }, [blocks]);

  return (
    <DragDropContext onDragEnd={handleChange}>
      <Droppable droppableId="block">
        {(provided) => (
          <div
            className={`${styles.main} block`}
            {...provided.droppableProps}
            ref={provided.innerRef}
          >
            {blocks.map(({ id, text, option }, i: number) => (
              <Draggable draggableId={id} index={i} key={id}>
                {(provided, snapshot) => {
                  const result = (
                    <div
                      className={`${styles.blocks}`}
                      {...provided.draggableProps}
                      {...provided.dragHandleProps}
                      ref={provided.innerRef}
                    >
                      <EditorBlock
                        id={id}
                        text={text}
                        option={option}
                        addBlock={addBlock}
                        deleteBlock={deleteBlock}
                        updateBlock={updateBlock}
                      />
                    </div>
                  );

                  if (snapshot.isDragging) {
                    const portal = document.getElementById('portal');
                    if (portal) {
                      return ReactDOM.createPortal(result, portal);
                    }
                  } else {
                    return result;
                  }
                }}
              </Draggable>
            ))}
            {provided.placeholder}
          </div>
        )}
      </Droppable>
    </DragDropContext>
  );
}
