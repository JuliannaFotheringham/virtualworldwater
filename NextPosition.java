import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface NextPosition {

    Point getPosition();

    static Point nextPosition(NextPosition obj, WorldModel world, Point destPos, PathingStrategy pathingStrategy,
                              Function<Point, Stream<Point>> potentialNeighbors)
    {

        List<Point> path = pathingStrategy.computePath(
                obj.getPosition(),
                destPos,
                p -> world.canPassThrough(p),
                (placement, goal) -> world.withinReach(placement, goal, potentialNeighbors),
                potentialNeighbors );

        if (path.size() >= 1)
        {
            return path.get(0);
        }

        return obj.getPosition();
    }


    static void possiblyMove(NextPosition obj, Entity target, WorldModel world, EventScheduler scheduler,
                             PathingStrategy pathingStrategy, Function<Point, Stream<Point>> potentialNeighbors)
    {

        Point nextPos = nextPosition(obj, world, target.getPosition(), pathingStrategy, potentialNeighbors);

        if (!((Entity)obj).getPosition().equals(nextPos))
        {
            Optional<Entity> occupant = world.getOccupant(nextPos);
            if (occupant.isPresent())
            {
                scheduler.unscheduleAllEvents(occupant.get());
            }

            world.moveEntity((Entity)obj, nextPos);
        }
    }
}

