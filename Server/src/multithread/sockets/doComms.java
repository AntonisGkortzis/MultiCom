package multithread.sockets;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

class doComms implements Runnable {
    private Socket server;
    private String line,input;

    doComms(Socket server) {
      this.server=server;
    }

    public void run () {

      input="";

      try {
        // Get input from the client
        DataInputStream in = new DataInputStream (server.getInputStream());
        PrintStream out = new PrintStream(server.getOutputStream());
        System.out.println("\tServer: new message received!");
        while((line = in.readLine()) != null && !line.equals(".")) {
          input=input + line;
          out.println("Server: I got:" + line);
          System.out.println("\tI got:" + input);
        }

        // Now write to the client

        System.out.println("Overall message is:" + input);
        out.println("Server: Overall message is:" + input);

        server.close();
        System.out.println("...server terminated!");
      } catch (IOException ioe) {
        System.out.println("IOException on socket listen: " + ioe);
        ioe.printStackTrace();
      }
    }
}