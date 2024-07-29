import { codeHighlighter } from '@/configs/codeHighlighter';

export const highlightCode = (code: string, language: string) => {
  return codeHighlighter.highlight(code, { language }).value;
};
