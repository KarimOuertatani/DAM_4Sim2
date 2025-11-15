import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document } from 'mongoose';

export type SortieDocument = Sortie & Document;

@Schema()
export class Sortie {
  @Prop({ required: true })
  titre: string;

  @Prop({ required: true })
  description: string;

  @Prop({ required: true })
  date: Date;

  @Prop({ required: true })
  type: string;

  @Prop({ required: true })
  option_camping: boolean;

  @Prop({ required: true })
  lieu: string;

  @Prop({ required: true })
  difficulte: string;

  @Prop({ required: true })
  niveau: string;

  @Prop({ type: [String], default: [] })
  equipement_requis: string[];

  @Prop({ type: Object, required: true })
  itineraire: {
    pointDepart: {
      latitude: number;
      longitude: number;
    };
    pointArrivee: {
      latitude: number;
      longitude: number;
    };
    distance: number;
    duree_estimee: number;
  };

  @Prop({ type: Object })
  camping?: {
    nom: string;
    lieu: string;
    prix: number;
    dateDebut: Date;
    dateFin: Date;
  };

  @Prop({ type: String, default: null })
  campingId?: string;

  @Prop({ required: true })
  capacite: number;

  @Prop({ required: true })
  prix: number;

  @Prop({ type: String, default: null })
  createurId?: string;

  @Prop({ type: [String], default: [] })
  participants: string[];

  @Prop({ default: Date.now })
  createdAt: Date;

  @Prop({ default: Date.now })
  updatedAt: Date;
}

export const SortieSchema = SchemaFactory.createForClass(Sortie);