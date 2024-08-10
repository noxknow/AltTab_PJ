import { Block } from '@/components/Editor/EditorPage';
import { API } from './api';

export const solutions = {
  endpoint: {
    default: '/solutions',
  },
  create: async (studyId: string, problemId: string, blocks: Block[]) => {
    const { data } = await API.post(
      `${solutions.endpoint.default}/${studyId}/${problemId}`,
      { blocks: blocks },
    );

    return data;
  },
  getBlocks: async (studyId: string, problemId: string): Promise<Block[]> => {
    const { data } = await API.get<{ blocks: Block[] }>(
      `${solutions.endpoint.default}/${studyId}/${problemId}`,
    );

    return data.blocks;
  },
};
