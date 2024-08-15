package redmine.interfaceGrafica;

import redmine.runner.ThreadB;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.swing.*;

@Data
@NoArgsConstructor
public class DefaultGUI extends JFrame {

    public ThreadB threadB;

    public DefaultGUI(String titulo) {
        super(titulo);
    }

}
