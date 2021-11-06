package serverImplementation;

import java.io.*;
import java.net.*;
import java.sql.Time;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;


public class Server extends Thread {
    protected  static  Socket clientSocket;
    String path = "src/main/TestSite/a.html";
    String path1 = "src/main/TestSite";
    int ok = 1;
    private static  ServerState serverState = ServerState.RUNNING;
    private static ServerSocket serverSocket = null;

    public static void main(String[] args) throws Exception{


        Thread starting = new Thread(){


            public void run() {
                CheckServerState();
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

    private Server(Socket clientSoc){
        clientSocket = clientSoc;
        if(serverState == ServerState.RUNNING) {
            start();
        }

        if(serverState == ServerState.MAINTENANCE){
                ServerMaintenance();
            }
        try {
            if (serverState == ServerState.STOPPED) {
                serverSocket.close();
            }
        }catch (Exception e)
        {
            System.out.println("Couldn t close socket");
        }

    }

    public void run()
    {
        System.out.println("New Communication Thread Started");
        try{
            InputStream is;

            OutputStream os = new BufferedOutputStream(clientSocket.getOutputStream());
            InputStreamReader isr = new InputStreamReader(clientSocket.getInputStream());

            if(ok == 1){

                path = InputStr(isr);
                ok = 0;
            }
            File file = new File(path);
            System.out.println("----------" + path);

            if(file.exists())
            {

                int length;

                try{

                    byte[] data = new byte[(int) file.length()];
                    is = new BufferedInputStream(new FileInputStream(file));

                    os.write("HTTP:/1.0 200 OK\r\n\r\n".getBytes("UTF-8"));
                    while((length = is.read(data)) > 0)
                    {
                        os.write(data, 0, length);
                    }


                    
                    is.close();





                }catch (Exception e)
                {

                    System.out.println("Can't read");
                }

                os.flush();
            }
            else
            {
                System.out.println("File not found");
            }






                clientSocket.close();
        } catch (IOException e) {
            System.out.println(e);
            System.err.println("Problem with Communication Server");
            //System.exit(1);
        }
        }



    public  String InputStr(InputStreamReader isr){


        try {

            BufferedReader br = new BufferedReader(isr);
            String[] line = br.readLine().split(" ");

            while(line[1].indexOf("%20") != -1)
            {

                line[1].replace("%20", " ");
            }

            return path1 + line[1];

        } catch (Exception e){
            System.out.println(e);
        }
        return "";
    }


    private static void CheckServerState(){

        try{
            if(serverState == ServerState.STOPPED)
            {

                serverSocket.close();
                System.exit(1);
            }

        }catch (Exception e)
        {
            System.out.println(e);
        };

        System.out.println("Give the state of the server:\n0.STOP\n1.RUN\n2.MAINTENANCE\n");
        System.out.println("Current status of server : " + serverState + "\n");

        Scanner option = new Scanner(System.in);

        if(option.nextLine().equals("0"))
        {
            serverState = ServerState.STOPPED;
        }
        else if(option.nextLine().equals("1"))
        {
            serverState = ServerState.RUNNING;
        }
        else if(option.nextLine().equals("2"))
        {
            serverState = ServerState.MAINTENANCE;
        }




        System.out.println("The new state of server : " + serverState + "\n");


        CheckServerState();



    }


    private void ServerMaintenance()
    {

        try{
            InputStream is;
            OutputStream os = new BufferedOutputStream(clientSocket.getOutputStream());

            File myMaintenanceFile = new File("src/main/TestSite/maintenance.html");

            try{
                byte[] data = new byte[((int)myMaintenanceFile.length())];
                is = new BufferedInputStream(new FileInputStream(myMaintenanceFile));
                os.write("HTTP:/1.0 200 OK\r\n\r\n".getBytes("UTF-8"));
                int length;
                while((length = is.read(data)) > 0)
                {
                    os.write(data, 0, length);
                }

            }catch (Exception e){

                System.out.println("Can't read maintenance file");
            }

            os.flush();
            clientSocket.close();
        }catch (Exception e){
            System.err.println("Problem with Communication Server");
            //System.exit(1);
        }

    }

}


