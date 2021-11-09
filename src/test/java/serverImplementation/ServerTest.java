package serverImplementation;

import com.sun.tools.javac.Main;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import serverImplementation.*;

import static org.junit.Assert.*;

public class ServerTest {


    public Server server = null;
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }


    @Test
    public void CheckServerStateTest() throws Exception {
        ServerSocket serverSocket = new ServerSocket(8080);
        Socket clientSocket = serverSocket.accept();
        server = new Server(clientSocket);

        assertEquals("We are in running state",Server.serverState,ServerState.RUNNING);

        Server.serverState = ServerState.STOPPED;
        server.CheckServerState();

    }

    @Test
    public void ServerMaintenance() throws Exception {
        ServerSocket serverSocket = new ServerSocket(8080);
        Socket clientSocket = serverSocket.accept();
        server = new Server(clientSocket);

         Server.serverState = ServerState.MAINTENANCE;
        server.ServerMaintenance();


    }


    @Test
    public void Run() throws Exception {
        Thread starting = new Thread(){


            public void run() {
                Server.CheckServerState();
            }
        };

        starting.start();

        ServerSocket serverSocket = new ServerSocket(8080);
        Socket clientSocket = serverSocket.accept();
        server = new Server(clientSocket);
        Server.serverState = ServerState.MAINTENANCE;

        server.run();


    }

    @Test
    public void CheckMaintenanceTest() throws Exception {
        Thread starting = new Thread(){


            public void run() {
                Server.serverState = ServerState.MAINTENANCE;
                Server.CheckServerState();

            }
        };

        starting.start();

        ServerSocket serverSocket = new ServerSocket(8080);
        Socket clientSocket = serverSocket.accept();
        server = new Server(clientSocket);





    }


}