package com.mswipetech.sdk.network.util.impl.data;

import android.util.Base64;

import com.mswipetech.sdk.network.data.ApplicationData;
import com.mswipetech.sdk.network.util.DESede_BC;
import com.mswipetech.sdk.network.util.generators.KeySedGenerator;
import com.mswipetech.sdk.network.util.impl.ImplData;
import com.mswipetech.sdk.network.util.params.KeyGenerationParameters;

import java.util.Random;


public class MetaDataImpl extends ImplData {

    public final static String log_tab = "MetaDataImpl=>";

    public MetaDataImpl() {
    }

    public static byte[] compressKeyData(byte[] data, int index, int type) throws Exception {

        //getRandomKey();
        DESede_BC desEncryption = null;
        byte[] encyData = null;

        try {
            //random key for 30 index JpBS74yRznIA6MhXeH04isSs //dmdE0NgUlBqvpYh8NrNgvmdE
            KeySedGenerator key = new KeySedGenerator();
            key.init(new KeyGenerationParameters(new Random(), 24, ""));
            byte[] k1bytes = key.generateKey(index);//"JpBS74yRznIA6MhXeH04isSs";
            key = null;
            if (type == 1) {

              /*  if (ApplicationData.IS_DEBUGGING_ON) {
                    Logs.v(ApplicationData.packName, log_tab + " The key used is " + new String(k1bytes), true, true);
                }
*/
                desEncryption = new DESede_BC(k1bytes);
                encyData = desEncryption.encrypt(data);
                desEncryption = null;

            } else {

                desEncryption = new DESede_BC(k1bytes);
                encyData = desEncryption.decrypt(data);
                desEncryption = null;
            }

        } catch (Exception ex) {
            throw ex;
        }
        return encyData;

    }

    public String compressData(String data) throws Exception {
        //convert data to Bytes using the funcation.
        byte[] outdatabytes = new byte[data.length()];
        for (int i = 0; i < data.length(); i++) {
            outdatabytes[i] = (byte) data.charAt(i);
        }
        long seed = System.currentTimeMillis();
        Random rand = new Random(seed);
        int lenk1 = rand.nextInt(99 + (ApplicationData.index * outdatabytes.length)); // the part 1 length
        lenk1 =1;
     /*   if (ApplicationData.IS_DEBUGGING_ON) {
            Logs.v(ApplicationData.packName, log_tab+ " The k1 " + lenk1, true, true);
        }
*/
        //byte[] outData = compressDataTemp2(outdatabytes, lenk1, 1);
        //String x1 = new String(Base64.encode(compressDataTemp2(outdatabytes, lenk1, 1)));

        return (lenk1 > 9 ? "" + (lenk1 / 10) : "0") + new String(Base64.encode(compressKeyData(outdatabytes, lenk1, 1),Base64.DEFAULT)) + (lenk1 > 9 ? "" + (lenk1 % 10) : "" + lenk1);

    }

    public byte[] extractData(byte[] bytes) throws Exception {
        try {

            /*byte[] bytes = new byte[data.length()];
            for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) data.charAt(i);
            
            }*/

            int lenkl = (bytes[0] - 48) * 10 + (bytes[bytes.length - 1] - 48);
           /* if (ApplicationData.IS_DEBUGGING_ON) {
                Logs.v(ApplicationData.packName, log_tab + " The lenk1  is " + lenkl, true, true);
            }*/

            byte[] indata = new byte[bytes.length - 2];
            System.arraycopy(bytes, 1, indata, 0, bytes.length - 2);
            /*if (ApplicationData.IS_DEBUGGING_ON) {
                Logs.v(ApplicationData.packName, log_tab + " The indata  base 64 is " + new String(indata), true, true);
            }*/
            byte[] inbdata = Base64.decode(indata, Base64.DEFAULT);
           /* if (ApplicationData.IS_DEBUGGING_ON) {
                Logs.v(ApplicationData.packName, log_tab + " The indata is " + new String(inbdata), true, true);
            }*/

            byte[] xbbytes = compressKeyData(inbdata, lenkl, 0);
           /* if (ApplicationData.IS_DEBUGGING_ON) {
                Logs.v(ApplicationData.packName, log_tab + " The extraced data is " + new String(xbbytes), true, true);
            }
*/
            return (xbbytes);

        } catch (Exception ex) {
            throw ex;
        }
    }

    public String extractData(String data, byte[] nbbytesk1) throws Exception {
        try {

            byte[] bytes = new byte[data.length()];
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = (byte) data.charAt(i);

            }
            byte[] outbytes = extractData(bytes);

            return new String(outbytes);

        } catch (Exception ex) {
            throw ex;
        }




    }
}
