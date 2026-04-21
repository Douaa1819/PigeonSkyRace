/** Mirrors backend DTO field names (Jackson camelCase). */

export type CompetitionDto = {
  id: string;
  nom: string;
  latitudeGPS: number;
  longitudeGPS: number;
  saisonId: string;
  nbPigeons: number;
  pourcentageAdmission: number;
  startTime?: string | null;
  endTime?: string | null;
};

export type PigeonDto = {
  id: string;
  numeroBague: string;
  sexe?: string | null;
  age?: number | null;
  couleur?: string | null;
  colombierId?: string | null;
  imageUrl?: string | null;
};

export type ColombierDto = {
  id: string;
  nomColombier: string;
  coordonneeGPSlatitude: number;
  coordonneeGPSlongitude: number;
  userId: string;
  pigeons: PigeonDto[];
};

/** Matches `ResultatResponseDTO` (Jackson serializes `LocalDateTime` as string). */
export type ResultatDto = {
  id: string;
  dateArrivee?: string | null;
  distance?: number | null;
  vitesse?: number | null;
  points?: number | null;
  rank?: number | null;
  pigeonId?: string | null;
  ringNumber?: string | null;
  loftName?: string | null;
  imageUrl?: string | null;
};

export type PigeonResultHistoryItemDto = {
  competitionId: string;
  competitionName: string;
  dateArrivee?: string | null;
  distance?: number | null;
  vitesse?: number | null;
  points?: number | null;
  rank?: number | null;
};

export type SaisonDto = {
  id: string;
  date?: string | null;
  nom: string;
  description?: string | null;
};
