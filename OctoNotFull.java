import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class OctoNotFull extends Entity implements Movers {

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

    public OctoNotFull(EntityKind kind, String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod) {
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

    //added create octo not full from entity

    public static Entity createOctoNotFull(String id, int resourceLimit,
                                           Point position, int actionPeriod, int animationPeriod,
                                           List<PImage> images)
    {
        return new OctoNotFull(EntityKind.OCTO_NOT_FULL, id, position, images,
                resourceLimit, 0, actionPeriod, animationPeriod);
    }



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



    //add schedule actions for octo not full
    public  void scheduleActions(EventScheduler scheduler,
                                 WorldModel world, ImageStore imageStore){
        scheduleEvent(scheduler,
                Activity.createActivityAction(this, world, imageStore),  ///chnged class from action to activity
                this.actionPeriod);
        scheduleEvent(scheduler,
                Animation.createAnimationAction(this, 0), Entity.getAnimationPeriod(this));   //changed class to animation


    }

    @Override //implemented octofull execute action from activity class
    public void executeAction(EventScheduler scheduler) {
        executeOctoNotFullActivity(this.world, //removed this.entity.   executeOctoNotFullActivit
                this.imageStore, scheduler);
    }
}
//
//    public static Entity createOctoNotFull(String id, int resourceLimit,
//                                           Point position, int actionPeriod, int animationPeriod,
//                                           List<PImage> images)
//    {
//        return new Entity(EntityKind.OCTO_NOT_FULL, id, position, images,
//                resourceLimit, 0, actionPeriod, animationPeriod);
//    }
//
//
//    public static void executeOctoNotFullActivity(Entity entity,
//                                                  WorldModel world, ImageStore imageStore, EventScheduler scheduler)
//    {
//        Optional<Entity> notFullTarget = WorldModel.findNearest(world, entity.position,
//                EntityKind.FISH);
//
//        if (!notFullTarget.isPresent() ||
//                !moveToNotFull(entity, world, notFullTarget.get(), scheduler) ||
//                !transformNotFull(entity, world, scheduler, imageStore))
//        {
//            EventScheduler.scheduleEvent(scheduler, entity,
//                    Activity.createActivityAction(entity, world, imageStore),
//                    entity.actionPeriod);
//        }
//    }
//
//
//    public static boolean moveToNotFull(Entity octo, WorldModel world,
//                                        Entity target, EventScheduler scheduler)
//    {
//        if (Point.adjacent(octo.position, target.position))
//        {
//            octo.resourceCount += 1;
//            WorldModel.removeEntity(world, target);
//            EventScheduler.unscheduleAllEvents(scheduler, target);
//
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
//    public static boolean transformNotFull(Entity entity, WorldModel world,
//                                           EventScheduler scheduler, ImageStore imageStore)
//    {
//        if (entity.resourceCount >= entity.resourceLimit)
//        {
//            Entity octo = OctoFull.createOctoFull(entity.id, entity.resourceLimit,
//                    entity.position, entity.actionPeriod, entity.animationPeriod,
//                    entity.images);
//
//            WorldModel.removeEntity(world, entity);
//            EventScheduler.unscheduleAllEvents(scheduler, entity);
//
//            Entity.addEntity(world, octo);
//            EventScheduler.scheduleActions(octo, scheduler, world, imageStore);
//
//            return true;
//        }
//
//        return false;
//
//
//    }
//
//}
