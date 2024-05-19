package main;

import entity.Entity;

import java.util.Comparator;

public class EntityComparator implements Comparator<Entity> {

    @Override
    public int compare(Entity entity1, Entity entity2) {
        return entity1.worldY - entity2.worldY;
    }
}
