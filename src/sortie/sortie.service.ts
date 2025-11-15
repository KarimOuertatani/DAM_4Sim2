import {
  BadRequestException,
  ForbiddenException,
  Injectable,
  NotFoundException,
} from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model, Types } from 'mongoose';
import { Sortie, SortieDocument } from './entities/sortie.schema';
import { CreateSortieDto, UpdateSortieDto } from './dto/sortie.dto';
import { SortieType } from '../enums/sortie-type.enum';
import { CampingService } from '../camping/camping.service';
import { CreateCampingDto } from '../camping/camping.dto';

@Injectable()
export class SortieService {
  constructor(
    @InjectModel(Sortie.name) private sortieModel: Model<SortieDocument>,
    private campingService: CampingService,
  ) {}

  async create(
    createSortieDto: CreateSortieDto,
    userId: string,
  ): Promise<SortieDocument> {
    const { type, option_camping, campingId, camping, ...rest } =
      createSortieDto;

    // Validate business rules
    if (type === SortieType.CAMPING) {
      if (!campingId && !camping) {
        throw new BadRequestException(
          'For CAMPING sorties, either campingId or camping DTO must be provided',
        );
      }
    } else if (type === SortieType.VELO || type === SortieType.RANDONNEE) {
      if (option_camping === true) {
        if (!campingId && !camping) {
          throw new BadRequestException(
            'For VELO/RANDONNEE with option_camping=true, either campingId or camping DTO must be provided',
          );
        }
      } else if (option_camping === false) {
        if (campingId || camping) {
          throw new BadRequestException(
            'For VELO/RANDONNEE with option_camping=false, camping must not be provided',
          );
        }
      }
    }

    let campingId_resolved: Types.ObjectId | null = null;

    // Handle camping DTO: create new camping
    if (camping) {
      const createdCamping = await this.campingService.create(camping);
      campingId_resolved = new Types.ObjectId(String(createdCamping._id));
    } else if (campingId) {
      // Validate camping exists
      await this.campingService.findOne(campingId);
      campingId_resolved = new Types.ObjectId(campingId);
    }

    const sortieData = {
      ...rest,
      type,
      option_camping,
      createurId: new Types.ObjectId(userId),
      camping: campingId_resolved,
      participants: [],
    };

    const sortie = new this.sortieModel(sortieData);
    return sortie.save();
  }

  async findAll(): Promise<SortieDocument[]> {
    return this.sortieModel
      .find()
      .populate('createurId', 'name email')
      .populate('camping')
      .populate('participants')
      .exec();
  }

  async findOne(id: string): Promise<SortieDocument> {
    if (!Types.ObjectId.isValid(id)) {
      throw new BadRequestException('Invalid sortie ID');
    }

    const sortie = await this.sortieModel
      .findById(id)
      .populate('createurId', 'name email')
      .populate('camping')
      .populate('participants')
      .exec();

    if (!sortie) {
      throw new NotFoundException('Sortie not found');
    }

    return sortie;
  }

  async update(
    id: string,
    updateSortieDto: UpdateSortieDto,
    userId: string,
  ): Promise<SortieDocument> {
    if (!Types.ObjectId.isValid(id)) {
      throw new BadRequestException('Invalid sortie ID');
    }

    const sortie = await this.sortieModel.findById(id).exec();
    if (!sortie) {
      throw new NotFoundException('Sortie not found');
    }

    // Check authorization: only creator can update
    if (sortie.createurId.toString() !== userId) {
      throw new ForbiddenException(
        'Only the creator can update this sortie',
      );
    }

    const { type, option_camping, campingId, camping, ...rest } =
      updateSortieDto;

    let updateData: any = rest;

    if (type !== undefined) {
      updateData.type = type;
    }
    if (option_camping !== undefined) {
      updateData.option_camping = option_camping;
    }

    // Handle camping updates
    if (camping) {
      const createdCamping = await this.campingService.create(camping);
      updateData.camping = new Types.ObjectId(String(createdCamping._id));
    } else if (campingId) {
      await this.campingService.findOne(campingId);
      updateData.camping = new Types.ObjectId(campingId);
    }

    const updated = await this.sortieModel
      .findByIdAndUpdate(id, updateData, { new: true })
      .populate('createurId', 'name email')
      .populate('camping')
      .populate('participants')
      .exec();

    if (!updated) {
      throw new NotFoundException('Sortie not found after update');
    }

    return updated;
  }

  async delete(id: string, userId: string): Promise<void> {
    if (!Types.ObjectId.isValid(id)) {
      throw new BadRequestException('Invalid sortie ID');
    }

    const sortie = await this.sortieModel.findById(id).exec();
    if (!sortie) {
      throw new NotFoundException('Sortie not found');
    }

    // Check authorization: only creator can delete
    if (sortie.createurId.toString() !== userId) {
      throw new ForbiddenException(
        'Only the creator can delete this sortie',
      );
    }

    await this.sortieModel.findByIdAndDelete(id).exec();
  }

  async addParticipant(sortieId: string, participationId: string): Promise<void> {
    await this.sortieModel.findByIdAndUpdate(
      sortieId,
      { $push: { participants: new Types.ObjectId(participationId) } },
    );
  }

  async removeParticipant(sortieId: string, participationId: string): Promise<void> {
    await this.sortieModel.findByIdAndUpdate(
      sortieId,
      { $pull: { participants: new Types.ObjectId(participationId) } },
    );
  }

  async getParticipantCount(sortieId: string): Promise<number> {
    const sortie = await this.sortieModel.findById(sortieId).exec();
    return sortie?.participants?.length || 0;
  }
}
