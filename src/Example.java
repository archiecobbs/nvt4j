
import java.net.*;
import org.dellroad.nvt4j.Terminal;

public class Example {

    public static void main(String[] args) throws Exception {

        /*
         * Example: moving "Hello World!" around the screen.
         *
         * Start this program in the background, then type:
         * 'telnet localhost 8000'
         */

        System.err.println("Now telnet to port 8000");

        ServerSocket serverSocket = new ServerSocket(8000);
        Socket socket = serverSocket.accept();
        Terminal terminal = new nvt4j.impl.Terminal(socket);

        terminal.put(10, 10, "Use keys 6,7,8,9 to move the text");
        Thread.sleep(2000);
        terminal.clear();

        String text   = "Hello World!";
        String blanks = "            ";

        int x = 10;
        int y = 10;
        terminal.put(x, y, text);

        int r;
        while ((r = terminal.get()) != -1) {
            int oldX = x;
            int oldY = y;
            switch (r) {
            case '6':
                if (x > 1) {
                    --x;
                } else {
                    continue;
                }
                break;
            case '9':
                if (x + text.length() < terminal.getColumns() + 1) {
                    ++x;
                } else {
                    continue;
                }
                break;
            case '7':
                if (y < terminal.getRows()) {
                    ++y;
                } else {
                    continue;
                }
                break;
            case '8':
                if (y > 1) {
                    --y;
                } else {
                    continue;
                }
                break;
            default:
                continue;
            }
            terminal.put(oldX, oldY, blanks);
            terminal.put(x, y, text);
        }
    }

}
