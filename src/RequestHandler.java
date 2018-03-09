/*
 * RequestHandler.java
 * 
 * Class ini bertugas mengirim payload.
 * 
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
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
public class RequestHandler extends Thread {

    private String ipProxyProvider = "49.213.25.14";
    private int portProxyProvider = 8080;
    private String payloadc;
    private String payload = "CONNECT 49.213.25.14:1234 HTTP/1.0\r\nConnection:keep-alive\r\nProxy-Connection:keep-alive\r\n\r\n";
    Socket sc, socketProxy;
    

    public RequestHandler(Socket sc) {
        this.sc = sc;
    }

    @Override
    public void run() {

        try {

            socketProxy = socketInject();

            if (socketProxy == null) {
                System.out.println("Socket Inject null.. socket client closed!");
                this.sc.close();

            } else if (socketProxy.isConnected()) {
                System.out.println("Inject sukses..");

                ForwarderClient fc = new ForwarderClient(sc, socketProxy);
                fc.start();


                ForwarderProxy fp = new ForwarderProxy(socketProxy, sc);
                fp.start();

            }
        } catch (UnknownHostException ex) {
            try {
                Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, ex);
                interrupt();
                sc.close();
            } catch (IOException ex1) {
                try {
                    Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, ex1);
                    interrupt();
                    sc.close();
                } catch (IOException ex2) {
                    Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, ex2);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, ex);
            interrupt();
        }
    }

    private Socket socketInject() {
        Socket socket;
        int numberRead = 0;
        byte[] buffer = new byte[1024];
        try {
            /*
             * Buat socket baru untuk koneksi ke proxy dan port operator
             */
            socket = new Socket(ipProxyProvider, portProxyProvider);
            OutputStream os = socket.getOutputStream();
            InputStream is = socket.getInputStream();

            /*
             * Kirim payload ke socket yang sudah dibuat.
             */
            
            payloadc = payload + payloadc;
            
            System.out.println(payloadc);
            os.write(payloadc.getBytes());
            os.flush();

//            numberRead = is.read(buffer);
//
//            if (numberRead == -1) {
//                socket.close();
//                socket = null;
//            }
//
//            numberRead = is.read(buffer);

//            String respon = ambilHeader(is);
//            if (respon.startsWith("HTTP/1.1 200")) {
//                String s = "HEAD http://adpop.telkomsel.com/ HTTP/1.1\r\n\r\n\r\n";
//                s += payloadc;
//
//                os.write(s.getBytes());
//
//                numberRead = is.read(buffer);
//                System.out.println("Mengirim inject..");
//                return socket;
//
//            }


        } catch (Exception ex) {
            socket = null;
        }

        /*
         * Hasil dari method ini yaitu socket itu sendiri yang siap digunakan
         */
        return socket;
    }

    public String getPayloadc() {
        return payloadc;
    }

    public void setPayloadc(String payloadc) {
        this.payloadc = payloadc;
    }

    public String getIpProxyProvider() {
        return ipProxyProvider;
    }

    public void setIpProxyProvider(String ipProxyProvider) {
        this.ipProxyProvider = ipProxyProvider;
    }

    public int getPortProxyProvider() {
        return portProxyProvider;
    }

    public void setPortProxyProvider(int portProxyProvider) {
        this.portProxyProvider = portProxyProvider;
    }

    public String ambilHeader(InputStream is) throws IOException {

        String ret = null;
        byte[] b = new byte[512];
        int a = is.read(b);
        String str = new String(b, 0, a);
        String data[] = str.split("\r\n\r\n");
        ret = data[0];
        System.out.println(data[0]);
//        is.close();

//        StringBuilder sb = new StringBuilder();
//        BufferedReader br = new BufferedReader(new InputStreamReader(is));
//        String line;
//        while (!(line = br.readLine()).startsWith("<head>")) {
//            sb.append(line).append("\r\n");
//        }

//        System.out.println(sb.toString());
        // br.reset();
        return ret;

    }
}
