import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class ServerCommunication {
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;

    public void startConnection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip,port);
        out = new PrintWriter(clientSocket.getOutputStream(),true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public void sendMessage(String msg) throws IOException {
        out.println(msg);
    }

    public String recMessage() throws IOException{
        return in.readLine();
    }
}
