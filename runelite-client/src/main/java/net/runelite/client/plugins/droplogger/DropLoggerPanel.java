package net.runelite.client.plugins.droplogger;

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

@Slf4j
public class DropLoggerPanel extends PluginPanel {
    private final JTextArea dropLog = new JTextArea();

    void init(DropLoggerConfig config){
        // this may or may not qualify as a hack
        // but this lets the editor pane expand to fill the whole parent panel
        getParent().setLayout(new BorderLayout());
        getParent().add(this, BorderLayout.CENTER);

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(ColorScheme.DARK_GRAY_COLOR);

        dropLog.setTabSize(2);
        dropLog.setLineWrap(true);
        dropLog.setWrapStyleWord(true);

        JPanel logContainer = new JPanel();
        logContainer.setLayout(new BorderLayout());
        logContainer.setBackground(ColorScheme.DARKER_GRAY_COLOR);

        dropLog.setOpaque(false);

        // load note text
        String data = config.dropsData();
        dropLog.setText(data);

        dropLog.addFocusListener(new FocusListener()
        {
            @Override
            public void focusGained(FocusEvent e)
            {

            }

            @Override
            public void focusLost(FocusEvent e)
            {
                logChanged(dropLog.getDocument());
            }

            private void logChanged(Document doc)
            {
                try
                {
                    // get document text and save to config whenever editor is changed
                    String data = doc.getText(0, doc.getLength());
                    config.dropsData(data);
                }
                catch (BadLocationException ex)
                {
                    log.warn("Notes Document Bad Location: " + ex);
                }
            }
        });
        logContainer.add(dropLog, BorderLayout.CENTER);
        logContainer.setBorder(new EmptyBorder(10, 10, 10, 10));

        add(logContainer, BorderLayout.CENTER);
    }

    void setDrops(String data)
    {
        dropLog.setText(data);
    }

}
