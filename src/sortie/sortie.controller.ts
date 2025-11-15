import {
  Controller,
  Post,
  Get,
  Patch,
  Delete,
  Body,
  Param,
  UseGuards,
  Request,
} from '@nestjs/common';
import {
  ApiTags,
  ApiOperation,
  ApiResponse,
  ApiParam,
  ApiBody,
  ApiBearerAuth,
} from '@nestjs/swagger';
import { SortieService } from './sortie.service';
import { CreateSortieDto, UpdateSortieDto } from './sortie.dto';
import { JwtAuthGuard } from '../auth/jwt-auth.guard';

@ApiTags('Sorties')
@Controller('sorties')
export class SortieController {
  constructor(private readonly sortieService: SortieService) {}

  @Post()
  @UseGuards(JwtAuthGuard)
  @ApiBearerAuth('access_token')
  @ApiOperation({ 
    summary: 'Create a new sortie',
    description: `
      Create a sortie with different configurations based on type:
      
      **CAMPING Type:**
      - Must provide camping details (either campingId or nested camping object)
      - option_camping is ignored for CAMPING type
      
      **VELO or RANDONNEE Type with camping:**
      - Set option_camping = true
      - Provide camping details (campingId or nested camping object)
      
      **VELO or RANDONNEE Type without camping:**
      - Set option_camping = false
      - Do NOT provide camping field
    `
  })
  @ApiBody({
    type: CreateSortieDto,
    examples: {
      veloAvecCampingComplet: {
        summary: '✅ VELO - Avec camping (création)',
        description: 'Sortie vélo avec création de camping',
        value: {
          titre: 'Tour du Lac Léman à Vélo',
          description: 'Circuit vélo de 2 jours avec nuit en camping',
          date: '2024-08-20T07:00:00.000Z',
          type: 'VELO',
          option_camping: true,
          lieu: 'Genève, Suisse',
          difficulte: 'MOYEN',
          niveau: 'INTERMEDIAIRE',
          equipement_requis: ['Vélo', 'Casque', 'Kit réparation', 'Tente'],
          itineraire: {
            pointDepart: {
              latitude: 46.2044,
              longitude: 6.1432
            },
            pointArrivee: {
              latitude: 46.5103,
              longitude: 6.6339
            },
            distance: 68000,
            duree_estimee: 19800
          },
          camping: {
            nom: 'Camping Riviera',
            lieu: 'Vevey, Suisse',
            prix: 42.50,
            dateDebut: '2024-08-20T18:00:00.000Z',
            dateFin: '2024-08-21T10:00:00.000Z'
          },
          capacite: 18,
          prix: 85.00
        },
      },
      veloAvecCampingId: {
        summary: '✅ VELO - Avec campingId existant',
        description: 'Sortie vélo utilisant un camping existant',
        value: {
          titre: 'VTT en Forêt',
          description: 'Sortie VTT avec nuit au camping',
          date: '2024-09-10T08:30:00.000Z',
          type: 'VELO',
          option_camping: true,
          lieu: 'Fontainebleau, France',
          difficulte: 'DIFFICILE',
          niveau: 'AVANCE',
          equipement_requis: ['VTT', 'Casque', 'Protections'],
          itineraire: {
            pointDepart: {
              latitude: 48.4084,
              longitude: 2.7019
            },
            pointArrivee: {
              latitude: 48.3758,
              longitude: 2.6542
            },
            distance: 35000,
            duree_estimee: 14400
          },
          campingId: '673636b4c8e7890123456789',
          capacite: 12,
          prix: 55.00
        },
      },
      veloSansCamping: {
        summary: '✅ VELO - Sans camping',
        description: 'Balade vélo sans hébergement',
        value: {
          titre: 'Balade Vélo Lyon',
          description: 'Circuit vélo urbain',
          date: '2024-09-05T09:00:00.000Z',
          type: 'VELO',
          option_camping: false,
          lieu: 'Lyon, France',
          difficulte: 'FACILE',
          niveau: 'DEBUTANT',
          equipement_requis: ['Vélo', 'Casque', 'Antivol'],
          itineraire: {
            pointDepart: {
              latitude: 45.7640,
              longitude: 4.8357
            },
            pointArrivee: {
              latitude: 45.7597,
              longitude: 4.8275
            },
            distance: 12000,
            duree_estimee: 7200
          },
          capacite: 25,
          prix: 12.00
        },
      },
      campingComplet: {
        summary: '✅ CAMPING - Création complète',
        description: 'Type CAMPING avec création du camping',
        value: {
          titre: 'Weekend Camping Montagne',
          description: 'Camping en montagne',
          date: '2024-07-15T08:00:00.000Z',
          type: 'CAMPING',
          lieu: 'Chamonix, France',
          difficulte: 'MOYEN',
          niveau: 'INTERMEDIAIRE',
          equipement_requis: ['Tente', 'Sac de couchage'],
          itineraire: {
            pointDepart: {
              latitude: 45.8326,
              longitude: 6.8652
            },
            pointArrivee: {
              latitude: 45.9237,
              longitude: 6.8694
            },
            distance: 12500,
            duree_estimee: 18000
          },
          camping: {
            nom: 'Camping Les Aiguilles',
            lieu: 'Argentière, France',
            prix: 55.50,
            dateDebut: '2024-07-15T14:00:00.000Z',
            dateFin: '2024-07-17T11:00:00.000Z'
          },
          capacite: 25,
          prix: 120.00
        },
      },
      campingExistant: {
        summary: '✅ CAMPING - Avec campingId',
        description: 'Type CAMPING avec camping existant',
        value: {
          titre: 'Camping au Lac',
          description: 'Weekend au lac',
          date: '2024-08-10T09:00:00.000Z',
          type: 'CAMPING',
          lieu: 'Annecy, France',
          difficulte: 'FACILE',
          niveau: 'DEBUTANT',
          equipement_requis: ['Maillot de bain', 'Serviette'],
          itineraire: {
            pointDepart: {
              latitude: 45.8992,
              longitude: 6.1294
            },
            pointArrivee: {
              latitude: 45.8636,
              longitude: 6.1691
            },
            distance: 5000,
            duree_estimee: 3600
          },
          campingId: '673636b4c8e7890123456789',
          capacite: 30,
          prix: 85.00
        },
      },
      randonneeAvecCamping: {
        summary: '✅ RANDONNEE - Avec camping',
        description: 'Randonnée avec camping',
        value: {
          titre: 'Randonnée GR20',
          description: 'Trek en Corse',
          date: '2024-09-15T06:00:00.000Z',
          type: 'RANDONNEE',
          option_camping: true,
          lieu: 'Calenzana, Corse',
          difficulte: 'DIFFICILE',
          niveau: 'EXPERT',
          equipement_requis: ['Sac à dos', 'Chaussures montagne'],
          itineraire: {
            pointDepart: {
              latitude: 42.5074,
              longitude: 8.8550
            },
            pointArrivee: {
              latitude: 42.3926,
              longitude: 9.0372
            },
            distance: 42000,
            duree_estimee: 86400
          },
          campingId: '673636b4c8e7890123456790',
          capacite: 15,
          prix: 180.00
        },
      },
      randonneeSansCamping: {
        summary: '✅ RANDONNEE - Sans camping',
        description: 'Randonnée journée',
        value: {
          titre: 'Randonnée Lac Blanc',
          description: 'Randonnée panoramique',
          date: '2024-10-01T08:00:00.000Z',
          type: 'RANDONNEE',
          option_camping: false,
          lieu: 'Chamonix, France',
          difficulte: 'MOYEN',
          niveau: 'INTERMEDIAIRE',
          equipement_requis: ['Chaussures randonnée', 'Gourde'],
          itineraire: {
            pointDepart: {
              latitude: 45.9798,
              longitude: 6.9085
            },
            pointArrivee: {
              latitude: 45.9771,
              longitude: 6.8908
            },
            distance: 7500,
            duree_estimee: 14400
          },
          capacite: 35,
          prix: 30.00
        },
      },
    },
  })
  @ApiResponse({
    status: 201,
    description: 'Sortie created successfully',
    schema: {
      example: {
        _id: '673636b4c8e7890123456788',
        titre: 'Weekend Camping Adventure en Montagne',
        description: 'Epic mountain camping trip for 2 days with stunning views and hiking trails',
        date: '2024-07-15T08:00:00Z',
        type: 'CAMPING',
        option_camping: false,
        lieu: 'Chamonix, Haute-Savoie, France',
        difficulte: 'MOYEN',
        equipement_requis: ['Tente', 'Sac de couchage', 'Chaussures de randonnée', 'Lampe frontale'],
        itineraire: {
          pointDepart: {
            latitude: 45.8326,
            longitude: 6.8652,
            display_name: 'Chamonix-Mont-Blanc, France',
            address: 'Place de l\'Église, 74400 Chamonix-Mont-Blanc'
          },
          pointArrivee: {
            latitude: 45.9237,
            longitude: 6.8694,
            display_name: 'Refuge du Plan de l\'Aiguille, France',
            address: 'Refuge du Plan de l\'Aiguille, 74400 Chamonix'
          },
          description: 'Montée progressive vers le refuge avec vue panoramique',
          distance: 12500,
          duree_estimee: 18000
        },
        createurId: '6915f73054c7d88a631ed7df',
        camping: {
          _id: '673636b4c8e7890123456789',
          nom: 'Camping Les Aiguilles d\'Argentière',
          description: 'Camping 4 étoiles avec vue sur le Mont-Blanc',
          lieu: 'Argentière, 74400 Chamonix-Mont-Blanc',
          prix: 55.50,
          dateDebut: '2024-07-15T14:00:00Z',
          dateFin: '2024-07-17T11:00:00Z'
        },
        capacite: 25,
        prix: 120.00,
        participants: [],
        createdAt: '2024-11-14T20:10:00Z',
        updatedAt: '2024-11-14T20:10:00Z',
      },
    },
  })
  @ApiResponse({
    status: 400,
    description: 'Invalid business logic',
    schema: {
      example: {
        statusCode: 400,
        message: 'For CAMPING sorties, either campingId or camping DTO must be provided',
        error: 'Bad Request'
      }
    }
  })
  @ApiResponse({
    status: 401,
    description: 'Unauthorized - valid JWT token required',
  })
  async create(@Body() createSortieDto: CreateSortieDto, @Request() req) {
    const userId = req.user.sub;
    return this.sortieService.create(createSortieDto, userId);
  }

  @Get()
  @ApiOperation({ 
    summary: 'Get all sorties with populated data (Public)',
    description: 'Retrieve all sorties with creator, camping, and participants information populated'
  })
  @ApiResponse({
    status: 200,
    description: 'List of all sorties',
    schema: {
      example: [
        {
          _id: '673636b4c8e7890123456788',
          titre: 'Weekend Camping Adventure en Montagne',
          description: 'Epic mountain camping trip',
          date: '2024-07-15T08:00:00Z',
          type: 'CAMPING',
          option_camping: false,
          lieu: 'Chamonix, Haute-Savoie, France',
          difficulte: 'MOYEN',
          createurId: {
            _id: '6915f73054c7d88a631ed7df',
            name: 'Mohamed Amine Mami',
            email: 'mohamedamine.mami@esprit.tn',
          },
          camping: {
            _id: '673636b4c8e7890123456789',
            nom: 'Camping Les Aiguilles d\'Argentière',
            lieu: 'Argentière, France',
            prix: 55.50
          },
          capacite: 25,
          prix: 120.00,
          participants: [],
        },
        {
          _id: '673636b4c8e7890123456791',
          titre: 'Balade Vélo en Ville',
          description: 'Circuit vélo urbain',
          date: '2024-09-05T09:00:00Z',
          type: 'VELO',
          option_camping: false,
          lieu: 'Lyon, France',
          difficulte: 'FACILE',
          createurId: {
            _id: '6915f73054c7d88a631ed7df',
            name: 'Mohamed Amine Mami',
            email: 'mohamedamine.mami@esprit.tn',
          },
          camping: null,
          capacite: 40,
          prix: 15.00,
          participants: [],
        }
      ],
    },
  })
  async findAll() {
    return this.sortieService.findAll();
  }

  @Get(':id')
  @ApiOperation({ summary: 'Récupérer une sortie par ID' })
  @ApiParam({
    name: 'id',
    description: 'ID de la sortie',
    example: '507f1f77bcf86cd799439011',
  })
  @ApiResponse({
    status: 200,
    description: 'Sortie trouvée',
    schema: {
      example: {
        _id: '6709d45e1c9f4c123456789b',
        titre: 'Weekend Camping Adventure',
        description: 'Epic mountain camping trip',
        date: '2024-07-15T08:00:00Z',
        type: 'CAMPING',
        option_camping: false,
        createurId: {
          _id: '6709d45e1c9f4c123456789c',
          name: 'John Doe',
          email: 'john@example.com',
        },
        camping: {
          _id: '6709d45e1c9f4c123456789d',
          nom: 'Camping Alpes',
          lieu: 'Annecy, France',
          prix: 50,
        },
        capacite: 20,
        participants: ['6709d45e1c9f4c123456789e'],
      },
    },
  })
  @ApiResponse({ status: 404, description: 'Sortie non trouvée' })
  async findOne(@Param('id') id: string) {
    return this.sortieService.findOne(id);
  }

  @Patch(':id')
  @UseGuards(JwtAuthGuard)
  @ApiBearerAuth('access_token')
  @ApiOperation({ summary: 'Mettre à jour une sortie' })
  @ApiParam({ name: 'id', description: 'ID de la sortie' })
  @ApiBody({
    type: UpdateSortieDto,
    examples: {
      updateTitle: {
        summary: 'Update sortie title',
        value: {
          titre: 'Updated Camping Adventure',
        },
      },
      updateCapacity: {
        summary: 'Update capacity',
        value: {
          capacite: 25,
        },
      },
      updateComplete: {
        summary: 'Mise à jour complète',
        value: {
          titre: 'Randonnée au Mont Blanc - Modifiée',
          description: 'Description mise à jour avec nouvelles informations',
          date: '2024-06-25T09:00:00Z',
          type: 'RANDONNEE',
          option_camping: true,
          capacite: 20,
          itineraire: {
            pointDepart: {
              latitude: 45.8326,
              longitude: 6.8652,
            },
            pointArrivee: {
              latitude: 45.9237,
              longitude: 6.8694,
            },
            distance: 13000,
            duree_estimee: 19000,
          },
        },
      },
      updatePartial: {
        summary: 'Mise à jour partielle (titre et capacité)',
        value: {
          titre: 'Nouveau titre',
          capacite: 25,
        },
      },
    },
  })
  @ApiResponse({ status: 200, description: 'Sortie mise à jour' })
  @ApiResponse({ status: 401, description: 'Unauthorized - valid JWT token required' })
  @ApiResponse({ status: 403, description: 'Only creator can update' })
  @ApiResponse({ status: 404, description: 'Sortie non trouvée' })
  async update(
    @Param('id') id: string,
    @Body() updateSortieDto: UpdateSortieDto,
    @Request() req,
  ) {
    const userId = req.user.sub;
    return this.sortieService.update(id, updateSortieDto, userId);
  }

  @Delete(':id')
  @UseGuards(JwtAuthGuard)
  @ApiBearerAuth('access_token')
  @ApiOperation({ summary: 'Supprimer une sortie' })
  @ApiParam({ name: 'id', description: 'ID de la sortie' })
  @ApiResponse({ status: 200, description: 'Sortie supprimée' })
  @ApiResponse({ status: 401, description: 'Unauthorized - valid JWT token required' })
  @ApiResponse({ status: 403, description: 'Only creator can delete' })
  @ApiResponse({ status: 404, description: 'Sortie non trouvée' })
  async delete(@Param('id') id: string, @Request() req) {
    const userId = req.user.sub;
    await this.sortieService.delete(id, userId);
    return { message: 'Sortie supprimée avec succès' };
  }
}
