package project.alexandre.homecontrol;

/**
 * Created by Alexandre on 2015-03-29.
 */
public class MicroWaveItem {
    String id;
    String identity;
    boolean inUse;

    public MicroWaveItem(String identity, String id, boolean inuse){
        this.id = id;
        this.identity = identity;
        this.inUse = inuse;
    }
}
