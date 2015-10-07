package cz.vrbik.pt.semestralka.model.galaxy;

/**
 * Rozhraní definující metody pro dvourozměrný objekt
 */
public interface IGraphicalObject {

    /**
     * Vrátí vodorovnou pozici objektu
     */
    double getX();

    /**
     * Nastaví vodorovnou pozici objektu
     *
     * @param x vodorovná pozice objektu
     */
    void setX(double x);

    /**
     * Vrátí svislou pozici objektu
     */
    double getY();

    /**
     * Nastaví svislou pozici objektu
     *
     * @param y svislá pozice objektu
     */
    void setY(double y);

    /**
     * Metoda vypočítá střed objektu ve vodorovné ose
     *
     * @return Střed objektu ve vodorovné ose
     */
    default double getCenterX() {
        return getX() + getWidth() / 2;
    }

    /**
     * Metoda vypočítá střed objektu ve svislé ose
     *
     * @return Střed objektu ve svislé ose
     */
    default double getCenterY() {
        return getY() + getHeight() / 2;
    }

    /**
     * Vrátí šířku objektu
     */
    double getWidth();

    /**
     * Nastaví šířku objektu
     *
     * @param width šířku objektu
     */
    void setWidth(double width);

    /**
     * Vrátí výšku objektu
     */
    double getHeight();

    /**
     * Nastaví výšku objektu
     *
     * @param height Výška
     */
    void setHeight(double height);

}
