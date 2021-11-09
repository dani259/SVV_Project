package MainMethod;

import serverImplementation.Server;

import java.io.IOException;
import java.net.ServerSocket;
import static serverImplementation.Server.CheckServerState;


public class Main {

    private static ServerSocket serverSocket = null;

    public static void main(String[] args) throws Exception{



        Thread starting = new Thread(){


            public void run() {

                Server.CheckServerState();
            }
        };

        starting.start();

        try{
            serverSocket = new ServerSocket(8080);
            System.out.println("Connection socket created");
            try{
                while(true){
                    System.out.println("Waiting for connection");
                    new Server(serverSocket.accept());
                }
            }catch(IOException e){
                System.err.println("Accept failed.");
                System.exit(1);
            }

        }catch (IOException e){
            System.err.println("Couldn't listen on port 8080");
            System.exit(1);
        }finally {
            try {
                serverSocket.close();
            }
            catch (IOException e )
            {
                System.err.println("Couldn't close port 8080");
                System.exit(1);
            }
        }


    }
}
