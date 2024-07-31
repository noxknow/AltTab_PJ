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
  const initialBlock = { id: v4(), text: '', option: 'header' };
  const [blocks, setBlocks] = useState<Block[]>([initialBlock]);
  const [caretId, setCaretId] = useState(initialBlock.id);
  const blocksRef = useRef(blocks);
  const prevLengthRef = useRef(blocks.length);

  const addBlock = (id: string) => {
    const newBlock: Block = { id: v4(), text: '', option: 'content' };

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
      if (startContainer.childNodes.length === 0) {
        startContainer.appendChild(document.createTextNode(''));
      }

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
      blocksRef.current = blocks;
      // console.log(newRange);

      if (selection) {
        selection.removeAllRanges();
        selection.addRange(newRange);
      }
    }
  }, [blocks]);

  const moveUp = (id: string) => {
    const selection = document.getSelection();
    const newRange = document.createRange();
    const index = blocksRef.current.findIndex((block) => block.id === id);
    if (index === 0) {
      return;
    }
    const startContainer = document.querySelector(
      `.a${blocksRef.current[index - 1].id}`,
    );
    if (startContainer && selection) {
      const pos = selection.anchorOffset;
      if (startContainer.childNodes[0].textContent !== null) {
        newRange.setStart(
          startContainer.childNodes[0],
          pos > startContainer.childNodes[0].textContent.length
            ? startContainer.childNodes[0].textContent.length
            : pos,
        );
      }
      console.log(newRange);
      if (selection) {
        selection.removeAllRanges();
        selection.addRange(newRange);
      }
    }
  };

  const moveDown = (id: string) => {
    const selection = document.getSelection();
    const newRange = document.createRange();
    const index = blocksRef.current.findIndex((block) => block.id === id);
    if (index === blocksRef.current.length - 1) {
      return;
    }
    const startContainer = document.querySelector(
      `.a${blocksRef.current[index + 1].id}`,
    );
    if (startContainer && selection) {
      const pos = selection.anchorOffset;
      if (startContainer.childNodes[0].textContent !== null) {
        newRange.setStart(
          startContainer.childNodes[0],
          pos > startContainer.childNodes[0].textContent.length
            ? startContainer.childNodes[0].textContent.length
            : pos,
        );
      }
      console.log(newRange);
      if (selection) {
        selection.removeAllRanges();
        selection.addRange(newRange);
      }
    }
  };

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
          moveUp={moveUp}
          moveDown={moveDown}
        />
      ))}
    </div>
  );
}
