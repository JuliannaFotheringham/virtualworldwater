import processing.core.PImage;
import java.util.Random;

import java.util.List;
import java.util.Optional;

public class SGrass extends Entity implements Movers {  //added entity and movers

    public static final Random rand = new Random();

    private static final String FISH_ID_PREFIX = "fish -- ";
    private static final int FISH_CORRUPT_MIN = 20000;
    private static final int FISH_CORRUPT_MAX = 30000;
    private static final String FISH_KEY = "fish";

    public static final String SGRASS_KEY = "seaGrass";
    public static final int SGRASS_NUM_PROPERTIES = 5;
    public static final int SGRASS_ID = 1;
    public static final int SGRASS_COL = 2;
    public static final int SGRASS_ROW = 3;
    public static final int SGRASS_ACTION_PERIOD = 4;

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

    public SGrass(EntityKind kind, String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod) {
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


    //changed createsgrass from entity
    public static Entity createSgrass(String id, Point position, int actionPeriod,
                                      List<PImage> images)
    {
        return new SGrass(EntityKind.SGRASS, id, position, images, 0, 0,
                actionPeriod, 0);
    }


    public  void scheduleActions(EventScheduler scheduler,   //creates sgrass schedule actions, breaking up switch
                                 WorldModel world, ImageStore imageStore){
        scheduleEvent(scheduler,
                Activity .createActivityAction(this, world, imageStore),  ///chnged class from action to activity
                this.actionPeriod);

    }

    @Override //implemented execute action from movers
    public void executeAction(EventScheduler scheduler) {
        this.entity.executeSgrassActivity(this.world, this.imageStore,
                scheduler);

    }

    //moved her but hasnt been implemented yet

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

//
//    public static Entity createSgrass(String id, Point position, int actionPeriod,
//                                      List<PImage> images)
//    {
//        return new Entity(EntityKind.SGRASS, id, position, images, 0, 0,
//                actionPeriod, 0);
//    }
//
//    public static void executeSgrassActivity(Entity entity, WorldModel world,
//                                             ImageStore imageStore, EventScheduler scheduler)
//    {
//        Optional<Point> openPt = WorldModel.findOpenAround(world, entity.position);
//
//        if (openPt.isPresent())
//        {
//            Entity fish = Entity.createFish(FISH_ID_PREFIX + entity.id,
//                    openPt.get(), FISH_CORRUPT_MIN +
//                            rand.nextInt(FISH_CORRUPT_MAX - FISH_CORRUPT_MIN),
//                    ImageStore.getImageList(imageStore,FISH_KEY));
//            Entity.addEntity(world, fish);
//            EventScheduler.scheduleActions(fish, scheduler, world, imageStore);
//        }
//
//        EventScheduler.scheduleEvent(scheduler, entity,
//                Activity.createActivityAction(entity, world, imageStore),
//                entity.actionPeriod);
//    }
//
//    public static boolean parseSgrass(String [] properties, WorldModel world,
//                                      ImageStore imageStore)
//    {
//        if (properties.length == SGRASS_NUM_PROPERTIES)
//        {
//            Point pt = new Point(Integer.parseInt(properties[SGRASS_COL]),
//                    Integer.parseInt(properties[SGRASS_ROW]));
//            Entity entity = SGrass.createSgrass(properties[SGRASS_ID],
//                    pt,
//                    Integer.parseInt(properties[SGRASS_ACTION_PERIOD]),
//                    ImageStore.getImageList(imageStore, SGRASS_KEY));
//            WorldModel.tryAddEntity(world, entity);
//        }
//
//        return properties.length == SGRASS_NUM_PROPERTIES;
//    }
//


}
