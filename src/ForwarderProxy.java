/*
 * ForwarderProxy.java
 * 
 * Class ini bertugas mengambil respon dari proxy operator
 * kemudian mengirimkannya ke client.
 */

import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

class ForwarderProxy extends Thread {

    Socket sckMasuk, sckKeluar;

    ForwarderProxy(Socket in, Socket out) {
        this.sckMasuk = in;
        this.sckKeluar = out;
    }

    @Override
    public void run() {
        

        /*
         * TIP. Buat nilai buffer sekecil mungkin untuk mempercepat respon
         */
        byte[] buffer = new byte[512];
        int numberRead = 0;
        OutputStream os;
        InputStream is;
        
        try {
            os = sckKeluar.getOutputStream();
            is = sckMasuk.getInputStream();

            while (true) {

                numberRead = is.read(buffer);

                if (numberRead == -1) {

                    sckMasuk.close();
                    sckKeluar.close();
                    System.out.println("PROXY : End data.. socket closed!");
                }
                
//                if(byteBufferToString(buffer, 50).startsWith("HTTP/1.1 200 Connection established")){
//                    os.write("HTTP/1.0 200 Connection established".getBytes());
//                }else{
//                    
//                }
                os.write(buffer, 0, numberRead);
                
            }

        } catch (IOException | ArrayIndexOutOfBoundsException e) {
        }

    }

    /*
     * Ini adalah method tambahan untuk mengambil String respon proxy operator
     * method ini sangat berguna jika ingin memanipulasi respon dari proxy operator
     */
    private static String byteBufferToString(byte[] buffer, int limit) {
        StringBuilder sb = new StringBuilder();
        for (byte b : buffer) {
            sb.append((char) b);
        }
        return sb.toString().substring(0, limit);

    }
}
