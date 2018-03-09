
/*
 * HttpProxy.java
 * 
 * Class ini merupakan class yang menjalankan http proxy sederahana
 * 
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Reza Elborneo
 */
public class HttpProxy extends Thread {

    ServerSocket server;
    int listenPort = 80;

    @Override
    public void run() {
        try {
            /*
             * Mulai service sebagai http proxy
             */
            server = new ServerSocket(listenPort);
            System.out.println("Listening to port " + listenPort);
        } catch (IOException ex) {
            Logger.getLogger(HttpProxy.class.getName()).log(Level.SEVERE, null, ex);
            this.interrupt();
        }

        while (true) {
            try {
                /*
                 * Selama service berjalan, terima semua request yang masuk
                 */
                Socket tc = server.accept();
                System.out.println("Incoming connection accepted..");
                BufferedReader br = new BufferedReader(new InputStreamReader(tc.getInputStream()));
                String l;
                RequestHandler rh = new RequestHandler(tc);
                /*
                 * Ambil request client
                 */
                while ((l = br.readLine()) != null) {
                    rh.setPayloadc(l+"\r\nConnection:keep-alive\r\nProxy-Connection:keep-alive\r\n\r\n");

                    break;
                }
               rh.start();
            } catch (IOException ex) {
                Logger.getLogger(HttpProxy.class.getName()).log(Level.SEVERE, null, ex);
                break;
            }
        }
    }

    public static void main(String args[]) {
        HttpProxy hp = new HttpProxy();
        hp.start();
    }
}
