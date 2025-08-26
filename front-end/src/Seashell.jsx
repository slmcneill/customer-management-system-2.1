import React from 'react';
import seashellImg from './assets/seashell.webp';
import './Seashell.css';

const Seashell = ({ className, animationDuration = 6, animationDelay = 0 }) => {
  return (
    <img
      src={seashellImg}
      alt="Seashell"
      className={`seashell ${className}`}
      style={{
        animationDuration: `${animationDuration}s`,
        animationDelay: `${animationDelay}s`,
      }}
    />
  );
};

export default Seashell;
