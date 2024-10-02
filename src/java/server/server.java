package src.java.server;

import javax.net.ssl.*;
import java.io.*;
import java.security.KeyStore;

public class server {
    // TLS Server Implementation in Java using SSLSocket
    public static void main(String[] args) throws Exception {
        int port = 8993;

        try {
            // load the server keystore file from the /src/resources dir
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(new FileInputStream("src/certs/server.jks"), "passwd".toCharArray());

            // Initialise key manager factory with the keystore
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            keyManagerFactory.init(keyStore, "passwd".toCharArray());

            // initialise the SSL context with key managers
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), null, null);

            // create a server socket factory
            SSLServerSocketFactory SSF = sslContext.getServerSocketFactory();
            SSLServerSocket s = (SSLServerSocket) SSF.createServerSocket(port);

            System.out.print("Server started on port " + port + "\n");

            // wait for connection
            SSLSocket clientSocket = (SSLSocket) s.accept();

            // handle the connection
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
            System.out.println("Client says: " + reader.readLine());
            writer.println("Hi client");

            clientSocket.close();
            s.close();
        } catch (IOException e) {
            System.err.println("IOException occurred: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }
    }

}
