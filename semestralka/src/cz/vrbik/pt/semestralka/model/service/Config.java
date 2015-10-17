package cz.vrbik.pt.semestralka.model.service;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

/**
 * Třída starající se o nastavení aplikace
 */
public class Config {

    private static final URL CONFIG_FILE = ClassLoader.getSystemResource("resources/config/galaxy.properties");
    //private static final String CONFIG_FILE_PATH= "src/resources/config/galaxy.properties";
    private static final String CONFIG_HEADER = "Konfiguracni soubor pro galaxii";

    private static final String PROP_GALAXY_WIDTH = "galaxy_width";
    private static final String PROP_GALAXY_HEIGHT = "galaxy_height";
    private static final String PROP_PLANET_COUNT = "planet_count";
    private static final String PROP_STATION_COUNT = "station_count";
    private static final String PROP_PLANET_SPACING = "planet_spacing";
    private static final String PROP_STATION_SPACING = "station_spacing";
    private static final String PROP_STATION_SHIP_COUNT = "station_ship_count";


    private static final int DEF_GALAXY_WIDTH = 800;
    private static final int DEF_GALAXY_HEIGHT = 800;
    private static final int DEF_PLANET_COUNT = 5000;
    private static final int DEF_STATION_COUNT = 5;
    private static final int DEF_PLANET_SPACING = 3;
    private static final int DEF_STATION_SPACING = 10;
    private static final int DEF_STATION_SHIP_COUNT = 1000;

    public final ObjectProperty<Number> galaxyWidth = new SimpleObjectProperty<>();
    public final ObjectProperty<Number> galaxyHeight = new SimpleObjectProperty<>();
    public final ObjectProperty<Number> planetCount = new SimpleObjectProperty<>();
    public final ObjectProperty<Number> stationCount = new SimpleObjectProperty<>();
    public final ObjectProperty<Number> planetSpacing = new SimpleObjectProperty<>();
    public final ObjectProperty<Number> stationSpacing = new SimpleObjectProperty<>();
    public final ObjectProperty<Number> shipCount = new SimpleObjectProperty<>();

    private Properties p;
    private File configFile;

    /**
     *  Konstruktor třídy Config
     */
    public Config() {
        p = new Properties();

        try {
            configFile = new File(CONFIG_FILE.toURI());
            if (configFile.createNewFile()) {
                p.setProperty(PROP_GALAXY_WIDTH, String.valueOf(DEF_GALAXY_WIDTH));
                p.setProperty(PROP_GALAXY_HEIGHT, String.valueOf(DEF_GALAXY_HEIGHT));
                p.setProperty(PROP_PLANET_COUNT, String.valueOf(DEF_PLANET_COUNT));
                p.setProperty(PROP_STATION_COUNT, String.valueOf(DEF_STATION_COUNT));
                p.setProperty(PROP_PLANET_SPACING, String.valueOf(DEF_PLANET_SPACING));
                p.setProperty(PROP_STATION_SPACING, String.valueOf(DEF_PLANET_SPACING));
                p.setProperty(PROP_STATION_SHIP_COUNT, String.valueOf(DEF_STATION_SHIP_COUNT));

                p.store(new FileOutputStream(configFile), CONFIG_HEADER);
            } else {
                p.load(new FileInputStream(configFile));
            }

            setValues();
            bindProperty();
        } catch (IOException | URISyntaxException e) {
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
        shipCount.setValue(getShipCount());
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
        shipCount.addListener((observable, oldValue, newValue) -> setShipCount(newValue.intValue()));
    }

    /**
     * Metoda pro nastavení konfigurace do souboru
     *
     * @param line vstupní řádek z .glx souboru
     */
    public void updateConfig(String line) {
        String[] data = line.split(";");
        p.setProperty(PROP_GALAXY_WIDTH, data[0]);
        p.setProperty(PROP_GALAXY_HEIGHT, data[1]);
        p.setProperty(PROP_PLANET_COUNT, data[2]);
        p.setProperty(PROP_STATION_COUNT, data[3]);
        p.setProperty(PROP_PLANET_SPACING, data[4]);
        p.setProperty(PROP_STATION_SPACING, data[5]);
        p.setProperty(PROP_STATION_SHIP_COUNT, data[6]);
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
     *
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
     *
     * @param property šířka galaxie
     */
    public void setGalaxyWidth(int property) {
        p.setProperty(PROP_GALAXY_WIDTH, String.valueOf(property));
    }

    /**
     * Metoda vrací výšku galaxie
     *
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
     *
     * @param property výška galaxie
     */
    public void setGalaxyHeight(int property) {
        p.setProperty(PROP_GALAXY_HEIGHT, String.valueOf(property));
    }

    /**
     * Metoda vrací počet planet
     *
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
     *
     * @param property počet planet
     */
    public void setPlanetCount(int property) {
        p.setProperty(PROP_PLANET_COUNT, String.valueOf(property));
    }

    /**
     * Metoda vrací počet stanic
     *
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
     *
     * @param property počet stanic
     */
    public void setStationCount(int property) {
        p.setProperty(PROP_STATION_COUNT, String.valueOf(property));
    }

    /**
     * Metoda vrací minimální vzdálenost mezi planetami při generování
     *
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
     *
     * @param property minimální vzdálenost planet při generování
     */
    public void setPlanetSpacing(int property) {
        p.setProperty(PROP_PLANET_SPACING, String.valueOf(property));
    }

    /**
     * Metoda vrací minimální vzdálenost mezi stanicemi při generování
     *
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
     *
     * @param property minimální vzdálenost mezi stanicemi při generování
     */
    public void setStationSpacing(int property) {
        p.setProperty(PROP_STATION_SPACING, String.valueOf(property));
    }

    /**
     * Metoda vrací počet lodí pro jednu stancii
     *
     * @return Počet lodí pro jednu stanici
     */
    public int getShipCount() {
        String property = p.getProperty(PROP_STATION_SHIP_COUNT);
        if (property == null || property.isEmpty() || property.equals(""))
            return DEF_STATION_SHIP_COUNT;

        return Integer.valueOf(property);
    }

    /**
     * Metoda pro nastavení počtu lodí na jednu stanici
     *
     * @param property Počet lodí
     */
    public void setShipCount(int property) {
        p.setProperty(PROP_STATION_SHIP_COUNT, String.valueOf(property));
    }

    @Override
    public String toString() {
        return String.format("%s;%d;%d;%d;%d;%d;%d", String.valueOf(getGalaxyWidth()), getGalaxyHeight(), getPlanetCount(), getStationCount(), getPlanetSpacing(), getStationCount(), getShipCount());
    }

}
