package cz.vrbik.pt.semestralka.model.galaxy.ship;

import cz.vrbik.pt.semestralka.Headquarters;
import cz.vrbik.pt.semestralka.model.galaxy.planet.BasePlanet;
import cz.vrbik.pt.semestralka.model.service.ResourceRequest;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Základní třída pro všechny lodě
 */
public abstract class BaseShip implements IShip {

    private static final Logger log = Logger.getLogger(BaseShip.class.getName());
    public static final int DEFAULT_WIDTH = 5;
    public static final int DEFAULT_HEIGHT = 5;

    public final LinkedList<BasePlanet> trip = new LinkedList<>();

    protected static int ID_COUNTER = 0;

    private Iterator<BasePlanet> tripIterator;
    private BasePlanet homePlanet;
    protected BasePlanet actualPlanet;
    protected BasePlanet nextPlanet;

    protected double x, y, width = DEFAULT_WIDTH, height = DEFAULT_HEIGHT;
    protected boolean endConnection = false;
    protected int totalProgress = 0;
    protected int connectionProgress = 0;
    protected int speed = 25;
    protected boolean hijacked;
    protected int id;
    protected boolean checked;
    protected int loadingProgress = 0;
    protected ResourceRequest request;

    /**
     * Konstruktor třídy {@link BaseShip}
     * <p>
     * Vytvoří novou vesmírnou loď
     *
     * @param homePlanet Reference na domovskou planetu lodi
     */
    public BaseShip(BasePlanet homePlanet) {
        this.homePlanet = homePlanet;
        id = ID_COUNTER++;
        log.debug(String.format("Vytvářím novou loď na planetě: %s. ID lodi: %d", homePlanet.getName(), id));
    }

    /**
     * Odstartuje loď na cestu
     */
    @Override
    public void startTrip() {
        endConnection = false;
        try {
            nextPlanet = tripIterator.next();
            actualPlanet.send(this);
        } catch (NoSuchElementException ex) {
            log.error(String.format("Nepodařilo se odstartovat loď číslo: %d. Aktuální planeta: %s", id, actualPlanet.getName()));
            //ex.printStackTrace();
        }
    }

    /**
     * Pošle loď na další cestu
     */
    @Override
    public void continueTrip() {
        if (nextPlanet != null)
            actualPlanet = nextPlanet;
        else
            actualPlanet = tripIterator.next();
        startTrip();
    }

    /**
     * Pošle loď z aktuální pozice zpět domů
     */
    @Override
    public void turnBackToHome() {
        while (tripIterator.hasNext()) {
            tripIterator.next();
            tripIterator.remove();
        }

        unLoadCargo(getCargo());
        schedule();
        startTrip();
    }

    /**
     * Metoda zjišťujě, zda-li je loď na konci své cesty
     *
     * @return Vrátí true, pokud už dál loď nepoletí, jinak false
     */
    @Override
    public boolean isEndOfTrip() {
        return !tripIterator.hasNext();
    }

    /**
     * Metoda zjišťuje, zda-li má loď vystoupit z hyperprostoru
     *
     * @return True, pokud loď dosáhla dočasného cíle, jinak false
     */
    @Override
    public boolean isEndOfConnection() {
        return endConnection;
    }

    /**
     * Inkrementuje celkový postup lodě
     */
    @Override
    public void incrementTotalProgress() {
        totalProgress++;
    }

    /**
     * Inkrementuje postup lodi na aktuálním meziplanetárním spojení
     */
    @Override
    public void decrementConnectionProgress() {
        connectionProgress--;
        //TODO vytvořit nastavení pro počítání celkového pstupu
        if (connectionProgress <= 0) {
            endConnection = true;
            incrementTotalProgress();
            nextPlanet.dock(this);
        }
    }

    /**
     * Nastaví celkovou dobu na jedné cestě
     *
     * @param value Doba na cestě
     */
    @Override
    public void setConnectionProgress(int value) {
        /*if (value > 4)
            value /= 2;*/
        connectionProgress = value;
    }

    /**
     * Zjistí, kolik zbývá do konce cesty
     *
     * @return Číslo udávající zbývající dobu na cestě
     */
    @Override
    public int getConnectionProgress() {
        return connectionProgress;
    }

    /**
     * Naplánuje cestu zpět do výchozího bodu
     */
    @Override
    public void schedule() {
        totalProgress = 0;
        Collections.reverse(trip);
        actualPlanet = nextPlanet;
        tripIterator = trip.iterator();
        tripIterator.next();
    }

    /**
     * Naplánuje novou cestu
     *
     * @param road List všech planet, přes které vede cesta
     */
    @Override
    public void schedule(List<BasePlanet> road) {
        actualPlanet = homePlanet;
        nextPlanet = null;
        totalProgress = 0;
        hijacked = false;
        checked = false;
        trip.clear();
        trip.addAll(road);
        tripIterator = trip.iterator();
        tripIterator.next();
    }

    /**
     * Nastaví příznak, pokud byla loď přepadena
     *
     * @param hijacked True, pokud byla loď přepadena, jinak false
     */
    @Override
    public void setHijacked(boolean hijacked) {
        if (hijacked) {
            Headquarters.getInstance().makeRequest(request);
            Headquarters.getInstance().nextHijackedShip();

            log.debug("loď byla přepadena piráty, posílá nový dotaz aby byla nahrazena");
        }
        this.hijacked = hijacked;
    }

    /**
     * Metoda zjišťuje, zda-li byla loď přepadena
     *
     * @return True, pokud byla loď přepadena, jinak false
     */
    @Override
    public boolean isHijacked() {
        return hijacked;
    }

    /**
     * Metoda nastaví příznak, zda-li byla kontrolovaná na piráty
     *
     * @param checked True, pokud byla loď zkontrolovaná na piráty, jinak false
     */
    @Override
    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    /**
     * Metoda zjistí, zda-li se loď kontrolovala na piráty
     *
     * @return True, pokud byla loď zkontrolovaná na piráty, jinak false
     */
    @Override
    public boolean isChecked() {
        return checked;
    }

    /**
     * Vrátí referenci na další cíl
     */
    @Override
    public BasePlanet getNextDestination() {
        return nextPlanet;
    }

    /**
     * Vrátí referenci na poslední cílovou planetu
     */
    @Override
    public BasePlanet getTotalDestination() {
        return trip.getLast();
    }

    /**
     * Vrátí list s naplánovanou cestou
     */
    @Override
    public List<BasePlanet> getTrip() {
        return trip;
    }

    /**
     * Returns a string representation of the object. In general, the
     * {@code toString} method returns a string that
     * "textually represents" this object. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     * It is recommended that all subclasses override this method.
     * <p>
     * The {@code toString} method for class {@code Object}
     * returns a string consisting of the name of the class of which the
     * object is an instance, the at-sign character `{@code @}', and
     * the unsigned hexadecimal representation of the hash code of the
     * object. In other words, this method returns a string equal to the
     * value of:
     * <blockquote>
     * <pre>
     * getClass().getName() + '@' + Integer.toHexString(hashCode())
     * </pre></blockquote>
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return String.format("Ship {%d}", id);
    }

    /**
     * Metoda zjistí, zda-li je loď připravena k odletu
     *
     * @return True, pokud je loď připravena k odletu, jinak false
     */
    @Override
    public boolean isReady() {
        if (loadingProgress == 25) {
            loadingProgress = 0;
            return true;
        }

        loadingProgress++;
        return false;
    }

    @Override
    public void setRequest(ResourceRequest request) {
        this.request = request;
    }

    @Override
    public ResourceRequest getRequest() {
        return request;
    }

    @Override
    public void render(GraphicsContext g) {
        if (isHijacked()) {
            g.setFill(Color.RED);
        } else if (getCargo() == 0) {
            g.setFill(Color.BLUE);
        } else {
            g.setFill(Color.LIGHTGRAY);
        }

        g.fillRect(x - width / 2, y - height / 2, width, height);
    }

    /**
     * Vrátí vodorovnou pozici objektu
     */
    @Override
    public double getX() {
        return x;
    }

    /**
     * Nastaví vodorovnou pozici objektu
     *
     * @param x vodorovná pozice objektu
     */
    @Override
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Vrátí svislou pozici objektu
     */
    @Override
    public double getY() {
        return y;
    }

    /**
     * Nastaví svislou pozici objektu
     *
     * @param y svislá pozice objektu
     */
    @Override
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Vrátí šířku objektu
     */
    @Override
    public double getWidth() {
        return width;
    }

    /**
     * Nastaví šířku objektu
     *
     * @param width šířku objektu
     */
    @Override
    public void setWidth(double width) {
        this.width = width;
    }

    /**
     * Vrátí výšku objektu
     */
    @Override
    public double getHeight() {
        return height;
    }

    /**
     * Nastaví výšku objektu
     *
     * @param height Výška
     */
    @Override
    public void setHeight(double height) {
        this.height = height;
    }
}
