import { useState, useRef, useEffect } from 'react';
import { v4 } from 'uuid';
import EditorBlock from './EditorBlock';
import styles from './EditorPage.module.scss';

type Block = {
  id: string;
  text: string;
  option: string;
};

export default function EditorPage() {
  const initialBlock = { id: v4(), text: '', option: 'default' };
  const [blocks, setBlocks] = useState<Block[]>([initialBlock]);
  const [caretId, setCaretId] = useState(initialBlock.id);
  const prevLengthRef = useRef(blocks.length);

  const addBlock = (id: string) => {
    const newBlock: Block = { id: v4(), text: '', option: 'default' };

    setCaretId(newBlock.id);

    setBlocks((prevBlocks) => {
      const index = prevBlocks.findIndex((block) => block.id === id);

      const newBlocks = [
        ...prevBlocks.slice(0, index + 1),
        newBlock,
        ...prevBlocks.slice(index + 1),
      ];

      return newBlocks;
    });
  };

  const deleteBlock = (id: string) => {
    setBlocks((prevBlocks) => {
      if (prevBlocks.length === 1) {
        return prevBlocks;
      }

      const index = prevBlocks.findIndex((block) => block.id === id);

      const newBlocks = [
        ...prevBlocks.slice(0, index),
        ...prevBlocks.slice(index + 1),
      ];

      if (index - 1 !== -1) {
        setCaretId(newBlocks[index - 1].id);
      } else {
        setCaretId(newBlocks[0].id);
      }
      return newBlocks;
    });
  };

  useEffect(() => {
    const selection = window.getSelection();
    const newRange = document.createRange();
    const startContainer = document.querySelector(`.a${caretId}`);
    if (startContainer) {
      startContainer.appendChild(document.createTextNode(''));

      if (blocks.length > prevLengthRef.current) {
        newRange.setStart(startContainer.childNodes[0], 0);
      } else {
        if (startContainer.childNodes[0].textContent !== null) {
          newRange.setStart(
            startContainer.childNodes[0],
            startContainer.childNodes[0].textContent.length,
          );
        }
      }

      prevLengthRef.current = blocks.length;

      if (selection) {
        selection.removeAllRanges();
        selection.addRange(newRange);
      }
    }
  }, [blocks]);

  return (
    <div className={styles.main}>
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
