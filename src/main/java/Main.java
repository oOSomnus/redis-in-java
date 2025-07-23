import dispatcher.CommandDispatcher;
import handlers.*;
import parsers.BatchReader;
import utils.BufferReaderUtil;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // You can use print statements as follows for debugging, they'll be visible when running tests.
        System.out.println("Logs from your program will appear here!");

        //  Uncomment this block to pass the first stage
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        int port = 6379;
        try {
            serverSocket = new ServerSocket(port);
            // Since the tester restarts your program quite often, setting SO_REUSEADDR
            // ensures that we don't run into 'Address already in use' errors
            serverSocket.setReuseAddress(true);
            while (true) {
                // Wait for connection from client.
                clientSocket = serverSocket.accept();
                System.out.println("New client connected!");
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } finally {
            try {
                if (clientSocket != null) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                System.out.println("IOException: " + e.getMessage());
            }
        }
    }

    static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try (
                    InputStream inputStream = this.clientSocket.getInputStream();
                    OutputStream outputStream = this.clientSocket.getOutputStream();
            ) {
                BatchReader batchReader = new BatchReader(inputStream);
                List<String> commandList;
                while ((commandList = batchReader.readBatch()) != null) {
                    String retVal = CommandDispatcher.dispatch(commandList);
                    if(retVal == null){
                        continue;
                    }
                    outputStream.write(retVal.getBytes(StandardCharsets.UTF_8));
                    outputStream.flush();
                }
            } catch (IOException e) {
                System.out.println("IOException: " + e.getMessage());
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    System.out.println("IOException: " + e.getMessage());
                }
            }
        }
    }
}
