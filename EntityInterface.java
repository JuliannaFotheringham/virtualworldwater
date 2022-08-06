import processing.core.PImage;

import java.util.List;

public interface EntityInterface {

    public Entity createEntity();


    public boolean parseEntity(String [] properties, WorldModel world,
                                        ImageStore imageStore);

}
