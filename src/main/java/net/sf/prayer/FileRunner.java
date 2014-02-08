package net.sf.prayer;

import java.io.*;

public class FileRunner {
    
    public String loadSource(String fileName) {
        try {
            return loadSource(new FileInputStream(fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public String loadSource(InputStream stream) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(stream));
            
            while (true) {
                String line = input.readLine();
                if (line == null) {
                    break;
                }
                sb.append(line).append('\n');
            }
            
            input.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }

    public static void main(String... args) {
    
        String source = new FileRunner().loadSource(args[0]);
        
        Processor proc = new Processor();
        proc.loadProgram(new Parser().parse(source));
        proc.execSubroutine("main");
    }

}

