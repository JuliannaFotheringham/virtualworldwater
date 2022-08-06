import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class OctoFull extends Entity implements Movers {
    //added extensions

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

    public OctoFull(EntityKind kind, String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod) {
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

    //added create octo full from entity
    public static Entity createOctoFull(String id, int resourceLimit,
                                        Point position, int actionPeriod, int animationPeriod,
                                        List<PImage> images)
    {
        return new OctoFull(EntityKind.OCTO_FULL, id, position, images,
                resourceLimit, resourceLimit, actionPeriod, animationPeriod);
    }


    //add schedule actions for octofull

    public  void scheduleActions(EventScheduler scheduler,
                                 WorldModel world, ImageStore imageStore){

        scheduleEvent(scheduler,
                Activity.createActivityAction(this, world, imageStore),  ///chnged class from action to activity
                this.actionPeriod);
        scheduleEvent(scheduler,  Animation.createAnimationAction(this, 0),  //changed class to animation from action
                Entity.getAnimationPeriod(this));

    }

    @Override //implemented execute action for octo full
    public void executeAction(EventScheduler scheduler) {
        executeOctoFullActivity(this.world,  //removed this.entity. since in same class now
                this.imageStore, scheduler);

    }

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



}
//
//    public static Entity createOctoFull(String id, int resourceLimit,
//                                        Point position, int actionPeriod, int animationPeriod,
//                                        List<PImage> images)
//    {
//        return new Entity(EntityKind.OCTO_FULL, id, position, images,
//                resourceLimit, resourceLimit, actionPeriod, animationPeriod);
//    }
//
//
//
//
//
//
//    public static void executeOctoFullActivity(Entity entity, WorldModel world,
//                                               ImageStore imageStore, EventScheduler scheduler)
//    {
//        Optional<Entity> fullTarget = WorldModel.findNearest(world, entity.position,
//                EntityKind.ATLANTIS);
//
//        if (fullTarget.isPresent() &&
//                moveToFull(entity, world, fullTarget.get(), scheduler))
//        {
//            //at atlantis trigger animation
//            EventScheduler.scheduleActions(fullTarget.get(), scheduler, world, imageStore);
//
//            //transform to unfull
//            transformFull(entity, world, scheduler, imageStore);
//        }
//        else
//        {
//            EventScheduler.scheduleEvent(scheduler, entity,
//                    Activity.createActivityAction(entity, world, imageStore),
//                    entity.actionPeriod);
//        }
//    }
//
//
//
//    public static boolean moveToFull(Entity octo, WorldModel world,
//                                     Entity target, EventScheduler scheduler)
//    {
//        if (Point.adjacent(octo.position, target.position))
//        {
//            return true;
//        }
//        else
//        {
//            Point nextPos = nextPositionOcto(octo, world, target.position);
//
//            if (!octo.position.equals(nextPos))
//            {
//                Optional<Entity> occupant =  WorldModel.getOccupant(world, nextPos);
//                if (occupant.isPresent())
//                {
//                    EventScheduler.unscheduleAllEvents(scheduler, occupant.get());
//                }
//
//                Entity.moveEntity(world, octo, nextPos);
//            }
//            return false;
//        }
//    }
//
//
//
//
//
//    public static void transformFull(Entity entity, WorldModel world,
//                                     EventScheduler scheduler, ImageStore imageStore)
//    {
//        Entity octo = OctoNotFull.createOctoNotFull(entity.id, entity.resourceLimit,
//                entity.position, entity.actionPeriod, entity.animationPeriod,
//                entity.images);
//
//        WorldModel.removeEntity(world, entity);
//        EventScheduler.unscheduleAllEvents(scheduler, entity);
//
//        Entity.addEntity(world, octo);
//        EventScheduler.scheduleActions(octo, scheduler, world, imageStore);
//    }
//
//
//
//
//}
