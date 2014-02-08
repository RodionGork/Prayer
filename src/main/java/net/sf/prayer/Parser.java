package net.sf.prayer;

import java.util.*;

public class Parser {

    public Map<String, Subroutine> parse(String code) {
        String[] lines = code.split("\\n");
        Map<String, List<List<Object>>> prog = new HashMap<>();
        List<List<Object>> cur = null;
        
        for (String line : lines) {
            if (line.matches("^\\s*\\#.*")) {
                continue;
            }
            if (line.matches("^(?:sub|func)\\s.*")) {
                cur = new ArrayList<>();
                prog.put(line, cur);
            } else if (line.matches("\\s+\\S+.*")) {
                cur.add(parseLine(line));
            }
        }
        
        return makeSubroutines(prog);
    }
    
    public List<Object> parseLine(String s) {
        List<Object> res = new ArrayList<>();
        char[] chars = s.trim().toCharArray();
        int cur = 0;
        StringBuilder sb = new StringBuilder();
        
        while (true) {
            char peek = chars[cur];
            if (peek == '"') {
                sb.setLength(0);
                int start = cur++;
                while (true) {
                    char c = chars[cur++];
                    if (c == '"') {
                        break;
                    }
                    if (c != '\\') {
                        sb.append(c);
                    } else {
                        sb.append(chars[cur++]);
                    }
                }
                res.add(new Literal(sb));
            } else if (peek == '\'') {
                String w = fetchNonSpace(chars, ++cur);
                cur += w.length();
                res.add(new Literal(w));
            } else {
                String w = fetchNonSpace(chars, cur);
                cur += w.length();
                if (w.matches("\\-?\\d+")) {
                    res.add(new Integer(w));
                } else {
                    res.add(w);
                }
            }
            
            if (cur >= chars.length) {
                break;
            }
            
            while (Character.isWhitespace(chars[cur])) {
                cur++;
            }
        }
        
        return res;
    }
    
    private Map<String, Subroutine> makeSubroutines(Map<String, List<List<Object>>> map) {
        Map<String, Subroutine> prog = new HashMap<>();
        for (Map.Entry<String, List<List<Object>>> entry : map.entrySet()) {
            Object[][] subr = new Object[entry.getValue().size()][];
            for (int i = 0; i < subr.length; i++) {
                List<Object> line = entry.getValue().get(i);
                subr[i] = line.toArray(new Object[line.size()]);
            }
            String name = entry.getKey();
            boolean isFunc = name.startsWith("func");
            name = name.replaceFirst("\\S+\\s+", "");
            String[] parts = name.split("\\s+");
            name = parts[0];
            char[] args = new char[parts.length - 1];
            for (int i = 1; i < parts.length; i++) {
                args[i - 1] = parts[i].charAt(0);
            }
            prog.put(name, new Subroutine(name, subr, args, isFunc));
        }
        return prog;
    }
    
    private String fetchNonSpace(char[] chars, int from) {
        int to = from;
        while (to < chars.length && !Character.isWhitespace(chars[to])) {
            to++;
        }
        return new String(chars, from, to - from);
    }
}

