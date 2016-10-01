package project.alexandre.homecontrol;

/**
 * Created by Alexandre on 2015-03-27.
 */
public class CoffeeMakerItem {
    String identity;
    String id;
    Boolean inUse;
    int minutes;
    int seconds;

    public CoffeeMakerItem(String identity, String id, Boolean inUse, int minutes, int seconds ){
        super();
        this.identity = identity;
        this.id = id;
        this.inUse = inUse;
        this.minutes = minutes;
        this.seconds = seconds;
    }


    public String time() {
        String seconds;
        if (this.seconds < 10){
            seconds = "0" + String.valueOf(this.seconds);
        }  else {
            seconds = String.valueOf(this.seconds);
        }
        return String.valueOf(this.minutes) + ":" + seconds;
    }

    public int totalTime(){
        return ((this.minutes*60)+this.seconds);
    }



}


