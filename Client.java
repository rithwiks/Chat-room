import java.net.*;
import java.io.*;


public class Client {
    
    public static void main(String args[]) throws UnknownHostException, IOException{
        Socket s = new Socket("localhost", 7);
        PrintWriter out = new PrintWriter(s.getOutputStream(), true);
        BufferedReader in  = new BufferedReader(new InputStreamReader (s.getInputStream()));
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        Thread sendMessage = new Thread (new Runnable()
        {
            @Override
            public void run()
            {
                while (true)
                {
                    try{
                        String message = stdIn.readLine();
                        out.println(message);
                        if (message.equals("exit"))
                            break;
                    } catch (IOException e){}
                }
            }
        });
        Thread readMessage = new Thread (new Runnable()
        {
            @Override
            public void run()
            {
                while (true)
                {
                    try{
                        String input = in.readLine();
                        System.out.println(input);
                        if (input.equals("exit"))
                            break;
                    } catch (IOException e){}
                }
            }
        });
        sendMessage.start();
        readMessage.start();
    }
}
