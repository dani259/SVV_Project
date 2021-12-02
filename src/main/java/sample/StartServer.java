package sample;

import java.io.IOException;
import java.net.ServerSocket;

public class StartServer {

    public static ServerSocket serverSocket = null;

    static int ok = 0;
    Server s;

    public void Start_Server(int port, String root, String maintenanceFile) throws Exception{



        try{
            if(ok == 0)
            {
                serverSocket = new ServerSocket(port);
                ok = 1;
            }
            System.out.println("Connection socket created");
            try{
                while(true){

                    System.out.println("Waiting for connection");
                     s =  new Server(serverSocket.accept(), root, maintenanceFile);

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
