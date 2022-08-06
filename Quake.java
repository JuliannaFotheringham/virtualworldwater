import processing.core.PImage;

import java.util.List;

public class Quake extends Entity implements Movers {

    public static final String QUAKE_ID = "quake";
    public static final int QUAKE_ACTION_PERIOD = 1100;
    public static final int QUAKE_ANIMATION_PERIOD = 100;



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

    public Quake(EntityKind kind, String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod) {
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

    //added create quake from entity

    public static Entity createQuake(Point position, List<PImage> images)
    {
        return new Quake(EntityKind.QUAKE, QUAKE_ID, position, images,
                0, 0, QUAKE_ACTION_PERIOD, QUAKE_ANIMATION_PERIOD);
    }


    public  void scheduleActions(EventScheduler scheduler,   //created quakes schedule actions.
                                 WorldModel world, ImageStore imageStore){

        scheduleEvent(scheduler,
                Activity.createActivityAction(this, world, imageStore), ///chnged class from action to activity
                this.actionPeriod);
        scheduleEvent(scheduler,
                Animation.createAnimationAction(this, QUAKE_ANIMATION_REPEAT_COUNT),//changed class to animation
                Entity.getAnimationPeriod(this));

    }

    @Override //added execute action to quake class, all this really does is exedcute quake activity
    public void executeAction(EventScheduler scheduler) {
       executeQuakeActivity(this.world, this.imageStore,  // this.entity. removed
                scheduler);

    }


    public void executeQuakeActivity(WorldModel world,
                                     ImageStore imageStore, EventScheduler scheduler) {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }


//
//    public static Entity createQuake(Point position, List<PImage> images)
//    {
//        return new Entity(EntityKind.QUAKE, QUAKE_ID, position, images,
//                0, 0, QUAKE_ACTION_PERIOD, QUAKE_ANIMATION_PERIOD);
//    }
//    public static void executeQuakeActivity(Entity entity, WorldModel world,
//                                            ImageStore imageStore, EventScheduler scheduler)
//    {
//        EventScheduler.unscheduleAllEvents(scheduler, entity);
//        WorldModel.removeEntity(world, entity);
//    }
//


}
