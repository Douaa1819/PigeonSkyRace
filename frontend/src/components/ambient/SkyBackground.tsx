import './sky-background.css';

export function SkyBackground() {
  return (
    <div className="sky-bg" aria-hidden="true">
      <div className="sky-bg__gradient" />
      <div className="sky-bg__vignette" />
    </div>
  );
}
