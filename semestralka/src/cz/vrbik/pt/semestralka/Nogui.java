package cz.vrbik.pt.semestralka;


import cz.vrbik.pt.semestralka.model.galaxy.Galaxy;
import cz.vrbik.pt.semestralka.model.service.Config;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * Třída představující aplikaci spuštěnou bez grafického klienta
 */
public class Nogui implements Runnable {

    private boolean working = true;
    private boolean running = false;
    private Thread worker;
    private Galaxy g;
    private int timestamp = 0;

    public Nogui() {
        worker = new Thread(this, "NoGuiThread");
        g = new Galaxy(new Config());
    }

    @Override
    public void run() {
        while (running) {
            g.update(timestamp++);
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void work() {
        final InputStreamReader r = new InputStreamReader(System.in);
        System.out.println("Vitejte v konzolovem klientu galaxie.");
        System.out.println("Prikazem help zobrazte nabidku moznych prikazu.");
        System.out.println("Preji prijemnou zabavu...");
        while (working) {
            try {
                if (r.ready()) {
                    StringBuilder sb = new StringBuilder();
                    String read;
                    do {
                        sb.append((char) r.read());
                    } while (r.ready());
                    read = sb.toString();
                    if (read.contains("help")) {
                        System.out.println("Napoveda aplikace");
                        System.out.println("Mozne prikazy:");
                        System.out.println("-generate    vygeneruje novou galaxii");
                        System.out.println("-run         spusti simulaci");
                        System.out.println("-stop        zastavi simulaci");
                        System.out.println("-exit        ukonci simulaci pokud bezi a vypne aplikaci");
                    }
                    if (read.contains("generate")) {
                        System.out.println("Generuji novou galaxii");
                        g.generate();
                    }
                    if (read.contains("run")) {
                        System.out.println("Spoustim simulaci...");
                        if (!running) {
                            running = true;
                            worker.start();
                        }
                    }
                    if (read.contains("stop")) {
                        System.out.println("Zastavuji simluaci");
                        running = false;
                    }
                    if (read.contains("exit")) {
                        running = false;
                        working = false;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            /*if (s.hasNextLine()) {
                String read = s.next();
                if (read.contains("generuj")) {
                    System.out.println("Generuji novou galaxii");
                    g.generate();
                }
                if (read.contains("run")) {
                    System.out.println("Spoustim simulaci...");
                    if (!running) {
                        running = true;
                        worker.start();
                    }
                }
                if (read.contains("stop")) {
                    System.out.println("Zastavuji simluaci");
                    running = false;
                }
                if (read.contains("exit")) {
                    running = false;
                    working = false;
                }
            } else System.out.println("NOP");

            System.out.println(Thread.getAllStackTraces().keySet().toString());*/

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
