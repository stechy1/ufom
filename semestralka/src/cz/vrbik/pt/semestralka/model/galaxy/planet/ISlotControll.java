package cz.vrbik.pt.semestralka.model.galaxy.planet;

/**
 * Rozhraní definující metody pro práci se sloty pro spojení
 */
public interface ISlotControll {

    /**
     * Metoda zjišťuje, zda-li objekt má volný slot pro spojení
     *
     * @return True, pokud je slot volný, jinak false
     */
    boolean isFull();

    /**
     * Metoda vrací počet volných slotů
     *
     * @return Počet volných slotů
     */
    int getFreeSlots();

}
