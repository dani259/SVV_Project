package serverImplementation;


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.net.ServerSocket;
import java.net.Socket;


public class ServerTest {


    public Server server = null;
    Socket clientSocket;
    ServerSocket serverSocket;
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {

        try {

            serverSocket.close();
        }
        catch (Exception e)
        {
            System.out.println("Couldn't close port 8080");
        }
    }


    @Test
    public void CheckServerStateTest1() throws Exception {
         serverSocket = new ServerSocket(8080);
        clientSocket = serverSocket.accept();
        server = new Server(clientSocket);

        Server.serverState = ServerState.RUNNING;

        assertEquals(server.CheckServerState(), 1);


    }



    @Test
    public void CheckServerStateTest2() throws Exception {
         serverSocket = new ServerSocket(8080);
         clientSocket = serverSocket.accept();
        server = new Server(clientSocket);

         Server.serverState = ServerState.MAINTENANCE;

        assertEquals(server.CheckServerState(), 2);



    }


    @Test
    public void CheckServerStateTest3() throws Exception {
         serverSocket = new ServerSocket(8080);
         clientSocket = serverSocket.accept();
        server = new Server(clientSocket);

        Server.serverState = ServerState.STOPPED;

        assertEquals(server.CheckServerState(), 0);



    }

    @Test
    public void ServerMaintenanceTest() throws Exception {
         serverSocket = new ServerSocket(8080);
         clientSocket = serverSocket.accept();
        server = new Server(clientSocket);

        Server.serverState = ServerState.MAINTENANCE;
        server.ServerMaintenance();
        assertEquals(server.CheckServerState(), 2);

    }
    @Test
    public void RunTest() throws Exception {

         serverSocket = new ServerSocket(8080);
         clientSocket = serverSocket.accept();
        server = new Server(clientSocket);
        Server.serverState = ServerState.MAINTENANCE;

        server.run();

        assertEquals(server.CheckServerState(), 2);

    }

    @Test
    public void GiveServerStateTest() throws Exception {
        serverSocket = new ServerSocket(8080);
        clientSocket = serverSocket.accept();
        server = new Server(clientSocket);

        Thread starting = new Thread(){


            public void run() {
                Server.serverState = ServerState.STOPPED;
                Server.GiveServerState();

            }
        };

        starting.start();



        assertEquals(server.CheckServerState(), 0);



    }


}