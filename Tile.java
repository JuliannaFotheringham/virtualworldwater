public class Tile {
    public static Point position;
    private double f;
    private double g;
    private double h;
    public int nieghboring;
    public Tile nieghborParent;

    private boolean exploredNeighbors;

    public Tile(Point position, double h, Tile parent) {
        this.position = position;
        this.h = h;
        this.nieghborParent = parent;
        this.nieghboring = 0;

        this.exploredNeighbors = false;

        if (this.nieghborParent != null) {
            this.g = this.nieghborParent.getG() + 1;
            this.nieghborParent.nieghborCount();
        } else
            this.g = 0;

        this.f = this.g + this.h;
    }



    public boolean equals(Object other) {
        return other instanceof Tile &&
                ((Tile) other).position.equals(this.position);


    }


    public static Point getPos() {
        return position;
    }

    public void setExploredNeighbors(boolean exploredNeighbors) {
        this.exploredNeighbors = exploredNeighbors;
    }

    public void nieghborCount() {
        this.nieghboring += 1;
    }


    public boolean exploredNeighbors() {
        return this.exploredNeighbors;
    }

    public Tile getNieghborParent() {
        return this.nieghborParent;
    }

    public double getF() {
        this.f = this.g + this.h;
        return this.f;
    }

    public double getG() {
        return this.g;
    }





}
