import app.AppInfo;
import java.awt.EventQueue;
import ui.LibraryFrame;

/**
 * Application entry point that prints the group members, applies the system
 * look and feel and launches the themed Swing window on the Event Dispatch
 * Thread.
 *
 * @author Syeda Momina Sherazi (534663), Muqaddus Habib (534742)
 */
public class Main {

    /**
     * Prevents instantiation of this entry-point class.
     */
    private Main() {
    }

    /**
     * Starts the application.
     *
     * @param args command-line arguments; this application does not use them
     */
    public static void main(String[] args) {
        System.out.println("Library Book Management System");
        System.out.println("Group Members: " + AppInfo.MEMBERS);
        System.out.println("Launching the library window...");

        LibraryFrame.Theme.applyLookAndFeel();

        EventQueue.invokeLater(() -> {
            LibraryFrame frame = new LibraryFrame();
            frame.setVisible(true);
        });
    }
}
