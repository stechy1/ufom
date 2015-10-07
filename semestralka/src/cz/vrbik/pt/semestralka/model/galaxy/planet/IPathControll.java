package cz.vrbik.pt.semestralka.model.galaxy.planet;

import cz.vrbik.pt.semestralka.model.galaxy.Path;

/**
 * Rozhraní definující metody pro práci s cestou
 */
public interface IPathControll {

    /**
     * Přidá cestu do seznamu planety
     *
     * @param path Cesta vedoucí z planety
     */
    void addPath(Path path);

}
