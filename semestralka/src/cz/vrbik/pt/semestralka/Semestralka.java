package cz.vrbik.pt.semestralka;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.net.URL;

/**
 * Vstupní třída semestrální práce na PT
 */
public class Semestralka {

    private static final Logger log = Logger.getLogger(Semestralka.class.getName());

    public static final URL PATH_TO_LOG4J_PROPERTIES = ClassLoader.getSystemResource("resources/config/log4j.properties");

    public static void main(String[] args) throws Exception {
        PropertyConfigurator.configure(PATH_TO_LOG4J_PROPERTIES);

        if (args.length == 0) {
            log.info("Spouštím grafického klienta galaxie...");
            FXApp.main(args);
        } else {
            /*
            Nogui nogui = new Nogui();
            log.info("Spoustim konzoloveho klienta...");
            nogui.work();*/
        }
    }
}
