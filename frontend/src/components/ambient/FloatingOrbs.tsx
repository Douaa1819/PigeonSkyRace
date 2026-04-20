import { motion } from 'framer-motion';
import './floating-orbs.css';

const orbs = [
  { x: '8%', y: '12%', s: 180, delay: 0, duration: 22 },
  { x: '78%', y: '18%', s: 120, delay: 2, duration: 18 },
  { x: '22%', y: '68%', s: 200, delay: 1, duration: 26 },
  { x: '65%', y: '72%', s: 90, delay: 3, duration: 20 },
];

export function FloatingOrbs() {
  return (
    <div className="floating-orbs" aria-hidden="true">
      {orbs.map((o, i) => (
        <motion.div
          key={i}
          className="floating-orbs__blob"
          style={{
            left: o.x,
            top: o.y,
            width: o.s,
            height: o.s,
          }}
          animate={{
            y: [0, -18, 0],
            x: [0, 10, -6, 0],
            scale: [1, 1.05, 0.98, 1],
          }}
          transition={{
            duration: o.duration,
            repeat: Infinity,
            ease: 'easeInOut',
            delay: o.delay,
          }}
        />
      ))}
    </div>
  );
}
