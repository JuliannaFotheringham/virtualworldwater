import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import processing.core.PImage;

/*
Entity ideally would includes functions for how all the entities in our virtual world might act...
 */


public  abstract class  Entity //changed to abstract
{
   public EntityKind kind;
   public String id;
   public Point position;
   public List<PImage> images;
   public int imageIndex;
   public int resourceLimit;
   public int resourceCount;
   public int actionPeriod;
   public int animationPeriod;


   public static final int ATLANTIS_ANIMATION_REPEAT_COUNT = 7;
   public static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;

   public Entity(EntityKind kind, String id, Point position,
                 List<PImage> images, int resourceLimit, int resourceCount,
                 int actionPeriod, int animationPeriod) {
      this.kind = kind;
      this.id = id;
      this.position = position;
      this.images = images;
      this.imageIndex = 0;
      this.resourceLimit = resourceLimit;
      this.resourceCount = resourceCount;
      this.actionPeriod = actionPeriod;
      this.animationPeriod = animationPeriod;
   }


   public static PImage getCurrentImage(Object entity) {
      if (entity instanceof Background) {
         return ((Background) entity).images
                 .get(((Background) entity).imageIndex);
      } else if (entity instanceof Entity) {
         return ((Entity) entity).images.get(((Entity) entity).imageIndex);
      } else {
         throw new UnsupportedOperationException(
                 String.format("getCurrentImage not supported for %s",
                         entity));
      }
   }

   public static void nextImage(Entity entity) {
      entity.imageIndex = (entity.imageIndex + 1) % entity.images.size();
   }


   public static int getAnimationPeriod(Entity entity) {
      switch (entity.kind) {
         case OCTO_FULL:
         case OCTO_NOT_FULL:
         case CRAB:
         case QUAKE:
         case ATLANTIS:
            return entity.animationPeriod;
         default:
            throw new UnsupportedOperationException(
                    String.format("getAnimationPeriod not supported for %s",
                            entity.kind));
      }
   }



   public Point getPosition() {
      return position;
   }


   public void scheduleActions(EventScheduler scheduler,
                               WorldModel world, ImageStore imageStore) {
      switch (this.kind) {
         case OCTO_FULL:
            scheduleEvent(scheduler,
                    Activity.createActivityAction(this, world, imageStore),  ///chnged class from action to activity
                    this.actionPeriod);
            scheduleEvent(scheduler, Animation.createAnimationAction(this, 0),  //changed class to animation from action
                    Entity.getAnimationPeriod(this));
            break;

         case OCTO_NOT_FULL:
            scheduleEvent(scheduler,
                    Activity.createActivityAction(this, world, imageStore),  ///chnged class from action to activity
                    this.actionPeriod);
            scheduleEvent(scheduler,
                    Animation.createAnimationAction(this, 0), Entity.getAnimationPeriod(this));   //changed class to animation
            break;

         case FISH:
            scheduleEvent(scheduler,
                    Activity.createActivityAction(this, world, imageStore),///chnged class from action to activity
                    this.actionPeriod);
            break;

         case CRAB:
            scheduleEvent(scheduler,
                    Activity.createActivityAction(this, world, imageStore),///chnged class from action to activity
                    this.actionPeriod);
            scheduleEvent(scheduler,
                    Animation.createAnimationAction(this, 0), Entity.getAnimationPeriod(this));  //changed class to animation
            break;

         case QUAKE:
            scheduleEvent(scheduler,
                    Activity.createActivityAction(this, world, imageStore), ///chnged class from action to activity
                    this.actionPeriod);
            scheduleEvent(scheduler,
                    Animation.createAnimationAction(this, QUAKE_ANIMATION_REPEAT_COUNT),//changed class to animation
                    Entity.getAnimationPeriod(this));
            break;

         case SGRASS:
            scheduleEvent(scheduler,
                    Activity.createActivityAction(this, world, imageStore),  ///chnged class from action to activity
                    this.actionPeriod);
            break;
         case ATLANTIS:
            scheduleEvent(scheduler,
                    Animation.createAnimationAction(this, ATLANTIS_ANIMATION_REPEAT_COUNT),  //changed class to animation
                    Entity.getAnimationPeriod(this));
            break;

         default:
      }
   }


   public void scheduleEvent(EventScheduler scheduler,
                             Action action, long afterPeriod) {
      long time = System.currentTimeMillis() +
              (long) (afterPeriod * scheduler.timeScale);
      Event event = new Event(action, time, this);

      scheduler.eventQueue.add(event);

      // update list of pending events for the given entity
      List<Event> pending = scheduler.pendingEvents.getOrDefault(this,
              new LinkedList<>());
      pending.add(event);
      scheduler.pendingEvents.put(this, pending);
   }


   //atlatnis start


   public static final String ATLANTIS_KEY = "atlantis";
   public static final int ATLANTIS_NUM_PROPERTIES = 4;
   public static final int ATLANTIS_ID = 1;
   public static final int ATLANTIS_COL = 2;
   public static final int ATLANTIS_ROW = 3;


   public void executeAtlantisActivity(WorldModel world,
                                       ImageStore imageStore, EventScheduler scheduler) {
      scheduler.unscheduleAllEvents(this);
      world.removeEntity(this);
   }

//
//   public  boolean parseAtlantis(String [] properties, WorldModel world,
//                                       ImageStore imageStore)
//   {
//      if (properties.length == ATLANTIS_NUM_PROPERTIES)
//      {
//         Point pt = new Point(Integer.parseInt(properties[ATLANTIS_COL]),
//                 Integer.parseInt(properties[ATLANTIS_ROW]));
//         Entity entity = createAtlantis(properties[ATLANTIS_ID],
//                 pt, ImageStore.getImageList(imageStore, ATLANTIS_KEY));
//         WorldModel.tryAddEntity(world, entity);
//      }
//
//      return properties.length == ATLANTIS_NUM_PROPERTIES;
//   }


   ///atlantis done


   //CRAB START

   //   import processing.core.PImage;
//
//import java.util.List;
//import java.util.Optional;
//
//   public class Crab {
//
   private static final String QUAKE_KEY = "quake";
//      public EntityKind kind;
//      public String id;
//      public Point position;
//      public List<PImage> images;
//      public int resourceLimit;
//      public int resourceCount;
//      public int actionPeriod;
//      public int animationPeriod;
//
//      public Crab(EntityKind kind, String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod) {
//         this.kind = kind;
//         this.id = id;
//         this.position = position;
//         this.images = images;
//         this.resourceLimit = resourceLimit;
//         this.resourceCount = resourceCount;
//         this.actionPeriod = actionPeriod;
//         this.animationPeriod = animationPeriod;
//      }

//      public static Entity createCrab(String id, Point position,
//                                      int actionPeriod, int animationPeriod, List<PImage> images)
//      {
//         return new Crab(EntityKind.CRAB, id, position, images,
//                 0, 0, actionPeriod, animationPeriod);
//      }


   public void executeCrabActivity(WorldModel world,
                                   ImageStore imageStore, EventScheduler scheduler) {
      Optional<Entity> crabTarget = world.findNearest(
              this.position, EntityKind.SGRASS);
      long nextPeriod = this.actionPeriod;

      if (crabTarget.isPresent()) {
         Point tgtPos = crabTarget.get().position;

         if (moveToCrab(world, crabTarget.get(), scheduler)) {
            Entity quake = Quake.createQuake(tgtPos,  //qualified with Quake.
                    imageStore.getImageList(QUAKE_KEY));

            world.addEntity(quake);
            nextPeriod += this.actionPeriod;
            quake.scheduleActions(scheduler, world, imageStore);
         }
      }

      scheduleEvent(scheduler,
              Activity.createActivityAction(this, world, imageStore),  ///chnged class from action to activity
              nextPeriod);
   }

   public boolean moveToCrab(WorldModel world, Entity target, EventScheduler scheduler) {
      if (Point.adjacent(this.position, target.position)) {
         world.removeEntity(target);
         scheduler.unscheduleAllEvents(target);
         return true;
      } else {
         Point nextPos = nextPositionCrab(world, target.position);

         if (!this.position.equals(nextPos)) {
            Optional<Entity> occupant = world.getOccupant(nextPos);
            if (occupant.isPresent()) {
               scheduler.unscheduleAllEvents(occupant.get());
            }

            world.moveEntity(this, nextPos);
         }
         return false;
      }
   }

   public Point nextPositionCrab(WorldModel world,
                                 Point destPos) {
      int horiz = Integer.signum(destPos.x - this.position.x);
      Point newPos = new Point(this.position.x + horiz,
              this.position.y);

      Optional<Entity> occupant = world.getOccupant(newPos);

      if (horiz == 0 ||
              (occupant.isPresent() && !(occupant.get().kind == EntityKind.FISH))) {
         int vert = Integer.signum(destPos.y - this.position.y);
         newPos = new Point(this.position.x, this.position.y + vert);
         occupant = world.getOccupant(newPos);

         if (vert == 0 ||
                 (occupant.isPresent() && !(occupant.get().kind == EntityKind.FISH))) {
            newPos = this.position;
         }
      }

      return newPos;
   }


   // END CRAB


   //START FISH


   //import processing.core.PImage;
//
//import java.util.List;
//import java.util.Random;
//
//   public class Fish {
//
   public static final Random rand = new Random();

   private static final String CRAB_KEY = "crab";
   private static final String CRAB_ID_SUFFIX = " -- crab";
   private static final int CRAB_PERIOD_SCALE = 4;
   private static final int CRAB_ANIMATION_MIN = 50;
   private static final int CRAB_ANIMATION_MAX = 150;

   public static final String FISH_KEY = "fish";
   public static final int FISH_NUM_PROPERTIES = 5;
   public static final int FISH_ID = 1;
   public static final int FISH_COL = 2;
   public static final int FISH_ROW = 3;
   public static final int FISH_ACTION_PERIOD = 4;
//
//      public EntityKind kind;
//      public String id;
//      public Point position;
//      public List<PImage> images;
//      public int resourceLimit;
//      public int resourceCount;
//      public int actionPeriod;
//      public int animationPeriod;
//
//      public Fish(EntityKind kind, String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod) {
//         this.kind = kind;
//         this.id = id;
//         this.position = position;
//         this.images = images;
//         this.resourceLimit = resourceLimit;
//         this.resourceCount = resourceCount;
//         this.actionPeriod = actionPeriod;
//         this.animationPeriod = animationPeriod;
//      }

//      public static Entity createFish(String id, Point position, int actionPeriod,
//                                      List<PImage> images)
//      {
//         return new Fish(EntityKind.FISH, id, position, images, 0, 0,
//                 actionPeriod, 0);
//      }


   public void executeFishActivity(WorldModel world,
                                   ImageStore imageStore, EventScheduler scheduler) {
      Point pos = this.position;  // store current position before removing

      world.removeEntity(this);
      scheduler.unscheduleAllEvents(this);

      Entity crab = Crab.createCrab(this.id + CRAB_ID_SUFFIX, //changed from Entity to Crab
              pos, this.actionPeriod / CRAB_PERIOD_SCALE,
              CRAB_ANIMATION_MIN +
                      rand.nextInt(CRAB_ANIMATION_MAX - CRAB_ANIMATION_MIN),
              imageStore.getImageList(CRAB_KEY));

      world.addEntity(crab);
      crab.scheduleActions(scheduler, world, imageStore);
   }


//      public static boolean parseFish(String [] properties, WorldModel world,
//                                      ImageStore imageStore)
//      {
//         if (properties.length == FISH_NUM_PROPERTIES)
//         {
//            Point pt = new Point(Integer.parseInt(properties[FISH_COL]),
//                    Integer.parseInt(properties[FISH_ROW]));
//            Entity entity = Fish.createFish(properties[FISH_ID],
//                    pt, Integer.parseInt(properties[FISH_ACTION_PERIOD]),
//                    ImageStore.getImageList(imageStore, FISH_KEY));
//            WorldModel.tryAddEntity(world, entity);
//         }
//
//         return properties.length == FISH_NUM_PROPERTIES;
//      }
//   }


   //END FISH


   //START OCTO
//
//   public class Octo {
//
//      public static final String OCTO_KEY = "octo";
//      public static final int OCTO_NUM_PROPERTIES = 7;
//      public static final int OCTO_ID = 1;
//      public static final int OCTO_COL = 2;
//      public static final int OCTO_ROW = 3;
//      public static final int OCTO_LIMIT = 4;
//      public static final int OCTO_ACTION_PERIOD = 5;
//      public static final int OCTO_ANIMATION_PERIOD = 6;

//      public static boolean parseOcto(String [] properties, WorldModel world,
//                                      ImageStore imageStore)
//      {
//         if (properties.length == OCTO_NUM_PROPERTIES)
//         {
//            Point pt = new Point(Integer.parseInt(properties[OCTO_COL]),
//                    Integer.parseInt(properties[OCTO_ROW]));
//            Entity entity = createOctoNotFull(properties[OCTO_ID],
//                    Integer.parseInt(properties[OCTO_LIMIT]),
//                    pt,
//                    Integer.parseInt(properties[OCTO_ACTION_PERIOD]),
//                    Integer.parseInt(properties[OCTO_ANIMATION_PERIOD]),
//                    ImageStore.getImageList(imageStore, OCTO_KEY));
//            WorldModel.tryAddEntity(world, entity);
//         }
//
//         return properties.length == OCTO_NUM_PROPERTIES;
//      }

   public Point nextPositionOcto(WorldModel world,
                                 Point destPos) {
      int horiz = Integer.signum(destPos.x - this.position.x);
      Point newPos = new Point(this.position.x + horiz,
              this.position.y);

      if (horiz == 0 || world.isOccupied(newPos)) {
         int vert = Integer.signum(destPos.y - this.position.y);
         newPos = new Point(this.position.x,
                 this.position.y + vert);

         if (vert == 0 || world.isOccupied(newPos)) {
            newPos = this.position;
         }
      }

      return newPos;
   }


   //END OCTO


//START OCTOFULL




   public void executeOctoFullActivity(WorldModel world,
                                       ImageStore imageStore, EventScheduler scheduler) {
      Optional<Entity> fullTarget = world.findNearest(this.position,
              EntityKind.ATLANTIS);

      if (fullTarget.isPresent() &&
              moveToFull(world, fullTarget.get(), scheduler)) {
         //at atlantis trigger animation
         fullTarget.get().scheduleActions(scheduler, world, imageStore); /////could this be problmatic

         //transform to unfull
         transformFull(world, scheduler, imageStore);
      } else {
         scheduleEvent(scheduler,
                 Activity.createActivityAction(this, world, imageStore),  ///chnged class from action to activity
                 this.actionPeriod);
      }
   }


   public boolean moveToFull(WorldModel world,
                             Entity target, EventScheduler scheduler) {
      if (Point.adjacent(this.position, target.position)) {
         return true;
      } else {
         Point nextPos = nextPositionOcto(world, target.position);

         if (!this.position.equals(nextPos)) {
            Optional<Entity> occupant = world.getOccupant(nextPos);
            if (occupant.isPresent()) {
               scheduler.unscheduleAllEvents(occupant.get());
            }

            world.moveEntity(this, nextPos);
         }
         return false;
      }
   }


   public void transformFull(WorldModel world,
                             EventScheduler scheduler, ImageStore imageStore) {
      Entity octo = OctoNotFull.createOctoNotFull(this.id, this.resourceLimit, //added OctoNotFull.
              this.position, this.actionPeriod, this.animationPeriod,
              this.images);

      world.removeEntity(this);
      scheduler.unscheduleAllEvents(this);

      world.addEntity(octo);
      octo.scheduleActions(scheduler, world, imageStore);
   }


//END OCTO FULL


//START OCTO NOT FULL





   public void executeOctoNotFullActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
      Optional<Entity> notFullTarget = world.findNearest(this.position,
              EntityKind.FISH);

      if (!notFullTarget.isPresent() ||
              !moveToNotFull(world, notFullTarget.get(), scheduler) ||
              !transformNotFull(world, scheduler, imageStore)) {
         scheduleEvent(scheduler,
                 Activity.createActivityAction(this, world, imageStore),  ///chnged class from action to activity
                 this.actionPeriod);
      }
   }


   public boolean moveToNotFull(WorldModel world,
                                Entity target, EventScheduler scheduler) {
      if (Point.adjacent(this.position, target.position)) {
         this.resourceCount += 1;
         world.removeEntity(target);
         scheduler.unscheduleAllEvents(target);

         return true;
      } else {
         Point nextPos = nextPositionOcto(world, target.position);

         if (!this.position.equals(nextPos)) {
            Optional<Entity> occupant = world.getOccupant(nextPos);
            if (occupant.isPresent()) {
               scheduler.unscheduleAllEvents(occupant.get());
            }

            world.moveEntity(this, nextPos);
         }
         return false;
      }
   }


   public boolean transformNotFull(WorldModel world,
                                   EventScheduler scheduler, ImageStore imageStore) {
      if (this.resourceCount >= this.resourceLimit) {
         Entity octo = OctoFull.createOctoFull(this.id, this.resourceLimit, //added OctoFull in fromt of create octo
                 this.position, this.actionPeriod, this.animationPeriod,
                 this.images);

         world.removeEntity(this);
         scheduler.unscheduleAllEvents(this);

         world.addEntity(octo);
         octo.scheduleActions(scheduler, world, imageStore);

         return true;
      }

      return false;


   }


//END OCTO NOT FULL


//START QUAKE

   public void executeQuakeActivity(WorldModel world,
                                    ImageStore imageStore, EventScheduler scheduler) {
      scheduler.unscheduleAllEvents(this);
      world.removeEntity(this);
   }


//END QUAKE


//START SGRASS



   private static final String FISH_ID_PREFIX = "fish -- ";
   private static final int FISH_CORRUPT_MIN = 20000;
   private static final int FISH_CORRUPT_MAX = 30000;


   public void executeSgrassActivity(WorldModel world,
                                     ImageStore imageStore, EventScheduler scheduler) {
      Optional<Point> openPt = world.findOpenAround(this.position);

      if (openPt.isPresent()) {
         Entity fish = Fish.createFish(FISH_ID_PREFIX + this.id, //changed from Entity to Fish
                 openPt.get(), FISH_CORRUPT_MIN +
                         rand.nextInt(FISH_CORRUPT_MAX - FISH_CORRUPT_MIN),
                 imageStore.getImageList(FISH_KEY));
         world.addEntity(fish);
         fish.scheduleActions(scheduler, world, imageStore);
      }

      scheduleEvent(scheduler,
              Activity.createActivityAction(this, world, imageStore),  ///chnged class from action to activity
              this.actionPeriod);
   }


   //END SGRASS


}





