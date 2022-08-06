import processing.core.PImage;

import java.util.List;
import java.util.Random;

public class Fish extends Entity implements Movers{
    //added extensions

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

    public Fish(EntityKind kind, String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod) {
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

    //adderd create fish from enttit
    public static Entity createFish(String id, Point position, int actionPeriod,
                                    List<PImage> images)
    {
        return new Fish(EntityKind.FISH, id, position, images, 0, 0,
                actionPeriod, 0);
    }


    //  add schedule actions for Fish

    public  void scheduleActions(EventScheduler scheduler,
                                 WorldModel world, ImageStore imageStore){

        scheduleEvent(scheduler,
                Activity.createActivityAction(this, world, imageStore),///chnged class from action to activity
                this.actionPeriod);

    }

    @Override //implemented execute action for fish
    public void executeAction(EventScheduler scheduler) {
       executeFishActivity(this.world, this.imageStore, //removed  this.entity. since in same class
                scheduler);

    }


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



}
//
//    public static Entity createFish(String id, Point position, int actionPeriod,
//                                    List<PImage> images)
//    {
//        return new Entity(EntityKind.FISH, id, position, images, 0, 0,
//                actionPeriod, 0);
//    }
//
//
//
//
//    public static void executeFishActivity(Entity entity, WorldModel world,
//                                           ImageStore imageStore, EventScheduler scheduler)
//    {
//        Point pos = entity.position;  // store current position before removing
//
//        WorldModel.removeEntity(world, entity);
//        EventScheduler.unscheduleAllEvents(scheduler, entity);
//
//        Entity crab = Entity.createCrab(entity.id + CRAB_ID_SUFFIX,
//                pos, entity.actionPeriod / CRAB_PERIOD_SCALE,
//                CRAB_ANIMATION_MIN +
//                        rand.nextInt(CRAB_ANIMATION_MAX - CRAB_ANIMATION_MIN),
//                ImageStore.getImageList(imageStore, CRAB_KEY));
//
//        Entity.addEntity(world, crab);
//        EventScheduler.scheduleActions(crab, scheduler, world, imageStore);
//    }
//
//
//    public static boolean parseFish(String [] properties, WorldModel world,
//                                    ImageStore imageStore)
//    {
//        if (properties.length == FISH_NUM_PROPERTIES)
//        {
//            Point pt = new Point(Integer.parseInt(properties[FISH_COL]),
//                    Integer.parseInt(properties[FISH_ROW]));
//            Entity entity = Fish.createFish(properties[FISH_ID],
//                    pt, Integer.parseInt(properties[FISH_ACTION_PERIOD]),
//                    ImageStore.getImageList(imageStore, FISH_KEY));
//            WorldModel.tryAddEntity(world, entity);
//        }
//
//        return properties.length == FISH_NUM_PROPERTIES;
//    }
//}
