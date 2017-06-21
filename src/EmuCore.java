import java.io.*;

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
        //for (int i = 0; i < romh.length; i++){
        for (int i = 0; i < 5; i++){
            //int tmp = disassembler.exOpCode(romh, i);
            System.out.println("Index: " + i + ", " + romh[i]);
            disassembler.printOp(romh[i], romh, i); //DEBUG func
        }
        System.out.println(romh.length);
    }
}
