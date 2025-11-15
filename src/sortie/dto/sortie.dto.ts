import {
  IsString,
  IsOptional,
  IsEnum,
  IsBoolean,
  IsNumber,
  IsDateString,
} from 'class-validator';
import { Type } from 'class-transformer';
import { SortieType } from '../../enums/sortie-type.enum';
import { CreateCampingDto } from '../../camping/camping.dto';

export class CreateSortieDto {
  @IsString()
  titre: string;

  @IsOptional()
  @IsString()
  description?: string;

  @IsDateString()
  date: string;

  @IsEnum(SortieType)
  type: SortieType;

  @IsBoolean()
  option_camping: boolean;

  @IsOptional()
  @IsString()
  campingId?: string;

  @IsOptional()
  @Type(() => CreateCampingDto)
  camping?: CreateCampingDto;

  @IsOptional()
  @IsNumber()
  capacite?: number;
}

export class UpdateSortieDto {
  @IsOptional()
  @IsString()
  titre?: string;

  @IsOptional()
  @IsString()
  description?: string;

  @IsOptional()
  @IsDateString()
  date?: string;

  @IsOptional()
  @IsEnum(SortieType)
  type?: SortieType;

  @IsOptional()
  @IsBoolean()
  option_camping?: boolean;

  @IsOptional()
  @IsString()
  campingId?: string;

  @IsOptional()
  @Type(() => CreateCampingDto)
  camping?: CreateCampingDto;

  @IsOptional()
  @IsNumber()
  capacite?: number;
}
