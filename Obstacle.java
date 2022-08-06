import processing.core.PImage;

import java.util.List;

public class Obstacle extends Entity {

    public EntityKind kind;
    public String id;
    public Point position;
    public List<PImage> images;
    public int resourceLimit;
    public int resourceCount;
    public int actionPeriod;
    public int animationPeriod;

    public static final String OBSTACLE_KEY = "obstacle";
    public static final int OBSTACLE_NUM_PROPERTIES = 4;
    public static final int OBSTACLE_ID = 1;
    public static final int OBSTACLE_COL = 2;
    public static final int OBSTACLE_ROW = 3;

    public Obstacle(EntityKind kind, String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod) {
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
//added create obstace from enttiy
    public static Entity createObstacle(String id, Point position,
                                        List<PImage> images)
    {
        return new Obstacle(EntityKind.OBSTACLE, id, position, images,
                0, 0, 0, 0);
    }

}

//
//    public static boolean parseObstacle(String [] properties, WorldModel world,
//                                        ImageStore imageStore)
//    {
//        if (properties.length == OBSTACLE_NUM_PROPERTIES)
//        {
//            Point pt = new Point(
//                    Integer.parseInt(properties[OBSTACLE_COL]),
//                    Integer.parseInt(properties[OBSTACLE_ROW]));
//            Entity entity = Obstacle.createObstacle(properties[OBSTACLE_ID],
//                    pt, ImageStore.getImageList(imageStore, OBSTACLE_KEY));
//            WorldModel.tryAddEntity(world, entity);
//        }
//
//        return properties.length == OBSTACLE_NUM_PROPERTIES;
//    }
//}
