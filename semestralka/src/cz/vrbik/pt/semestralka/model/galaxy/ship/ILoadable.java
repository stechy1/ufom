package cz.vrbik.pt.semestralka.model.galaxy.ship;

/**
 * Rozhraní pro nakládání a vykládání nákladu
 */
public interface ILoadable {

    /**
     * Naloží dané množství
     *
     * @param mount množství
     */
    default void loadCargo(int mount){}

    /**
     * Vyloží veškerý náklad
     */
    default boolean unLoadCargo(int mount){return false;}

    /**
     * Metoda získá aktuální naložené množství
     *
     * @return Aktuální naložené mnozství
     */
    default int getCargo(){return 0;}

    /**
     * Vrátí množství, které loď může naložit
     *
     * @return naložitelné množství
     */
    default int getCapacity(){return 0;}

    /**
     * Metoda zjišťuje, zda-li je loď plně naložena
     *
     * @return True, pokud je loď plně naložena, jinak false
     */
    default boolean isShipFullyLoaded(){return false;}
}
