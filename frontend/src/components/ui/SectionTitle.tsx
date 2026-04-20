import type { ReactNode } from 'react';
import './section-title.css';

type Props = {
  eyebrow?: string;
  title: string;
  subtitle?: ReactNode;
};

export function SectionTitle({ eyebrow, title, subtitle }: Props) {
  return (
    <header className="section-title">
      {eyebrow && <span className="section-title__eyebrow">{eyebrow}</span>}
      <h1 className="section-title__h">{title}</h1>
      {subtitle && <p className="section-title__sub">{subtitle}</p>}
    </header>
  );
}
