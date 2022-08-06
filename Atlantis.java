import processing.core.PImage;

import java.util.List;
import java.util.Optional;


public class Atlantis extends Entity implements Movers  {  //now extends entity


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
public static final String ATLANTIS_KEY = "atlantis";
public static final int ATLANTIS_NUM_PROPERTIES = 4;
public static final int ATLANTIS_ID = 1;
public static final int ATLANTIS_COL = 2;
public static final int ATLANTIS_ROW = 3;

    //added these parameters when implementing execute action
    private Entity entity;
    private WorldModel world;
    private ImageStore imageStore;


   public Atlantis(EntityKind kind, String id, Point position, List<PImage> images, int imageIndex, int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod) {
     super(kind, id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod); //added super
      this.id = id;
      this.position = position;
      this.images = images;
      this.imageIndex = imageIndex;
      this.resourceLimit = resourceLimit;
      this.resourceCount = resourceCount;
      this.actionPeriod = actionPeriod;
      this.animationPeriod = animationPeriod;
   }

    public static Entity createAtlantis(String id, Point position,  //moved to atlantis class and changed return tyoe to new atlantis
                                        List<PImage> images)
    {
        return new Atlantis (EntityKind.ATLANTIS, id, position, images,
                0, 0, 0, 0, 0);
    }


    @Override
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {   //implements atlantis schedule actions from switch case
        scheduleEvent(scheduler,
                Animation.createAnimationAction(this, ATLANTIS_ANIMATION_REPEAT_COUNT),  //changed class to animation
                Entity.getAnimationPeriod(this));

    }

    @Override //implemented execute action from movers interface that came from execute (activity) action's switch case)
    public void executeAction(EventScheduler scheduler) {

        executeAtlantisActivity(this.world, this.imageStore,  //removed this.entity.  from in fromt of executeAtlatnisaCtivity since they are now both in atlantis class
                scheduler);

    }


    //moved execute entity activity from entity, not het implemented since it i still in entity

    public void executeAtlantisActivity(WorldModel world,
                                        ImageStore imageStore, EventScheduler scheduler) {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }




//
//    public void executeAtlantisActivity(WorldModel world,
//                                        ImageStore imageStore, EventScheduler scheduler)
//    {
//        scheduler.unscheduleAllEvents(scheduler, this);
//        world.removeEntity(this);
//    }


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
//

}

