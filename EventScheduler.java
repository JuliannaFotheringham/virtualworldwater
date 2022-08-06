import java.util.*;

/*
EventScheduler: ideally our way of controlling what happens in our virtual world
 */

final class EventScheduler
{

   public static final int ATLANTIS_ANIMATION_REPEAT_COUNT = 7;
   public static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;
   public PriorityQueue<Event> eventQueue;
   public Map<Entity, List<Event>> pendingEvents;
   public double timeScale;

   public EventScheduler(double timeScale)
   {
      this.eventQueue = new PriorityQueue<>(new EventComparator());
      this.pendingEvents = new HashMap<>();
      this.timeScale = timeScale;
   }

   public void removePendingEvent(Event event)
   {
      List<Event> pending = this.pendingEvents.get(event.entity);

      if (pending != null)
      {
         pending.remove(event);
      }
   }

   public  void updateOnTime(EventScheduler scheduler, long time)
   {
      while (!scheduler.eventQueue.isEmpty() &&
              scheduler.eventQueue.peek().time < time)
      {
         Event next = scheduler.eventQueue.poll();

         removePendingEvent(next);

         next.action.executeAction(scheduler);
      }
   }


   public  void unscheduleAllEvents(Entity entity)
   {
      List<Event> pending = pendingEvents.remove(entity);

      if (pending != null)
      {
         for (Event event : pending)
         {
            eventQueue.remove(event);
         }
      }
   }


//
//   public static void scheduleEvent(EventScheduler scheduler,
//                                    Entity entity, Action action, long afterPeriod)
//   {
//      long time = System.currentTimeMillis() +
//              (long)(afterPeriod * scheduler.timeScale);
//      Event event = new Event(action, time, entity);
//
//      scheduler.eventQueue.add(event);
//
//      // update list of pending events for the given entity
//      List<Event> pending = scheduler.pendingEvents.getOrDefault(entity,
//              new LinkedList<>());
//      pending.add(event);
//      scheduler.pendingEvents.put(entity, pending);
//   }


//   public static void scheduleActions(Entity entity, EventScheduler scheduler,
//                                      WorldModel world, ImageStore imageStore)
//   {
//      switch (entity.kind)
//      {
//         case OCTO_FULL:
//            scheduleEvent(scheduler, entity,
//                    Action.createActivityAction(entity, world, imageStore),
//                    entity.actionPeriod);
//            scheduleEvent(scheduler, entity, Action.createAnimationAction(entity, 0),
//                    Entity.getAnimationPeriod(entity));
//            break;
//
//         case OCTO_NOT_FULL:
//            scheduleEvent(scheduler, entity,
//                    Action.createActivityAction(entity, world, imageStore),
//                    entity.actionPeriod);
//            scheduleEvent(scheduler, entity,
//                    Action.createAnimationAction(entity, 0), Entity.getAnimationPeriod(entity));
//            break;
//
//         case FISH:
//            scheduleEvent(scheduler, entity,
//                    Action.createActivityAction(entity, world, imageStore),
//                    entity.actionPeriod);
//            break;
//
//         case CRAB:
//            scheduleEvent(scheduler, entity,
//                    Action.createActivityAction(entity, world, imageStore),
//                    entity.actionPeriod);
//            scheduleEvent(scheduler, entity,
//                    Action.createAnimationAction(entity, 0), Entity.getAnimationPeriod(entity));
//            break;
//
//         case QUAKE:
//            scheduleEvent(scheduler, entity,
//                    Action.createActivityAction(entity, world, imageStore),
//                    entity.actionPeriod);
//            scheduleEvent(scheduler, entity,
//                    Action.createAnimationAction(entity, QUAKE_ANIMATION_REPEAT_COUNT),
//                    Entity.getAnimationPeriod(entity));
//            break;
//
//         case SGRASS:
//            scheduleEvent(scheduler, entity,
//                    Action.createActivityAction(entity, world, imageStore),
//                    entity.actionPeriod);
//            break;
//         case ATLANTIS:
//            scheduleEvent(scheduler, entity,
//                    Action.createAnimationAction(entity, ATLANTIS_ANIMATION_REPEAT_COUNT),
//                    Entity.getAnimationPeriod(entity));
//            break;
//
//         default:
//      }
//   }

}
