import React from "react";
import styles from "./ToolButton.module.scss";

type ToolButtonProps = {
  icon: React.ComponentType<React.SVGProps<SVGSVGElement>>;
  onClick: () => void;
  disabled?: boolean;
  title: string;
};

const ToolButton: React.FC<ToolButtonProps> = ({ icon: Icon, onClick, disabled = false, title }) => {
  return (
    <button
      type="button"
      className={`${styles.toolbutton} ${disabled ? styles.disabledButton : ""}`}
      onClick={onClick}
      disabled={disabled}
      aria-disabled={disabled}
      title={title}
    >
      <Icon className={`${styles.icon} ${disabled ? styles.disabledIcon : ""}`} />
    </button>
  );
};

export default ToolButton;
