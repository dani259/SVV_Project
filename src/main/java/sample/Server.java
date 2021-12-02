package sample;


import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.scene.paint.Paint;
import javafx.util.Duration;


import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

import java.sql.Time;
import java.util.concurrent.TimeUnit;


public class Server extends Thread {
    protected  static  Socket clientSocket;

    int ok = 1;



  //  public static Controller c = new Controller();
   // public static ServerState serverState = c.getInstance().serverState;
   // public static ServerState serverState = Controller.serverState;
    public String maintenaceFile;
    public String path1;




    public Server(Socket clientSoc, String root, String maintenanceFile) throws IOException {


        init(clientSoc);
        path1 = root;
        this.maintenaceFile = maintenanceFile;


        if(CheckServerState() == 1)
        {
            run1();
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

    public int CheckServerState()  {


        if(Controller.serverState == ServerState.RUNNING) {
            return 1;      // return 1 if is in running

        }
        if(Controller.serverState == ServerState.MAINTENANCE){

            return 2;      // return 2 if is in maintenance
        }

        try {
            if (Controller.serverState == ServerState.STOPPED) {
                clientSocket.close();

                return 0;    // return 0 if is stopped
            }
        } catch (Exception e) {
            System.out.println("Couldn t close socket");
        }
        return 0;
    }


    public void run1()
    {
        System.out.println("Hello");
        System.out.println("New Communication Thread Started");
        try{
            InputStream is;

            OutputStream os = new BufferedOutputStream(clientSocket.getOutputStream());
            InputStreamReader isr = new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8);

            if(ok == 1){

                path1 = InputStr(isr);
                ok = 0;
            }
           // File file = new File(path);
            File file = new File(path1 );
            System.out.println("----------" + path1);

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

               System.out.println("File " + file.getName() + " not found or directory is not valid");

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


    public void ServerMaintenance()
    {

        try{
            InputStream is;
            OutputStream os = new BufferedOutputStream(clientSocket.getOutputStream());

            //File myMaintenanceFile = new File("src/TestSite/maintenance.html");
            File myMaintenanceFile = new File(maintenaceFile);

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
            System.out.println("Problem with Communication Server  " + maintenaceFile + e);

        }
    }

}


