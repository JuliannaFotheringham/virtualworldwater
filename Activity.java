public  class Activity extends Action { //extends action now


    private ActionKind kind;
    private Entity entity;
    private WorldModel world;
    private ImageStore imageStore;
    private int repeatCount;

    public Activity(ActionKind kind, Entity entity, WorldModel world,
                  ImageStore imageStore, int repeatCount)
    {
        super(kind, entity, world, imageStore, repeatCount); //added actions superclass constructor
        this.kind = kind;
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = repeatCount;
    }

    public static Action createActivityAction(Entity entity, WorldModel world,
                                              ImageStore imageStore)
    {
        return new Activity(ActionKind.ACTIVITY, entity, world, imageStore, 0); // changed createActivity action to create an activity upcasted as an action
    }


public  void executeAction(EventScheduler scheduler)
    { //changed executeactivity action to be execute action ineach individual classs that extends actiton

        // Now I am going to break up swithc case and move execute action to movers interface, they have been moved but not mplemented yet, implemntation will start when this i sdeleted
        switch (this.entity.kind)
        {
            case OCTO_FULL:
                this.entity.executeOctoFullActivity(this.world,
                        this.imageStore, scheduler);
                break;

            case OCTO_NOT_FULL:
                this.entity.executeOctoNotFullActivity(this.world,
                        this.imageStore, scheduler);
                break;

            case FISH:
                this.entity.executeFishActivity(this.world, this.imageStore,
                        scheduler);
                break;

            case CRAB:
                this.entity.executeCrabActivity(this.world,
                        this.imageStore, scheduler);
                break;

            case QUAKE:
                this.entity.executeQuakeActivity(this.world, this.imageStore,
                        scheduler);
                break;

            case SGRASS:
                this.entity.executeSgrassActivity(this.world, this.imageStore,
                        scheduler);
                break;

            case ATLANTIS:
                this.entity.executeAtlantisActivity(this.world, this.imageStore,
                        scheduler);
                break;


            default:
                throw new UnsupportedOperationException(
                        String.format("executeActivityAction not supported for %s",
                                this.entity.kind));
        }

    }














}
