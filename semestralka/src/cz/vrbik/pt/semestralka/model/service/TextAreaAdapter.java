package cz.vrbik.pt.semestralka.model.service;

import javafx.scene.control.TextArea;
import org.apache.log4j.WriterAppender;
import org.apache.log4j.spi.LoggingEvent;

/**
 * Adapter pro logování do text areay
 */
public class TextAreaAdapter extends WriterAppender {

    public static TextArea ta;

    @Override
    public void append(LoggingEvent event) {
        final String logMessage = this.layout.format(event);
        if (ta != null)
            ta.appendText(logMessage);
    }
}
