package cz.vrbik.ufo.model;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * Created by Vrbik on 24.09.2015.
 * Třída pro načtení jmen pro planety
 */
public class PlanetNames {
    
    private static final String RESOURCES = "resources/names.txt";

    private static PlanetNames instance;

    private final List<String> names;
    private int counter = 0;

    private PlanetNames(){
        names = new ArrayList<>();
        readNames();
    }

    /**
     * Vrátí jedinou instanci jmen planet
     * @return
     */
    public static PlanetNames getInstance(){
        if (instance == null)
            instance = new PlanetNames();

        return instance;
    }

    /**
     * Vrátí náhodně vygenerované jméno
     */
    public String getName() {
        if (counter == 0)
            shuffleNames();
        return names.get(--counter);
    }

    /**
     * Načte jména ze souboru
     */
    private void readNames() {
        InputStream inputStream = ClassLoader.getSystemResourceAsStream(RESOURCES);
        try(BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))){
            String line;
            while ((line = br.readLine()) != null){
                names.add(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void shuffleNames() {
        Collections.shuffle(names);
        counter = names.size();
    }
}
