package serverImplementation;


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import sample.Controller;
import sample.Main;
import sample.Server;
import sample.ServerState;

import static org.junit.Assert.assertEquals;

import java.net.ServerSocket;
import java.net.Socket;


public class ServerTest {


    public Server server;

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
            System.out.println("Couldn't close port 8080" + e);
        }
    }


    @Test
    public void CheckServerStateTest1() throws Exception {

        Controller.serverState = ServerState.RUNNING;
        //Server.serverState = ServerState.RUNNING;
        serverSocket = new ServerSocket(8080);
        server = new Server(serverSocket.accept(), "C:\\Users\\danie\\Desktop\\serverGUI\\src\\main\\java\\TestSite", "src/main/java/TestSite/maintenance.html");


        assertEquals(server.CheckServerState(), 1);

    }






    @Test
    public void ServerMaintenanceTest() throws Exception {

        Controller.serverState = ServerState.MAINTENANCE;
        //Server.serverState = ServerState.MAINTENANCE;
        serverSocket = new ServerSocket(8080);
        server = new Server(serverSocket.accept(), "C:\\Users\\danie\\Desktop\\serverGUI\\src\\main\\java\\TestSite", "src/main/java/TestSite/maintenance.html");



        server.ServerMaintenance();
        assertEquals(server.CheckServerState(), 2);



    }
    @Test
    public void RunMethod() throws Exception {


        Controller.serverState = ServerState.RUNNING;
        //Server.serverState = ServerState.RUNNING;
        serverSocket = new ServerSocket(8080);
        server = new Server(serverSocket.accept(), "C:\\Users\\danie\\Desktop\\serverGUI\\src\\main\\java\\TestSite", "src/main/java/TestSite/maintenance.html");


        server.run1();
        assertEquals(server.CheckServerState(), 1);

    }


    @Test
    public void CheckForStopped() throws Exception {

        Controller.serverState = ServerState.STOPPED;
        //Server.serverState = ServerState.STOPPED;
        serverSocket = new ServerSocket(8080);
        server = new Server(serverSocket.accept(), "C:\\Users\\danie\\Desktop\\serverGUI\\src\\main\\java\\TestSite", "src/main/java/TestSite/maintenance.html");



        assertEquals(server.CheckServerState(), 0);

    }

}