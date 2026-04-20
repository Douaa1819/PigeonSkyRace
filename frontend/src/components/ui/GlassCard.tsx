import { motion, type HTMLMotionProps } from 'framer-motion';
import type { ReactNode } from 'react';
import './glass-card.css';

type Props = {
  children: ReactNode;
  className?: string;
  hoverLift?: boolean;
} & Omit<HTMLMotionProps<'div'>, 'children'>;

export function GlassCard({ children, className = '', hoverLift = true, ...rest }: Props) {
  return (
    <motion.div
      className={`glass-card ${className}`.trim()}
      initial={false}
      whileHover={
        hoverLift
          ? { y: -6, transition: { type: 'spring', stiffness: 400, damping: 22 } }
          : undefined
      }
      {...rest}
    >
      <div className="glass-card__shine" aria-hidden />
      {children}
    </motion.div>
  );
}
