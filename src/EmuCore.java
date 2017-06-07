import java.io.*;

class Disassembler8080 {
    void print(String str){
        System.out.println(str);
    }
    String opString(String str, byte code1, byte code2){
        StringBuilder op = new StringBuilder();
        op.append(str);
        op.append(String.format(" %02x", code2));
        op.append(String.format(" %02x", code1));
        return op.toString();
    }
    String opString(String str, byte code){
        StringBuilder op = new StringBuilder();
        op.append(str);
        op.append(String.format(" %02x", code));
        return op.toString();
    }

    public int exOpCode(byte[] rom, int i){
        // There's really not a great way that I can see around a mega switch
        // without taking the minor performance hit from converting the byte
        // to an int and using the Adapter design scheme
        byte opcode = rom[i];
        int opbytes = 1;
        switch (opcode) {
            case 0x00: print("NOP"); opbytes = 1; break;
            //case 0x01: String op = String.format("LXI B %1$02x %2$02x", 
            //                        rom[i+2], rom[i+1]);
            case 0x01: String op = opString("LXI B", rom[i+1], rom[i+2]);
                       opbytes = 3;
                       print(op);
                                    
        }

        return opbytes;
    }
}

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
            int tmp = disassembler.exOpCode(romh, i);
        }
        System.out.println(romh.length);
    }
}
