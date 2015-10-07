package cz.vrbik.shippingsystem;

/**
 * Rozhraní definující aktualizační metodu
 */
public interface IUpdatable {

    /**
     * Aktualizační metoda objektu
     * @param timestap Doba od počátku. Počátek = prvotní spuštění výpočtů galaxie
     */
    void update(int timestap);

}
