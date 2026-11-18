import app.AppInfo;
import java.awt.EventQueue;
import ui.LibraryFrame;

/** Entry point that launches the Swing GUI on the Event Dispatch Thread. */
public class Main {

    public static void main(String[] args) {
        System.out.println("Library Book Management System");
        System.out.println("Group Members: " + AppInfo.MEMBERS);
        System.out.println("Launching the library window...");

        EventQueue.invokeLater(() -> {
            LibraryFrame frame = new LibraryFrame();
            frame.setVisible(true);
        });
    }
}
