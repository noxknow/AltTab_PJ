import { useNavigate } from 'react-router-dom';

import { Button } from '@/components/Button/Button';
import styles from './NotFound.module.scss';

export function NotFound() {
  const navigate = useNavigate();

  const onClickMain = () => {
    navigate('/');
  };
  return (
    <div className={styles.container}>
      <div className={styles.text}>
        <p>페이지를 찾을 수 없습니다.</p>
        <Button color="green" fill={true} size="large" onClick={onClickMain}>
          메인 페이지로 이동하기
        </Button>
      </div>
    </div>
  );
}
