import java.util.ArrayList;

/**
 * Created by Vrbik on 07.10.2015.
 */
public class Ship {

    private boolean loading = true;
    private boolean unloading = false;
    private int power = 0;
    private int urazenaVzdalenost = 0;

    private int last = 0;


    private stav state = stav.LOADING;

    private int update_count = 1;

    ArrayList<Path> cesta;
    int celkovaDelkaCesty = 0;

    public Ship(int medicine, ArrayList<Path> cesta){
        this.cesta = cesta;

        for(Path a : cesta){
            celkovaDelkaCesty += a.getWeight();
        }

        System.out.println("delka cesty :" + celkovaDelkaCesty);
    }

    public void update(int time){

        System.out.println("update: " + update_count++);

        switch (state){
            case LOADING:{
                    System.out.println("NAKLADAM NAKLAD");
                    state = stav.TRAVELING;
                    break;
            }
            case TRAVELING:{
                    System.out.println("TRAVELING");
                    power += time;
                    calculateTrack();
                    break;
            }
            case UNLOADING:{
                    System.out.println("UNLOADING");
                    state = stav.GOING_HOME;
                    break;
            }
            case GOING_HOME:{
                    System.out.println("GOING HOME");
                    power += time;
                    if(power > celkovaDelkaCesty){
                        state = stav.DOCKED;
                    }
                    break;
            }
            case DOCKED:{
                    System.out.println("JSEM DOMA MAMIIIIIII");
                    break;
            }

            case PIRATED:{
                System.out.println("PIARETD:::::");

                if(power > urazenaVzdalenost)
                    state = stav.DOCKED;

                power += time;


                break;
            }
        }
    }

    private void calculateTrack(){

        for (int i = last; i < cesta.size() ;i++) {

            Path malaCesta = cesta.get(i);

                if (power > malaCesta.getWeight()) {

                    if(malaCesta.generatePirata()){

                        System.out.println("pirati na ceste s delkou " + malaCesta.getWeight());
                        state = stav.PIRATED;
                        update(0);
                        break;
                    }

                    if(i == (cesta.size() - 1)){
                            power = power - malaCesta.getWeight();
                            state = stav.UNLOADING;
                            break;
                    }


                    power = power - malaCesta.getWeight();
                    urazenaVzdalenost += malaCesta.getWeight();
                }
                else {
                    last = i;
                    break;
                }


        }



    }


}
