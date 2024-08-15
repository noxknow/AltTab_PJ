import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { problemService } from '@/services/problem'; // 문제 정보 가져오는 함수
import styles from '../Modal.module.scss';
import CloseSVG from '@/assets/icons/close.svg?react';
import { useCompilerModalState } from '@/hooks/useCompilerState';

export function ProblemModal() {
  const { problemId } = useParams<{ problemId: string }>();
  const [problemData, setProblemData] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);
  const { setIsModalOpen } = useCompilerModalState();

  useEffect(() => {
    const fetchProblemData = async () => {
      try {
        const data = await problemService.getProblemDetails(Number(problemId));

        // HTML 경로 수정 (src와 href 경로를 절대 경로로 바꾸기)
        let fixedHtml: string = data
          .replace(/src="\//g, 'src="https://www.acmicpc.net/')
          .replace(/href="\//g, 'href="https://www.acmicpc.net/');

        // 복사 버튼을 삭제
        fixedHtml = fixedHtml.replace(
          /<button[^>]*copy-button[^>]*>[^<]*<\/button>/g,
          '',
        );

        // 중복된 출처 섹션 제거
        const sourceSectionRegex =
          /<section id="source">([\s\S]*?)<\/section>/g;
        const match = fixedHtml.match(sourceSectionRegex);
        if (match && match.length > 1) {
          // 여러 번 매칭된 경우 첫 번째만 남기고 제거
          fixedHtml = fixedHtml.replace(sourceSectionRegex, (match, index) =>
            index === 0 ? match : '',
          );
        }

        setProblemData(fixedHtml);
      } catch (error) {
        console.error('Error fetching problem data:', error);
        setProblemData('<p>Error loading problem data</p>');
      } finally {
        setLoading(false);
      }
    };

    fetchProblemData();
  }, [problemId]);

  const handleClose = () => {
    setIsModalOpen(false);
  };

  if (loading) {
    return <p>Loading...</p>;
  }

  if (!problemData) {
    return <p>잘못된 문제 번호 입니다. 다시 확인 해주세요</p>;
  }

  return (
    <div className={styles.problemModalContainer}>
      <button className={styles.closeButton} onClick={handleClose}>
        <CloseSVG width={24} height={24} stroke="#F24242" />
      </button>
      <h2 className={styles.problemTitle}>문제번호 : {problemId}</h2>
      <div className={styles.backjunbutton}>
        <button
          onClick={() =>
            window.open(
              `https://www.acmicpc.net/problem/${problemId}`,
              '_blank',
            )
          }
        >
          백준에서 문제 보기
        </button>
      </div>
      <div
        className={styles.problemContent}
        dangerouslySetInnerHTML={{ __html: problemData }}
      />
    </div>
  );
}
