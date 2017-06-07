import java.io.*;
import java.util.HashMap;

class Disassembler8080 {
    /*
    Class for interpreting and executing the CPU opcodes that exists within the
    ROM files.
    */
    
    // Some quality-of-life methods
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

    private interface Code {
        public void execute(byte[] rom, int pointer);
    }
    public class OpCode implements Code {
        String function;
        int opbytes;
        byte code;
        byte[] buffer;

        public OpCode(int byteSize, byte byteCode, String instruction){
            this.opbytes = byteSize;
            this.code = byteCode;
            this.function = instruction;

            if (this.opbytes > 1){
                this.buffer = new byte[this.opbytes-1];
            }
        }

        @Override
        public void execute(byte[] rom, int pointer){
            //Currently returns a string for testing purposes
            if (this.opbytes == 1){
                print(opbytes + " " + this.function);
            }
            else if (this.opbytes == 2){
                buffer[0] = rom[pointer+1];
                print(opbytes + " " + this.function + " " + 
                    String.format("%02x", buffer));
            }
            else {
                buffer[0] = rom[pointer+2];
                buffer[1] = rom[pointer+1];
                print(opbytes + " " + this.function + " " +
                    String.format("%1$02x %2$02x", buffer[0], buffer[1]));
            }
        }
    }

    // Finally create the library to house all the opcodes.  Will probably
    // also facilitate a factory handling design.  This might deprecate
    // the current state of exOpCode
    public class OpCodes {
        HashMap<Byte, OpCode> opCodeLib = new HashMap<Byte, OpCode>();
        
        public OpCodes(){
            opCodeLib.put((byte)0x00, new OpCode(1, (byte)0x00, "NOP"));
            opCodeLib.put((byte)0x01, new OpCode(3, (byte)0x01, "LXI B"));
        }

        public void exOp(byte opCode, byte[] rom, int pointer){
            String str = "Code not found";
            if (opCodeLib.containsKey(opCode)){
                opCodeLib.get(opCode).execute(rom, pointer);
            }
            else { print(str); }
        }
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
