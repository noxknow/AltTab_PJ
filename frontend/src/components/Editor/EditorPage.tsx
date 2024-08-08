import { useState, useEffect, useRef } from 'react';
import { v4 } from 'uuid';
import { EditorBlock } from './EditorBlock';
import styles from './EditorPage.module.scss';

type Block = {
  id: string;
  text: string;
  option: string;
};

export function EditorPage() {
  const initialBlock = { id: v4(), text: '', option: 'content' };
  const [blocks, setBlocks] = useState<Block[]>([initialBlock]);
  const isAdd = useRef(true);
  const index = useRef(-1);

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
  };

  const deleteBlock = (id: string) => {
    setBlocks((prevBlocks) => {
      index.current = prevBlocks.findIndex((block) => block.id === id);

      if (index.current == 0) {
        return prevBlocks;
      }

      const newBlocks = [
        ...prevBlocks.slice(0, index.current),
        ...prevBlocks.slice(index.current + 1),
      ];
      return newBlocks;
    });
    isAdd.current = false;
  };

  useEffect(() => {
    const selection = window.getSelection();
    const newRange = document.createRange();

    let startContainer = null;

    if (isAdd.current) {
      startContainer =
        document.querySelector('.block')!.childNodes[index.current + 1]
          .childNodes[0];
    } else {
      startContainer =
        document.querySelector('.block')!.childNodes[index.current - 1]
          .childNodes[0];
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
    <div className={`${styles.main} block`}>
      {blocks.map(({ id, text, option }) => (
        <EditorBlock
          key={id}
          id={id}
          text={text}
          option={option}
          addBlock={addBlock}
          deleteBlock={deleteBlock}
        />
      ))}
    </div>
  );
}
