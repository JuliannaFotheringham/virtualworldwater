import processing.core.PImage;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

/*
WorldModel ideally keeps track of the actual size of our grid world and what is in that world
in terms of entities and background elements
 */

final class WorldModel
{
 //  public static final String ATLANTIS_KEY = "atlantis";
   public static final int ATLANTIS_NUM_PROPERTIES = 4;
   public static final int ATLANTIS_ID = 1;
   public static final int ATLANTIS_COL = 2;
   public static final int ATLANTIS_ROW = 3;

   private static final String OCTO_KEY = "octo";
   private static final String OBSTACLE_KEY = "obstacle";
   private static final String FISH_KEY = "fish";
   private static final String ATLANTIS_KEY = "atlantis";
   private static final String SGRASS_KEY = "seaGrass";
   private static final int PROPERTY_KEY = 0;


   public int numRows;
   public int numCols;
   public Background background[][];
   public Entity occupancy[][];
   public Set<Entity> entities;


   public static final String BGND_KEY = "background";

   public static final int FISH_REACH = 1;

   public WorldModel(int numRows, int numCols, Background defaultBackground)
   {
      this.numRows = numRows;
      this.numCols = numCols;
      this.background = new Background[numRows][numCols];
      this.occupancy = new Entity[numRows][numCols];
      this.entities = new HashSet<>();

      for (int row = 0; row < numRows; row++)
      {
         Arrays.fill(this.background[row], defaultBackground);
      }
   }

   public  Optional<Point> findOpenAround(Point pos)
   {
      for (int dy = -FISH_REACH; dy <= FISH_REACH; dy++)
      {
         for (int dx = -FISH_REACH; dx <= FISH_REACH; dx++)
         {
            Point newPt = new Point(pos.x + dx, pos.y + dy);
            if (withinBounds(newPt) &&
                    !isOccupied(newPt))
            {
               return Optional.of(newPt);
            }
         }
      }

      return Optional.empty();
   }








   public  void removeEntity(Entity entity)
   {
      removeEntityAt(entity.position);
   }

   public  void removeEntityAt(Point pos)
   {
      if (withinBounds(pos)
              && getOccupancyCell(pos) != null)
      {
         Entity entity = getOccupancyCell(pos);

         /* this moves the entity just outside of the grid for
            debugging purposes */
         entity.position = new Point(-1, -1);
          this.entities.remove(entity);
         setOccupancyCell(this, pos, null);
      }
   }

   public  Optional<PImage> getBackgroundImage(
                                                     Point pos)
   {
      if (withinBounds(pos))
      {
         return Optional.of(Entity.getCurrentImage(getBackgroundCell(pos)));
      }
      else
      {
         return Optional.empty();
      }
   }

   public  void setBackground(Point pos,
                                    Background background)
   {
      if (withinBounds(pos))
      {
         setBackgroundCell(pos, background);
      }
   }

   public  Optional<Entity> getOccupant(Point pos)
   {
      if (isOccupied( pos))
      {
         return Optional.of(getOccupancyCell(pos));
      }
      else
      {
         return Optional.empty();
      }
   }

   public  Entity getOccupancyCell(Point pos)
   {
      return this.occupancy[pos.y][pos.x];
   }

   public static void setOccupancyCell(WorldModel world, Point pos,
                                       Entity entity)
   {
      world.occupancy[pos.y][pos.x] = entity;
   }

   public  Background getBackgroundCell(Point pos)
   {
      return this.background[pos.y][pos.x];
   }

   public  void setBackgroundCell( Point pos,
                                        Background background)
   {
       this.background[pos.y][pos.x] = background;
   }



   public  boolean processLine(String line,
                                     ImageStore imageStore)
   {
      String[] properties = line.split("\\s");
      if (properties.length > 0)
      {
         switch (properties[PROPERTY_KEY])
         {
            case BGND_KEY:
               return Background.parseBackground(properties, this, imageStore);
            case OCTO_KEY:
               return parseOcto(properties,  imageStore);
            case OBSTACLE_KEY:
               return parseObstacle(properties,  imageStore);
            case FISH_KEY:
               return parseFish(properties,  imageStore);
            case ATLANTIS_KEY:
               return parseAtlantis(properties,  imageStore);
            case SGRASS_KEY:
               return parseSgrass(properties,  imageStore);
         }
      }

      return false;
   }



   public  void tryAddEntity(Entity entity)
   {
      if (isOccupied( entity.position))
      {
         // arguably the wrong type of exception, but we are not
         // defining our own exceptions yet
         throw new IllegalArgumentException("position occupied");
      }

      addEntity(entity);
   }

   public  boolean withinBounds(Point pos)
   {
      return pos.y >= 0 && pos.y < this.numRows &&
              pos.x >= 0 && pos.x < this.numCols;
   }

   public  boolean isOccupied(Point pos)
   {
      return withinBounds(pos) &&
              getOccupancyCell(pos) != null;
   }


   public  Optional<Entity> findNearest(Point pos,
                                              EntityKind kind)
   {
      List<Entity> ofType = new LinkedList<>();
      for (Entity entity : this.entities)
      {
         if (entity.kind == kind)
         {
            ofType.add(entity);
         }
      }

      return nearestEntity(ofType, pos);
   }


   public  void load(Scanner in,ImageStore imageStore)
   {
      int lineNumber = 0;
      while (in.hasNextLine())
      {
         try
         {
            if (!this.processLine(in.nextLine(), imageStore))
            {
               System.err.println(String.format("invalid entry on line %d",
                       lineNumber));
            }
         }
         catch (NumberFormatException e)
         {
            System.err.println(String.format("invalid entry on line %d",
                    lineNumber));
         }
         catch (IllegalArgumentException e)
         {
            System.err.println(String.format("issue on line %d: %s",
                    lineNumber, e.getMessage()));
         }
         lineNumber++;
      }
   }

   public  boolean parseAtlantis(String [] properties,
                                 ImageStore imageStore)
   {
      if (properties.length == ATLANTIS_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[ATLANTIS_COL]),
                 Integer.parseInt(properties[ATLANTIS_ROW]));
         Entity entity = Atlantis.createAtlantis(properties[ATLANTIS_ID], //changed Entity to Atlatnis
                 pt, imageStore.getImageList(ATLANTIS_KEY));
         tryAddEntity(entity);
      }

      return properties.length == ATLANTIS_NUM_PROPERTIES;
   }



    public static Optional<Entity> nearestEntity(List<Entity> entities,
                                                 Point pos)
    {
        if (entities.isEmpty())
        {
            return Optional.empty();
        }
        else
        {
            Entity nearest = entities.get(0);
            int nearestDistance = Point.distanceSquared(nearest.position, pos);

            for (Entity other : entities)
            {
                int otherDistance = Point.distanceSquared(other.position, pos);

                if (otherDistance < nearestDistance)
                {
                    nearest = other;
                    nearestDistance = otherDistance;
                }
            }

            return Optional.of(nearest);
        }
    }









    ;
    public static final int FISH_NUM_PROPERTIES = 5;
    public static final int FISH_ID = 1;
    public static final int FISH_COL = 2;
    public static final int FISH_ROW = 3;
    public static final int FISH_ACTION_PERIOD = 4;

   public  boolean parseFish(String [] properties,
                                   ImageStore imageStore)
   {
      if (properties.length == FISH_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[FISH_COL]),
                 Integer.parseInt(properties[FISH_ROW]));
         Entity entity = Fish.createFish(properties[FISH_ID], //changed from Entity to Fish
                 pt, Integer.parseInt(properties[FISH_ACTION_PERIOD]),
                 imageStore.getImageList(FISH_KEY));
         tryAddEntity(entity);
      }

      return properties.length == FISH_NUM_PROPERTIES;
   }













    public static final int OCTO_NUM_PROPERTIES = 7;
    public static final int OCTO_ID = 1;
    public static final int OCTO_COL = 2;
    public static final int OCTO_ROW = 3;
    public static final int OCTO_LIMIT = 4;
    public static final int OCTO_ACTION_PERIOD = 5;
    public static final int OCTO_ANIMATION_PERIOD = 6;

    public  boolean parseOcto(String [] properties,
                                    ImageStore imageStore)
    {
        if (properties.length == OCTO_NUM_PROPERTIES)
        {
            Point pt = new Point(Integer.parseInt(properties[OCTO_COL]),
                    Integer.parseInt(properties[OCTO_ROW]));
            Entity entity = OctoNotFull.createOctoNotFull(properties[OCTO_ID], //changed from Entity.  to OctoFull.
                    Integer.parseInt(properties[OCTO_LIMIT]),
                    pt,
                    Integer.parseInt(properties[OCTO_ACTION_PERIOD]),
                    Integer.parseInt(properties[OCTO_ANIMATION_PERIOD]),
                    imageStore.getImageList(OCTO_KEY));
            tryAddEntity(entity);
        }

        return properties.length == OCTO_NUM_PROPERTIES;
    }












    public static final int SGRASS_NUM_PROPERTIES = 5;
    public static final int SGRASS_ID = 1;
    public static final int SGRASS_COL = 2;
    public static final int SGRASS_ROW = 3;
    public static final int SGRASS_ACTION_PERIOD = 4;

    public  boolean parseSgrass(String [] properties,
                                      ImageStore imageStore)
    {
        if (properties.length == SGRASS_NUM_PROPERTIES)
        {
            Point pt = new Point(Integer.parseInt(properties[SGRASS_COL]),
                    Integer.parseInt(properties[SGRASS_ROW]));
            Entity entity = SGrass.createSgrass(properties[SGRASS_ID], //hanged from Entity. to Sgrass.
                    pt,
                    Integer.parseInt(properties[SGRASS_ACTION_PERIOD]),
                    imageStore.getImageList(SGRASS_KEY));
            tryAddEntity(entity);
        }

        return properties.length == SGRASS_NUM_PROPERTIES;
    }






    public static final int OBSTACLE_NUM_PROPERTIES = 4;
    public static final int OBSTACLE_ID = 1;
    public static final int OBSTACLE_COL = 2;
    public static final int OBSTACLE_ROW = 3;

    public  boolean parseObstacle(String [] properties,
                                        ImageStore imageStore)
    {
        if (properties.length == OBSTACLE_NUM_PROPERTIES)
        {
            Point pt = new Point(
                    Integer.parseInt(properties[OBSTACLE_COL]),
                    Integer.parseInt(properties[OBSTACLE_ROW]));
            Entity entity = Obstacle.createObstacle(properties[OBSTACLE_ID], //.changed from Enttiy. to Obstacle.
                    pt, imageStore.getImageList(OBSTACLE_KEY));
            tryAddEntity(entity);
        }

        return properties.length == OBSTACLE_NUM_PROPERTIES;
    }




    public  void addEntity(Entity entity)
    {
        if (this.withinBounds(entity.position))
        {
            WorldModel.setOccupancyCell(this, entity.position, entity);
            this.entities.add(entity);
        }
    }

    public  void moveEntity(Entity entity, Point pos)
    {
        Point oldPos = entity.position;
        if (this.withinBounds(pos) && !pos.equals(oldPos))
        {
            WorldModel.setOccupancyCell(this, oldPos, null);
            this.removeEntityAt(pos);
            WorldModel.setOccupancyCell(this, pos, entity);
            entity.position = pos;
        }
    }





    public boolean withinReach(Point placement, Point goal, Function<Point, Stream<Point>> potentialNeighbors)
    {
        return potentialNeighbors.apply(placement).anyMatch(p -> p.equals(goal));
    }





    public boolean canPassThrough(Point pos)
    {
        return this.withinBounds(pos) && this.getOccupancyCell(pos) == null;
    }
}


