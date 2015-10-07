package cz.vrbik.ufo.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Třída reprezentující nastavení galaxie
 */

public class Config {

    private static final String CONFIG_FILE_PATH= "src/resources/config/config.properties";
    private static final String CONFIG_HEADER = "Konfiguracni soubor pro galaxii";

    private static final String PROP_GALAXY_WIDTH = "galaxy_width";
    private static final String PROP_GALAXY_HEIGHT = "galaxy_height";
    private static final String PROP_PLANET_COUNT = "planet_count";
    private static final String PROP_STATION_COUNT = "station_count";
    private static final String PROP_PLANET_SPACING = "planet_spacing";
    private static final String PROP_STATION_SPACING = "station_spacing";

    private static final int DEF_GALAXY_WIDTH = 800;
    private static final int DEF_GALAXY_HEIGHT = 800;
    private static final int DEF_PLANET_COUNT = 5000;
    private static final int DEF_STATION_COUNT = 5;
    private static final int DEF_PLANET_SPACING = 3;
    private static final int DEF_STATION_SPACING = 10;

    public final ObjectProperty<Number> galaxyWidth = new SimpleObjectProperty<>();
    public final ObjectProperty<Number> galaxyHeight = new SimpleObjectProperty<>();
    public final ObjectProperty<Number> planetCount = new SimpleObjectProperty<>();
    public final ObjectProperty<Number> stationCount = new SimpleObjectProperty<>();
    public final ObjectProperty<Number> planetSpacing = new SimpleObjectProperty<>();
    public final ObjectProperty<Number> stationSpacing = new SimpleObjectProperty<>();

    private Properties p;
    private File configFile;

    /**
     *  Konstruktor třídy Config
     */
    public Config() {
        p = new Properties();

        configFile = new File(CONFIG_FILE_PATH);
        try {
            if (configFile.createNewFile()) {
                p.setProperty(PROP_GALAXY_WIDTH, String.valueOf(DEF_GALAXY_WIDTH));
                p.setProperty(PROP_GALAXY_HEIGHT, String.valueOf(DEF_GALAXY_HEIGHT));
                p.setProperty(PROP_PLANET_COUNT, String.valueOf(DEF_PLANET_COUNT));
                p.setProperty(PROP_STATION_COUNT, String.valueOf(DEF_STATION_COUNT));
                p.setProperty(PROP_PLANET_SPACING, String.valueOf(DEF_PLANET_SPACING));
                p.setProperty(PROP_STATION_SPACING, String.valueOf(DEF_PLANET_SPACING));

                p.store(new FileOutputStream(configFile), CONFIG_HEADER);
            } else {
                p.load(new FileInputStream(configFile));
            }

            setValues();
            bindProperty();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Nastaví vlastnosti do pozorovatelných hodnot
     */
    private void setValues() {
        galaxyWidth.setValue(getGalaxyWidth());
        galaxyHeight.setValue(getGalaxyHeight());
        planetCount.setValue(getPlanetCount());
        stationCount.setValue(getStationCount());
        planetSpacing.setValue(getPlanetSpacing());
        stationSpacing.setValue(getStationSpacing());
    }

    /**
     * Nabinduje jednotlivé vlastnosti
     */
    private void bindProperty() {
        galaxyWidth.addListener((observable, oldValue, newValue) -> setGalaxyWidth(newValue.intValue()));
        galaxyHeight.addListener((observable, oldValue, newValue) -> setGalaxyHeight(newValue.intValue()));
        planetCount.addListener((observable, oldValue, newValue) -> setPlanetCount(newValue.intValue()));
        stationCount.addListener((observable, oldValue, newValue) -> setStationCount(newValue.intValue()));
        planetSpacing.addListener((observable, oldValue, newValue) -> setPlanetSpacing(newValue.intValue()));
        stationSpacing.addListener((observable, oldValue, newValue) -> setStationSpacing(newValue.intValue()));
    }

    /**
     * Metoda pro nastavení konfigurace do souboru
     * @param line vstupní řádek z .glx souboru
     * @throws IOException IO chyba
     */
    public void updateConfig(String line) {
        line = line.replaceAll(" ", "");
        String data[] = line.split(";");

        for (String s : data) {
            String[] data1 = s.split(":");
            p.setProperty(data1[0], data1[1]);
        }

        setValues();
    }

    /**
     * Uloží aktuální konfiguraci na disk
     */
    public void saveConfig() {
        try {
            p.store(new FileOutputStream(configFile), CONFIG_HEADER);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda vrací šířku galaxie
     * @return šířka galaxie
     */
    public int getGalaxyWidth() {
        String property = p.getProperty(PROP_GALAXY_WIDTH);
        if (property == null || property.isEmpty() || property.equals(""))
            return DEF_GALAXY_WIDTH;

        return Integer.valueOf(property);
    }

    /**
     * Metoda pro nastavení šířky galaxie
     * @param property šířka galaxie
     * @throws IOException IO chyba
     */
    public void setGalaxyWidth(int property) {
        p.setProperty(PROP_GALAXY_WIDTH, String.valueOf(property));
    }

    /**
     * Metoda vrací výšku galaxie
     * @return výška galaxie
     */
    public int getGalaxyHeight() {
        String property = p.getProperty(PROP_GALAXY_HEIGHT);
        if (property == null || property.isEmpty() || property.equals(""))
            return DEF_GALAXY_HEIGHT;

        return Integer.valueOf(property);
    }

    /**
     * Metoda pro nastavení výšky galaxie
     * @param property výška galaxie 
     * @throws IOException IO chyba
     */
    public void setGalaxyHeight(int property) {
        p.setProperty(PROP_GALAXY_HEIGHT, String.valueOf(property));
    }

    /**
     * Metoda vrací počet planet
     * @return počet planet
     */
    public int getPlanetCount() {
        String property = p.getProperty(PROP_PLANET_COUNT);
        if (property == null || property.isEmpty() || property.equals(""))
            return DEF_PLANET_COUNT;

        return Integer.valueOf(property);
    }

    /**
     * Metoda pro nastavení počtu planet
     * @param property počet planet
     * @throws IOException IO chyba
     */
    public void setPlanetCount(int property) {
        p.setProperty(PROP_PLANET_COUNT, String.valueOf(property));
    }

    /**
     * Metoda vrací počet stanic
     * @return počet stanic
     */
    public int getStationCount() {
        String property = p.getProperty(PROP_STATION_COUNT);
        if (property == null || property.isEmpty() || property.equals(""))
            return DEF_STATION_COUNT;

        return Integer.valueOf(property);
    }

    /**
     * Metoda pro nastavení počtu stanic
     * @param property počet stanic
     * @throws IOException IO chyba
     */
    public void setStationCount(int property) {
        p.setProperty(PROP_STATION_COUNT, String.valueOf(property));
    }

    /**
     * Metoda vrací minimální vzdálenost mezi planetami při generování
     * @return minimální vzdálenost mezi planetami při generování
     */
    public int getPlanetSpacing() {
        String property = p.getProperty(PROP_PLANET_SPACING);
        if (property == null || property.isEmpty() || property.equals(""))
            return DEF_PLANET_SPACING;

        return Integer.valueOf(property);
    }

    /**
     * Metoda pro nastavení minimální vzdálenosti planet při generování
     * @param property minimální vzdálenost planet při generování
     * @throws IOException IO chyba
     */
    public void setPlanetSpacing(int property) {
        p.setProperty(PROP_PLANET_SPACING, String.valueOf(property));
    }

    /**
     * Metoda vrací minimální vzdálenost mezi stanicemi při generování
     * @return minimální vzdálenost stanic při generování
     */
    public int getStationSpacing() {
        String property = p.getProperty(PROP_STATION_SPACING);
        if (property == null || property.isEmpty() || property.equals(""))
            return DEF_STATION_SPACING;

        return Integer.valueOf(property);
    }

    /**
     * Metoda pro nastavení minimální vzdálenosti mezi stanicemi při generování
     * @param property minimální vzdálenost mezi stanicemi při generování
     * @throws IOException IO chyba
     */
    public void setStationSpacing(int property) {
        p.setProperty(PROP_STATION_SPACING, String.valueOf(property));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<Object, Object> entry : p.entrySet()) {
            sb.append(entry.getKey())
                    .append(": ")
                    .append(entry.getValue())
                    .append("; ");
        }

        return sb.toString();

    }
}
