import java.util.Random;

/**
 * Created by Vrbik on 07.10.2015.
 */
public class Path {

    int weight;

    public Path(int weight) {
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }

    public boolean generatePirata(){
        Random a = new Random();

        return a.nextBoolean();
    }




}
