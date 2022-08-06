//public class Octo {
//
//    public static final String OCTO_KEY = "octo";
//    public static final int OCTO_NUM_PROPERTIES = 7;
//    public static final int OCTO_ID = 1;
//    public static final int OCTO_COL = 2;
//    public static final int OCTO_ROW = 3;
//    public static final int OCTO_LIMIT = 4;
//    public static final int OCTO_ACTION_PERIOD = 5;
//    public static final int OCTO_ANIMATION_PERIOD = 6;
//
//    public static boolean parseOcto(String [] properties, WorldModel world,
//                                    ImageStore imageStore)
//    {
//        if (properties.length == OCTO_NUM_PROPERTIES)
//        {
//            Point pt = new Point(Integer.parseInt(properties[OCTO_COL]),
//                    Integer.parseInt(properties[OCTO_ROW]));
//            Entity entity = OctoNotFull.createOctoNotFull(properties[OCTO_ID],
//                    Integer.parseInt(properties[OCTO_LIMIT]),
//                    pt,
//                    Integer.parseInt(properties[OCTO_ACTION_PERIOD]),
//                    Integer.parseInt(properties[OCTO_ANIMATION_PERIOD]),
//                    ImageStore.getImageList(imageStore, OCTO_KEY));
//            WorldModel.tryAddEntity(world, entity);
//        }
//
//        return properties.length == OCTO_NUM_PROPERTIES;
//    }
//
//    public static Point nextPositionOcto(Entity entity, WorldModel world,
//                                         Point destPos)
//    {
//        int horiz = Integer.signum(destPos.x - entity.position.x);
//        Point newPos = new Point(entity.position.x + horiz,
//                entity.position.y);
//
//        if (horiz == 0 || WorldModel.isOccupied(world, newPos))
//        {
//            int vert = Integer.signum(destPos.y - entity.position.y);
//            newPos = new Point(entity.position.x,
//                    entity.position.y + vert);
//
//            if (vert == 0 || WorldModel.isOccupied(world, newPos))
//            {
//                newPos = entity.position;
//            }
//        }
//
//        return newPos;
//    }
//}
