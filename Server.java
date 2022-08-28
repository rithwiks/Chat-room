import java.net.*;
import java.io.*;
import java.util.*;

public class Server {
    static Vector<ClientHandler> ar = new Vector<>();
    static int counter = 0;
    public static void main(String args[]) throws IOException {
        
        ServerSocket ss = new ServerSocket(7);
        Socket s;
        while (true)
        {
            s = ss.accept();
            System.out.println("New client received");
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            ClientHandler client = new ClientHandler(s, in, out, "client" + counter);
            Thread t = new Thread(client);
            ar.add(client);
            t.start();
            counter++;
            System.out.println("Client added");
        }
    }
}

class ClientHandler implements Runnable
{
    final PrintWriter out;
    final BufferedReader in;
    final Socket s;
    final String name;
    boolean isloggedin;

    public ClientHandler(Socket s, BufferedReader in, PrintWriter out, String name)
    {
        this.s = s;
        this.in = in;
        this.out = out;
        this.name = name;
        isloggedin = true;
    }

    @Override
    public void run()
    {
        String input;
        String receiver = null;
        while (true)
        {
            try {
                input = in.readLine();
                System.out.println(input);
                for (int i = 0; i < input.length(); i++)
                {
                    if (input.charAt(i) == ',')
                    {
                        receiver = input.substring(i+2);
                        input = input.substring(0, i);
                        break;
                    }
                }
                System.out.println(receiver);
                System.out.println(input);
                if (receiver != null)
                {
                    for (ClientHandler c: Server.ar)
                    {
                        if (c.name.equals(receiver) && c.isloggedin==true)
                        {
                            c.out.println(this.name + ": " + input);
                        }
                    }
                    if (input.equals("exit"))
                    {
                        this.out.println("exit");
                        out.close();
                        in.close();
                        s.close();
                        isloggedin = false;
                        break;
                    }
                }
            } catch (Exception e) {}
        }
    }
}
