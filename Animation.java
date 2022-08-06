public class Animation extends Action{

    public ActionKind kind;
    public Entity entity;
    public WorldModel world;
    public ImageStore imageStore;
    public int repeatCount;

    public Animation(ActionKind kind, Entity entity, WorldModel world,
                  ImageStore imageStore, int repeatCount)
    {
        super(kind, entity, world, imageStore, repeatCount); //added actions superclass constructor
        this.kind = kind;
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = repeatCount;
    }


    public static Action createAnimationAction(Entity entity, int repeatCount)
    {
        return new Animation(ActionKind.ANIMATION, entity, null, null, repeatCount);  //changed to return animation caseted as an action
    }



    public  void executeAction(EventScheduler scheduler){   //added execute aciton using body from executeanimationaction.

        {
            Entity.nextImage(this.entity);

            if (this.repeatCount != 1)
            {
                entity.scheduleEvent(scheduler,
                        Animation.createAnimationAction(this.entity,  //changed class to animation (it had nothing before since were in action )
                                Math.max(this.repeatCount - 1, 0)),
                        Entity.getAnimationPeriod(this.entity));
            }
        }


    }



//    public static void executeAnimationAction(Action action,
//                                              EventScheduler scheduler)
//    {
//        Entity.nextImage(action.entity);
//
//        if (action.repeatCount != 1)
//        {
//            EventScheduler.scheduleEvent(scheduler, action.entity,
//                    Animation.createAnimationAction(action.entity,
//                            Math.max(action.repeatCount - 1, 0)),
//                    Entity.getAnimationPeriod(action.entity));
//        }
//    }
//
//    public void executeAction(Action action, EventScheduler scheduler)
//    { Animation.executeAnimationAction(action, scheduler);}
}
