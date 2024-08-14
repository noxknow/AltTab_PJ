import { useMemo } from 'react';
import { studyProblemDetails } from '@/types/schedule';

const COLORS = [
  'red',
  'indigo',
  'blue',
  'green',
  'yellow',
  'purple',
  'orange',
  'pink',
  'teal',
  'cyan',
  'lime',
  'gray',
];

const DEADLINE_COLOR = 'black';

export const DIFFICULTY_MAP: { [key: number]: [string, string] } = {
  1: ['B5', 'bronze'],
  2: ['B4', 'bronze'],
  3: ['B3', 'bronze'],
  4: ['B2', 'bronze'],
  5: ['B1', 'bronze'],
  6: ['S5', 'silver'],
  7: ['S4', 'silver'],
  8: ['S3', 'silver'],
  9: ['S2', 'silver'],
  10: ['S1', 'silver'],
  11: ['G5', 'gold'],
  12: ['G4', 'gold'],
  13: ['G3', 'gold'],
  14: ['G2', 'gold'],
  15: ['G1', 'gold'],
  16: ['P5', 'platinum'],
  17: ['P4', 'platinum'],
  18: ['P3', 'platinum'],
  19: ['P2', 'platinum'],
  20: ['P1', 'platinum'],
  21: ['D5', 'diamond'],
  22: ['D4', 'diamond'],
  23: ['D3', 'diamond'],
  24: ['D2', 'diamond'],
  25: ['D1', 'diamond'],
  26: ['R5', 'ruby'],
  27: ['R4', 'ruby'],
  28: ['R3', 'ruby'],
  29: ['R2', 'ruby'],
  30: ['R1', 'ruby'],
};

function shuffleArray<T>(array: T[]): T[] {
  const shuffled = [...array];
  for (let i = shuffled.length - 1; i > 0; i--) {
    const j = Math.floor(Math.random() * (i + 1));
    [shuffled[i], shuffled[j]] = [shuffled[j], shuffled[i]];
  }
  return shuffled;
}

export function useChangeColor(
  studyInfo: studyProblemDetails | null | undefined,
) {
  const tagColors = useMemo(() => {
    const colorMap: { [key: string]: string } = {};
    const shuffledColors = shuffleArray(COLORS);
    let colorIndex = 0;

    studyInfo?.studyProblems.forEach((problem) => {
      problem.tag.split(';').forEach((tag) => {
        const trimmedTag = tag.trim();
        if (!colorMap[trimmedTag]) {
          colorMap[trimmedTag] =
            shuffledColors[colorIndex % shuffledColors.length];
          colorIndex++;
        }
      });
    });
    return colorMap;
  }, [studyInfo]);

  const getDifficultyColor = (level: number) => DIFFICULTY_MAP[level][1];
  const getDifficultyLabel = (level: number) => DIFFICULTY_MAP[level][0];
  const getDeadlineColor = () => DEADLINE_COLOR;

  return {
    tagColors,
    getDifficultyColor,
    getDifficultyLabel,
    getDeadlineColor,
  };
}
