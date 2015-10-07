package cz.vrbik.pt.semestralka.model;

import java.io.File;

/**
 * Rozhraní definující metody pro uložení a znovusestavení objektu
 */
public interface IRestorable {

    /**
     * Metoda uloží objekt do souboru
     *
     * @param file Reference na ukládaný soubor
     * @return True, pokud se uložení zdaří, jinak false
     */
    boolean store(File file);

    /**
     * Metoda sestaví objekt ze souboru
     *
     * @param file Reference na načítaný soubor
     * @return True, pokud se načtení zdaří, jinak false
     */
    boolean restore(File file);
}
