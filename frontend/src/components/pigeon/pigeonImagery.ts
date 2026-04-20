/**
 * Curated Unsplash imagery: racing birds, sky, motion — rotate by entity id for variety.
 * Replace with your own CDN assets in production if required.
 */
const HERO_IMAGES = [
  'https://images.unsplash.com/photo-1452570053594-1b985d6ea890?auto=format&fit=crop&w=900&q=80',
  'https://images.unsplash.com/photo-1549608276-5786777e65cd?auto=format&fit=crop&w=900&q=80',
  'https://images.unsplash.com/photo-1444464666168-49d7b178774f?auto=format&fit=crop&w=900&q=80',
  'https://images.unsplash.com/photo-1508615032210-6b89817a8439?auto=format&fit=crop&w=900&q=80',
  'https://images.unsplash.com/photo-1518837695005-2083093ee35b?auto=format&fit=crop&w=900&q=80',
];

function hashString(s: string): number {
  let h = 0;
  for (let i = 0; i < s.length; i++) {
    h = (Math.imul(31, h) + s.charCodeAt(i)) | 0;
  }
  return Math.abs(h);
}

export function heroImageForId(id: string): string {
  return HERO_IMAGES[hashString(id) % HERO_IMAGES.length];
}
