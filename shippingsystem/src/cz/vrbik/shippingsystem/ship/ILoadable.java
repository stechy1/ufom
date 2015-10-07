package cz.vrbik.shippingsystem.ship;

/**
 * Rozhraní pro nakládání a vykládání nákladu
 */
public interface ILoadable {

    /**
     * Naloží dané množství
     * @param mount množství
     */
    void loadCargo(int mount);

    /**
     * Vyloží veškerý náklad
     */
    int unLoadCargo();

    /**
     * Vrátí množství, které loď může naložit
     * @return naložitelné množství
     */
    int getCapacity();

}
