import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Crab extends Entity implements Movers{
//added entity and movers
    private static final String QUAKE_KEY = "quake";
    public EntityKind kind;
    public String id;
    public Point position;
    public List<PImage> images;
    public int resourceLimit;
    public int resourceCount;
    public int actionPeriod;
    public int animationPeriod;


    //added these parameters when implementing execute action
    private Entity entity;
    private WorldModel world;
    private ImageStore imageStore;

    public Crab(EntityKind kind, String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod) {
        super(kind, id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod); //added super
        this.kind = kind;
        this.id = id;
        this.position = position;
        this.images = images;
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }

    //moved create crab from entity
    public static Entity createCrab(String id, Point position,
                                    int actionPeriod, int animationPeriod, List<PImage> images)
    {
        return new Crab(EntityKind.CRAB, id, position, images,
                0, 0, actionPeriod, animationPeriod);
    }

    //implement schedule actions for crab


    public  void scheduleActions(EventScheduler scheduler,
                                 WorldModel world, ImageStore imageStore){

        scheduleEvent(scheduler,
                Activity.createActivityAction(this, world, imageStore),///chnged class from action to activity
                this.actionPeriod);
        scheduleEvent(scheduler,
                Animation.createAnimationAction(this, 0), Entity.getAnimationPeriod(this));  //changed class to animation

    }

    @Override
    public void executeAction(EventScheduler scheduler) {
        executeCrabActivity(this.world,  //removed this.entity ince in same calss now
                this.imageStore, scheduler);

    }



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

//    public boolean moveToCrab(WorldModel world, Entity target, EventScheduler scheduler) {
//        if (Point.adjacent(this.position, target.position)) {
//            world.removeEntity(target);
//            EventScheduler.unscheduleAllEvents(scheduler, target);
//            return true;
//        } else {
//            Point nextPos = nextPositionCrab(world, target.position);
//
//            if (!this.position.equals(nextPos)) {
//                Optional<Entity> occupant = world.getOccupant(nextPos);
//                if (occupant.isPresent()) {
//                    EventScheduler.unscheduleAllEvents(scheduler, occupant.get());
//                }
//
//                world.moveEntity(this, nextPos);
//            }
//            return false;
//        }
//    }
//

//
//    public static Entity createCrab(String id, Point position,
//                                    int actionPeriod, int animationPeriod, List<PImage> images)
//    {
//        return new Entity(EntityKind.CRAB, id, position, images,
//                0, 0, actionPeriod, animationPeriod);
//    }
//
//
//    public static void executeCrabActivity(Entity entity, WorldModel world,
//                                           ImageStore imageStore, EventScheduler scheduler)
//    {
//        Optional<Entity> crabTarget = WorldModel.findNearest(world,
//                entity.position, EntityKind.SGRASS);
//        long nextPeriod = entity.actionPeriod;
//
//        if (crabTarget.isPresent())
//        {
//            Point tgtPos = crabTarget.get().position;
//
//            if (moveToCrab(entity, world, crabTarget.get(), scheduler))
//            {
//                Entity quake = Quake.createQuake(tgtPos,
//                        ImageStore.getImageList(imageStore, QUAKE_KEY));
//
//                Entity.addEntity(world, quake);
//                nextPeriod += entity.actionPeriod;
//                EventScheduler.scheduleActions(quake, scheduler, world, imageStore);
//            }
//        }
//
//       EventScheduler.scheduleEvent(scheduler, entity,
//               Activity.createActivityAction(entity, world, imageStore),
//                nextPeriod);
//    }
//
//    public static boolean moveToCrab(Entity crab, WorldModel world,
//                                     Entity target, EventScheduler scheduler)
//    {
//        if (Point.adjacent(crab.position, target.position))
//        {
//            WorldModel.removeEntity(world, target);
//            EventScheduler.unscheduleAllEvents(scheduler, target);
//            return true;
//        }
//        else
//        {
//            Point nextPos = nextPositionCrab(crab, world, target.position);
//
//            if (!crab.position.equals(nextPos))
//            {
//                Optional<Entity> occupant =  WorldModel.getOccupant(world, nextPos);
//                if (occupant.isPresent())
//                {
//                    EventScheduler.unscheduleAllEvents(scheduler, occupant.get());
//                }
//
//                Entity.moveEntity(world, crab, nextPos);
//            }
//            return false;
//        }
//    }
//
//    public static Point nextPositionCrab(Entity entity, WorldModel world,
//                                         Point destPos)
//    {
//        int horiz = Integer.signum(destPos.x - entity.position.x);
//        Point newPos = new Point(entity.position.x + horiz,
//                entity.position.y);
//
//        Optional<Entity> occupant =  WorldModel.getOccupant(world, newPos);
//
//        if (horiz == 0 ||
//                (occupant.isPresent() && !(occupant.get().kind == EntityKind.FISH)))
//        {
//            int vert = Integer.signum(destPos.y - entity.position.y);
//            newPos = new Point(entity.position.x, entity.position.y + vert);
//            occupant =  WorldModel.getOccupant(world, newPos);
//
//            if (vert == 0 ||
//                    (occupant.isPresent() && !(occupant.get().kind == EntityKind.FISH)))
//            {
//                newPos = entity.position;
//            }
//        }
//
//        return newPos;
//    }



}
