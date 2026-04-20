import './skeleton.css';

type Props = {
  className?: string;
  height?: string | number;
};

export function Skeleton({ className = '', height = 14 }: Props) {
  return <div className={`skeleton-line ${className}`.trim()} style={{ height }} />;
}

export function SkeletonCard() {
  return (
    <div className="skeleton-card">
      <Skeleton height={160} className="skeleton-card__hero" />
      <Skeleton height={20} />
      <Skeleton height={14} />
      <Skeleton height={14} />
    </div>
  );
}
