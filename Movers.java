public interface Movers {

    public  void scheduleActions(EventScheduler scheduler,
                                 WorldModel world, ImageStore imageStore); //these parameters are the same for all


    public  void executeAction(EventScheduler scheduler); //all take same paramete-scheduler
}
