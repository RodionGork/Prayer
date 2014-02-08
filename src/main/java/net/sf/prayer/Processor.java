package net.sf.prayer;

import java.util.*;

public class Processor {

    private Context ctx;
    private OpStack stack;
    
    private Map<String, CmdExecutor> executors;
    private Map<String, Subroutine> subrs, funcs;
    private Map<String, Operation> operations;

    public Processor() {
        ctx = new Context();
        stack = ctx.getStack();
        setupExecutors();
    }
    
    private void setupExecutors() {
        executors = new HashMap<>();
        executors.put("call", new SubrExecutor());
        executors.put("let", new LetExecutor());
        executors.put("eval", new EvalExecutor());
        operations = new HashMap<>();
        addOperations(new Builtins().operations());
    }
    
    public void addOperations(Map<String, Operation> ops) {
        for (Map.Entry<String, Operation> entry : ops.entrySet()) {
            for (String name : entry.getKey().split(" ")) {
                operations.put(name, entry.getValue());
            }
        }
        operations.putAll(ops);
    }
    
    public void loadProgram(Map<String, Subroutine> prog) {
        subrs = new HashMap<>();
        funcs = new HashMap<>();
        for (Map.Entry<String, Subroutine> entry : prog.entrySet()) {
            if (entry.getValue().isFunc()) {
                funcs.put(entry.getKey(), entry.getValue());
            } else {
                subrs.put(entry.getKey(), entry.getValue());
            }
        }
    }
    
    public boolean execSubroutine(String name) {
        Subroutine subr = subrs.get(name);
        if (subr == null) {
            return false;
        }
        executeSubOrFunc(subr);
        return true;
    }
    
    public boolean execFunction(String name) {
        Subroutine subr = funcs.get(name);
        if (subr == null) {
            return false;
        }
        executeSubOrFunc(subr);
        return true;
    }
    
    private void executeSubOrFunc(Subroutine subr) {
        ctx.increaseCallDepth();
        try {
            loadArguments(subr.getArgs());
            for (int i = 0; i < subr.getLength(); i++) {
                Object[] line = subr.getLine(i);
                try {
                    execFullLine(line, 0);
                } catch (RuntimeException e) {
                    System.err.println(String.format("ERROR AT %s:%d", subr.getName(), i));
                    throw e;
                }
            }
        } finally {
            ctx.decreaseCallDepth();
        }
    }
    
    private void loadArguments(String[] args) {
        for (int i = args.length - 1; i >= 0; i--) {
            ctx.assignVar(args[i], stack.pop());
        }
    }
    
    public void execFullLine(Object[] line, int offset) {
        if (line[offset].equals("if")) {
            int delim = findDelimiter(line, offset + 1);
            evalExpression(line, offset + 1, delim);
            if (stack.popInt() != 0) {
                execLine(line, delim + 1);
            }
        } else if (line[offset].equals("while")) {
            int delim = findDelimiter(line, offset + 1);
            while (true) {
                evalExpression(line, offset + 1, delim);
                if (stack.popInt() == 0) {
                    break;
                }
                execLine(line, delim + 1);
            }
        } else  {
            execLine(line, offset);
        }
    }
    
    private void execLine(Object[] line, int offset) {
        while (offset < line.length) {
            int stop = findDelimiter(line, offset);
            try {
                CmdExecutor executor = executors.get(line[offset]);
                if (executor != null) {
                    executor.exec(line, offset, stop);
                } else {
                    throw new RuntimeException("COMMAND NOT FOUND: " + line[offset]);
                }
            } catch (RuntimeException e) {
                System.err.println("ERROR OFFSET: " + offset);
                throw e;
            }
            offset = stop + 1;
        }
    }
    
    private int findDelimiter(Object[] line, int offset) {
        while (offset < line.length) {
            if (line[offset].equals(";")) {
                break;
            }
            offset++;
        }
        return offset;
    }    
    
    public void evalExpression(Object[] line, int offset, int stop) {
        for (; offset < stop; offset++) {
            Object v = line[offset];
            if (v instanceof Integer) {
                stack.push(v);
            } else if (v instanceof Literal) {
                stack.push(v.toString());
            } else {
                String s = (String) v;
                if (s.charAt(0) == '$') {
                    stack.push(ctx.fetchVar(s.substring(1)));
                } else {
                    Operation op = operations.get(s);
                    if (op != null) {
                        op.execute(ctx);
                    } else {
                        boolean found = execFunction(s);
                        if (!found) {
                            String error = "FUNC NOT FOUND: " + s;
                            System.err.println(error);
                            throw new RuntimeException(error);
                        }
                    }
                }
            }
        }
    }
    
    private static interface CmdExecutor {
        public void exec(Object[] line, int offset, int stop);
    }
    
    private class SubrExecutor implements CmdExecutor {
        public void exec(Object[] line, int offset, int stop) {
            int stackTop = stack.size();
            evalExpression(line, offset + 1, stop);
            String name = (String) stack.get(stackTop);
            boolean subrExecuted = execSubroutine(name);
            if (!subrExecuted) {
                try {
                    operations.get(name).execute(ctx);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            stack.pop();
            if (stackTop != stack.size()) {
                String error = "STACK SPOILED: " + (stack.size() - stackTop);
                System.err.println(error);
                throw new RuntimeException(error);
            }
        }
    }

    private class LetExecutor implements CmdExecutor {
        public void exec(Object[] line, int offset, int stop) {
            evalExpression(line, offset + 1, stop);
            Object val = stack.pop();
            String name = stack.popStr();
            ctx.assignVar(name, val);
        }
    }
    
    private class EvalExecutor implements CmdExecutor {
        public void exec(Object[] line, int offset, int stop) {
            evalExpression(line, offset + 1, stop);
        }
    }
    
}

