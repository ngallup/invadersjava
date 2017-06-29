import java.util.HashMap;

public class Disassembler8080 {
    /*
    Class for interpreting and executing the CPU opcodes that exists within the
    ROM files.
    */

    //OpCodes opLib = new OpCodes();
    
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

    public interface OpCode {
        /*
        These will need access to the memory registers in the future, once
        debugged.  Will need to pass the appropriate register address 
        probably in the constructor.  Could probably also get away with a
        'getReg' function within the Memory class
        */

        public void printOp(byte[] rom, int pointer);
        public void execute(byte[] rom, int pointer);
    }

    public abstract class OpCodeObj implements OpCode {
        String function;
        String reg;
        int opbytes;
        byte code;
    }

    public class NOP extends OpCodeObj {
        /*
        The No Op class, when the CPU is fed an opcode it doesn't recognize
        */
        public NOP(int byteSize, byte byteCode, String instruct, String reg){
            this.opbytes = byteSize;
            this.code = byteCode;
            this.function = instruct;
            this.reg = reg; //Will need to accept actual reg addr in future
        }

        @Override
        public void printOp(byte[] rom, int pointer){
            print(this.opbytes + " " + this.function);
        }

        @Override
        public void execute(byte[] rom, int pointer){;} //Empty for now
    }

    public class LXI extends OpCodeObj {
        /*
        Loads 16-bit data/address into a register pair
        */
        public LXI(int byteSize, byte byteCode, String instruct, String reg){
            this.opbytes = byteSize;
            this.code = byteCode;
            this.function = instruct;
            this.reg = reg;
        }

        @Override
        public void printOp(byte[] rom, int pointer){
            StringBuilder str = new StringBuilder();
            str.append(this.opbytes);
            str.append(" ");
            str.append(this.function);
            str.append(this.reg);
            String data = String.format(
                "%1$02x%2$02x", 
                rom[pointer+2], 
                rom[pointer+1]);
            str.append(data);
             print(str.toString());
        }

        @Override
        public void execute(byte[] rom, int pointer){;} //Empty for now 
    }

    public class STAX extends OpCodeObj {
        /*
        Store accumulator (A) in address register pair
        */
        public STAX(int byteSize, byte byteCode, String instruct, String reg){
            this.opbytes = byteSize;
            this.code = byteCode;
            this.function = instruct;
            this.reg = reg;
        }

        @Override
        public void printOp(byte[] rom, int pointer){
            print(this.opbytes + " " + this.function + this.reg);
        }

        @Override
        public void execute(byte[] rom, int pointer){;} //Fill later
    }

    public class INX extends OpCodeObj {
        /*
        Increment the specified register pair by 1
        */
        public INX(int byteSize, byte byteCode, String instruct, String reg){
            this.opbytes = byteSize;
            this.code = byteCode;
            this.function = instruct;
            this.reg = reg;
        }

        @Override
        public void printOp(byte[] rom, int pointer){
            print(this.opbytes + " " + this.function + this.reg);
        }
        
        @Override
        public void execute(byte[] rom, int pointer){;} //Fill later
    }

    public class INR extends OpCodeObj {
        /*
        Increment the specified register by 1
        */
        public INR(int byteSize, byte byteCode, String instruct, String reg){
            this.opbytes = byteSize;
            this.code = byteCode;
            this.function = instruct;
            this.reg = reg;
        }
        
       @Override
       public void printOp(byte[] rom, int pointer){
           print(this.opbytes + " " + this.function + this.reg);
       }

       @Override
       public void execute(byte[] rom, int pointer){;} //Fill later
    }

    public class DCR extends OpCodeObj {
        /*
        Decrement the specified register by 1
        */
        public DCR(int byteSize, byte byteCode, String instruct, String reg){
            this.opbytes = byteSize;
            this.code = byteCode;
            this.function = instruct;
            this.reg = reg;
        }

        @Override
        public void printOp(byte[] rom, int pointer){
            print(this.opbytes + " " + this.function + this.reg);
        }

        @Override
        public void execute(byte[] rom, int pointer){;} //Fill later
    }

    public class MVI extends OpCodeObj {
        /*
        Copy byte to the specified register
        */
        public MVI(int byteSize, byte byteCode, String instruct, String reg){
            this.opbytes = byteSize;
            this.code = byteCode;
            this.function = instruct;
            this.reg = reg;
        }

        @Override
        public void printOp(byte[] rom, int pointer){
            String copyByte = String.format("%02x", rom[pointer+1]);
            print(this.opbytes + " " + 
                    this.function + this.reg + 
                    " " + copyByte);
        }

        @Override
        public void execute(byte[] rom, int pointer){;} //Fill later
    }

    public boolean opExists(byte buffer){
        if (opCodeLib.containsKey(buffer)){
            return true;
        }
        return false;
    }

    public void printOp(byte buffer, byte[] rom, int i){
        if (opCodeLib.containsKey(buffer)){
            OpCode code = opCodeLib.get(buffer);
            code.printOp(rom, i);
        }
        else {
            String op = String.format("%02x", buffer);
            print("Opcode not found: " + op);
        }
    }

    // Declare Opcode hashmap and populate
    HashMap<Byte, OpCode> opCodeLib = new HashMap<Byte, OpCode>();

    public Disassembler8080(){//Will need to accept memory class in future
        opCodeLib.put((byte)0x00, new NOP(1, (byte)0x00, "NOP", "None"));
        opCodeLib.put((byte)0x01, new LXI(3, (byte)0x01, "LXI", "BC"));
        opCodeLib.put((byte)0x02, new STAX(1, (byte)0x02, "STAX", "BC"));
        opCodeLib.put((byte)0x03, new INX(1, (byte)0x03, "INX +1", "BC"));
        opCodeLib.put((byte)0x04, new INR(1, (byte)0x04, "INR +1", "B"));
        opCodeLib.put((byte)0x05, new DCR(1, (byte)0x05, "DCR -1", "B"));
        opCodeLib.put((byte)0x06, new MVI(2, (byte)0x06, "MVI", "B"));
    }

    //Debug main, get rid when done
    public static void main(String[] args){
        Disassembler8080 test = new Disassembler8080();
        System.out.println(test.opExists((byte)0x00));
        System.out.println(test.opExists((byte)0x01));
        System.out.println(test.opExists((byte)0x02));
        System.out.println(test.opExists((byte)0x03));
    }
}

