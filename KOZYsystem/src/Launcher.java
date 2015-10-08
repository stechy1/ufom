import java.util.ArrayList;

/**
 * Created by Vrbik on 07.10.2015.
 */
public class Launcher {

    public static void main(String[] args) {
        Path a = new Path(10);
        Path b = new Path(3);
        Path c = new Path(4);
        Path d = new Path(19);
        Path e = new Path(7);


        ArrayList<Path> cesta = new ArrayList<>();


        cesta.add(a);
        cesta.add(b);
        cesta.add(c);
        cesta.add(d);
        cesta.add(e);


        Ship hovno = new Ship(100, cesta);

        for (int i = 0; i < 10; i++) {
            hovno.update(25);
        }




    }
}
