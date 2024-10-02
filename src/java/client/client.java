package src.java.client;

import javax.net.ssl.*;
import java.io.*;
import java.security.KeyStore;

public class client {
    // TLS client implementation in Java using SSLSocket
    public static void main(String[] args) throws Exception{
        String host = "localhost";
        int port = 8993;

        try {
            // load the client truststore containing the root CA certificate
            KeyStore trustStore = KeyStore.getInstance("JKS");
            trustStore.load(new FileInputStream("src/certs/rootCA.jks"), "passwd".toCharArray());

            // initialise the trust manager factory with the truststore
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(trustStore);

            // initialise the SSL context with the trust managers
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);

            // create SSL socket and connect to server
            SSLSocketFactory SSF = sslContext.getSocketFactory();
            SSLSocket s = (SSLSocket) SSF.createSocket(host, port);

            // send a message to the server
            System.out.println("Connected to the server");

            PrintWriter writer = new PrintWriter(s.getOutputStream(), true);
            writer.println("Hi Server!");

            // read the server's response
            BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
            System.out.println("Server says: " + reader.readLine());

            // close the connection
            s.close();

        } catch (IOException e) {
            System.err.println("IOException occurred: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }

    }
}
