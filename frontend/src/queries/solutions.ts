import { useQuery, useMutation, QueryClient } from '@tanstack/react-query';
import { solutions } from '@/services/solutions';
import { Block } from '@/components/Editor/EditorPage';

const blocksKeys = {
  create: ['create'],
  getBlocks: ['getBlocks'],
};

export const useCreateBlocksQuery = (
  studyId: string,
  problemId: string,
  blocks: Block[],
) => {
  const queryClient = new QueryClient();

  return useMutation({
    mutationFn: () => solutions.create(studyId, problemId, blocks),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: blocksKeys.create,
      });
    },
  });
}; 

export const useGetBlocksQuery = (studyId: string, problemId: string) => {
  const { data, isLoading, refetch } = useQuery({
    queryKey: blocksKeys.getBlocks,
    queryFn: (): Promise<Block[]> => solutions.getBlocks(studyId, problemId),
  });
  return { data, isLoading, refetch };
};
