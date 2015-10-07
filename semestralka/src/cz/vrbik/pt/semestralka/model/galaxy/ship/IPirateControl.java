package cz.vrbik.pt.semestralka.model.galaxy.ship;

/**
 * Rozhraní pro kontrolu pirátů
 */
public interface IPirateControl {

    /**
     * Metoda nastaví příznak, zda-li byla kontrolovaná na piráty
     *
     * @param checked True, pokud byla loď zkontrolovaná na piráty, jinak false
     */
    void setChecked(boolean checked);

    /**
     * Metoda zjistí, zda-li se loď kontrolovala na piráty
     *
     * @return True, pokud byla loď zkontrolovaná na piráty, jinak false
     */
    boolean isChecked();

}
