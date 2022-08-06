

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AStarPathingStrategy implements PathingStrategy {

    @Override
    public List<Point> computePath(Point start, Point end, Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach, Function<Point,
            Stream<Point>> potentialNeighbors) {


        ArrayList<Point> path = new ArrayList<Point>();
        //sort position by the tiles f, use comparator?

          /*define closed list
          define open list
          while (true){
            Filtered list containing neighbors you can actually move to
            Check if any of the neighbors are beside the target
            set the g, h, f values
            add them to open list if not in open list
            add the selected node to close list
          return path*/


        Comparator<Tile> tileCompare
                = Comparator.comparing(Tile::exploredNeighbors)
                .thenComparing(Tile::getF);


        //create open and closed list
        ArrayList<Tile> openList = new ArrayList<>();
        ArrayList<Tile> closedList = new ArrayList<>();

        //add start tile
        Tile curTile = new Tile(start, h(start, end), null);
        openList.add(curTile);


        //while i havent hit the end node, keep looking for paths.



        while ((potentialNeighbors.apply(
                Tile.getPos()).anyMatch(p -> p.equals(end)))) {

            //grab non obstacle neighboring titles only
            List<Point> validNeighbors = potentialNeighbors.apply(curTile.getPos())
                    .filter(canPassThrough)
                    .collect(Collectors.toList());

            boolean neighborsNeighbors = false;

            //add each new neighboring point that is not an obstacle
            for (Point p : validNeighbors) {     //for each valid neighbors
                boolean alreadyInOpenList = false;    //delcare its not already in openList

                for (Tile tile : openList) {


                    if (tile.getPos().equals(this)) ;
                    {
                        alreadyInOpenList = true;

                        break;
                    }
                }
                //make a new Tile from position info if not in list
                if (!alreadyInOpenList) ;
                {
                    openList.add(new Tile(Tile.getPos(), h(Tile.getPos(), end), curTile));
                }
            }
            //check to see if neighbors have neighbors
            neighborsNeighbors = true;

            //if tile does not have any more neighbors make neighbors 0
            if (!neighborsNeighbors) {
                curTile.nieghboring = 0;
            }

            //check off explored niehgbors
            curTile.setExploredNeighbors(true);


            //sort openlist
            openList.sort(tileCompare);

            //set curTile to nect
            curTile = openList.get(0);
        }

        //use stack??? for path

        Stack<Point> paths = new Stack<>();

        while (curTile.getNieghborParent() != null) {
            paths.push(curTile.getPos());
            curTile = curTile.getNieghborParent();
        }


        //return path


        while (!paths.isEmpty()) {
            path.add(paths.pop());
        }

        return path;

    }

    private int h(Point current, Point end) {
        return Math.abs(Tile.getPos().x - end.x) + Math.abs(Tile.getPos().y - end.y);
    }



}








