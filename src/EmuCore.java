import java.io.*;
import java.util.HashMap;
import Memory
import Disassembler8080


public class EmuCore {
    public static void main(String[] args) throws IOException {
        FileInputStream invadersTest = null;
       
        byte[] romh;
        try {	
            invadersTest = new FileInputStream(args[0]); //Need to reed ROM
            int avail = invadersTest.available();
            romh = new byte[avail];
            invadersTest.read(romh);
        } finally {
            if (invadersTest == null){
                invadersTest.close();
            }
        }
        Disassembler8080 disassembler = new Disassembler8080();
        for (int i = 0; i < romh.length; i++){
            //int tmp = disassembler.exOpCode(romh, i);
            disassembler.getOp(romh[i], romh, i);
        }
        System.out.println(romh.length);
    }
}
