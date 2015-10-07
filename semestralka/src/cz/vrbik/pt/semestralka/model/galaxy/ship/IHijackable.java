package cz.vrbik.pt.semestralka.model.galaxy.ship;

/**
 * Rozhraní definující metody pro přepadení lodi
 */
public interface IHijackable {

    /**
     * Nastaví příznak, pokud byla loď přepadena
     *
     * @param hijacked True, pokud byla loď přepadena, jinak false
     */
    void setHijacked(boolean hijacked);

    /**
     * Metoda zjišťuje, zda-li byla loď přepadena
     *
     * @return True, pokud byla loď přepadena, jinak false
     */
    boolean isHijacked();
}
