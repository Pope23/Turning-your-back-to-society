package main;

import javax.swing.JFrame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

public class GameWindow extends JFrame{
    private JFrame jframe;
    public GameWindow(GamePannel gamepanel) {
        jframe = new JFrame();
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.add(gamepanel);
        jframe.setResizable(false);
        jframe.pack();
        jframe.setLocationRelativeTo(null);
        jframe.setVisible(true);
        jframe.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {

            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                gamepanel.getGame().windowFocusLost();
            }
        });
    }
}
