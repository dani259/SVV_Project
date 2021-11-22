package serverImplementation;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.sql.Time;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;


public class Server extends Thread {
    protected  static  Socket clientSocket;
    String path = "src/main/TestSite/a.html";
    String path1 = "src/main/TestSite";
    int ok = 1;
    public static  ServerState serverState = ServerState.RUNNING;
    public static String option1 = "1";



    public Server(Socket clientSoc){

        init(clientSoc);


        if(CheckServerState() == 1)
        {
            start();
        }

         if(CheckServerState() == 2)
        {
            ServerMaintenance();
        }


    }

public static void init(Socket clientSoc)
{
    clientSocket = clientSoc;
}

    public int CheckServerState()
    {
        if(serverState == ServerState.RUNNING) {
            return 1;      // return 1 if is in running

        }
        if(serverState == ServerState.MAINTENANCE){
            return 2;      // return 2 if is in maintenance
        }

        try {
            if (serverState == ServerState.STOPPED) {
                clientSocket.close();
                return 0;    // return 0 if is stopped
            }
        } catch (Exception e) {
            System.out.println("Couldn t close socket");
        }
        return 0;
    }


    public void run()
    {
        System.out.println("New Communication Thread Started");
        try{
            InputStream is;

            OutputStream os = new BufferedOutputStream(clientSocket.getOutputStream());
            InputStreamReader isr = new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8);

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


                    //TimeUnit.SECONDS.sleep(4);
                   // is.close();





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
            System.err.println("Problem with Communication Server");

        }
        }



    public  String InputStr(InputStreamReader isr){


       try {

           BufferedReader br = new BufferedReader(isr);
            String[] line = br.readLine().split(" ");


           return path1 + line[1];

           } catch (Exception e){
      System.out.println(e);
            }
            return "";
    }


    public static void GiveServerState(){


        try{
            if(serverState == ServerState.STOPPED)
            {

             clientSocket.close();
             System.exit(1);
            }

        }catch (Exception e)
        {
            System.out.println(e);
        };

        System.out.println("Give the state of the server:\n0.STOP\n1.RUN\n2.MAINTENANCE\n");
        System.out.println("Current status of server : " + serverState + "\n");

        Scanner option = new Scanner(System.in, "UTF-8");
         option1 = option.nextLine();

        if(option1.equals("0")) {
            serverState = ServerState.STOPPED;
        }

         if(option1.equals("1"))
        {
            serverState = ServerState.RUNNING;
        }
         if(option1.equals("2"))
        {
            serverState = ServerState.MAINTENANCE;
        }


        System.out.println("The new state of server : " + serverState + "\n");


         if(serverState != ServerState.STOPPED)
         {
             GiveServerState();
         }
         else
         {
             System.exit(1);
         }

    }


    public void ServerMaintenance()
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
            System.out.println("Problem with Communication Server");

        }
    }

}


