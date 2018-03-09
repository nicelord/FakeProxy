/*
 * ForwarderClient.java
 * 
 * Class ini bertugas mengambil data request client
 * kemudian mengirimkannya ke proxy operator.
 */

import java.net.*;
import java.io.*;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;

class ForwarderClient extends Thread {

    Socket sckMasuk, sckKeluar;

    ForwarderClient(Socket in, Socket out) {
        this.sckMasuk = in;
        this.sckKeluar = out;
    }

    @Override
    public void run() {
        
        /*
         * TIP. Buat nilai buffer sekecil mungkin untuk mempercepat respon
         */
        byte[] buffer = new byte[128];
        int numberRead = 0;
        OutputStream os;
        InputStream is;
        try {
            os = sckKeluar.getOutputStream();
            is = sckMasuk.getInputStream();

            while (true) {

                numberRead = is.read(buffer);

                /*
                 * Koneksi socket akan ditutup apabila sudah tidak ada lagi data yang diterima dari client
                 * atau bernilai null, dalam byte berarti -1
                 */
                if (numberRead == -1) {
                    sckMasuk.close();
                    sckKeluar.close();
                    System.out.println("CLIENT : End data.. socket closed!");
                }

                
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
