import React from "react";
import styles from "./ToolButton.module.scss";

type ToolButtonProps = {
  icon: React.ComponentType<React.SVGProps<SVGSVGElement>>;
  onClick: () => void;
  disabled: boolean;
  title: string;
};

const ToolButton: React.FC<ToolButtonProps> = ({ icon: Icon, onClick, disabled = false, title }) => {
  return (
    <button
      type="button"
      className={styles.toolbutton}
      onClick={onClick}
      disabled={disabled}
      title={title}
    >
      <Icon className={disabled ? styles.disabled : ""} />
    </button>
  );
};

export default ToolButton;
