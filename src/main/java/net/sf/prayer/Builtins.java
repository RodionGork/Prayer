package net.sf.prayer;

import java.util.*;

public class Builtins {
    
    public Map<String, Operation> operations() {
        Map<String, Operation> res = new HashMap<>();
        
        res.put("print", new Operation() {
            public void execute(Context ctx) {
                System.out.print(ctx.getStack().pop());
            }
        });
        
        res.put("println", new Operation() {
            public void execute(Context ctx) {
                System.out.println(ctx.getStack().pop());
            }
        });
        
        res.put("+ add", new Operation() {
            public void execute(Context ctx) {
                OpStack stack = ctx.getStack();
                Object b = stack.pop();
                if (stack.peek() instanceof Integer && b instanceof Integer) {
                    stack.push(stack.popInt() + (Integer) b);
                } else {
                    stack.push(String.valueOf(stack.pop()) + String.valueOf(b));
                }
            }
        });
        
        res.put("- sub", new Operation() {
            public void execute(Context ctx) {
                OpStack stack = ctx.getStack();
                Integer b = stack.popInt();
                stack.push(stack.popInt() - b);
            }
        });
        
        res.put("* mul", new Operation() {
            public void execute(Context ctx) {
                OpStack stack = ctx.getStack();
                stack.push(stack.popInt() * stack.popInt());
            }
        });
        
        res.put("/ div", new Operation() {
            public void execute(Context ctx) {
                OpStack stack = ctx.getStack();
                Integer b = stack.popInt();
                stack.push(stack.popInt() / b);
            }
        });
        
        res.put("% mod", new Operation() {
            public void execute(Context ctx) {
                OpStack stack = ctx.getStack();
                Integer b = stack.popInt();
                stack.push(stack.popInt() % b);
            }
        });
        
        res.put("neg", new Operation() {
            public void execute(Context ctx) {
                OpStack stack = ctx.getStack();
                stack.push(- stack.popInt());
            }
        });
        
        res.put("= eq", new Operation() {
            public void execute(Context ctx) {
                OpStack stack = ctx.getStack();
                Object b = stack.pop();
                stack.push(b != null && b.equals(stack.pop()) ? 1 : 0);
            }
        });
        
        res.put("!= <> ne", new Operation() {
            public void execute(Context ctx) {
                OpStack stack = ctx.getStack();
                Object b = stack.pop();
                stack.push(b != null && !b.equals(stack.pop()) ? 1 : 0);
            }
        });
        
        res.put("! not", new Operation() {
            public void execute(Context ctx) {
                OpStack stack = ctx.getStack();
                stack.push(stack.popInt() == 0 ? 1 : 0);
            }
        });
        
        res.put("&& and", new Operation() {
            public void execute(Context ctx) {
                OpStack stack = ctx.getStack();
                boolean a = stack.popInt() != 0;
                boolean b = stack.popInt() != 0;
                stack.push(a && b ? 1 : 0);
            }
        });
        
        res.put("|| or", new Operation() {
            public void execute(Context ctx) {
                OpStack stack = ctx.getStack();
                boolean a = stack.popInt() != 0;
                boolean b = stack.popInt() != 0;
                stack.push(a || b ? 1 : 0);
            }
        });
        
        res.put("lt <", new Operation() {
            public void execute(Context ctx) {
                OpStack stack = ctx.getStack();
                Integer b = stack.popInt();
                stack.push(stack.popInt() < b ? 1 : 0);
            }
        });
        
        res.put("le <=", new Operation() {
            public void execute(Context ctx) {
                OpStack stack = ctx.getStack();
                Integer b = stack.popInt();
                stack.push(stack.popInt() <= b ? 1 : 0);
            }
        });
        
        res.put("gt >", new Operation() {
            public void execute(Context ctx) {
                OpStack stack = ctx.getStack();
                Integer b = stack.popInt();
                stack.push(stack.popInt() > b ? 1 : 0);
            }
        });
        
        res.put("ge >= =>", new Operation() {
            public void execute(Context ctx) {
                OpStack stack = ctx.getStack();
                Integer b = stack.popInt();
                stack.push(stack.popInt() >= b ? 1 : 0);
            }
        });
        
        res.put("itoa", new Operation() {
            public void execute(Context ctx) {
                OpStack stack = ctx.getStack();
                stack.push(Integer.toString(stack.popInt()));
            }
        });
        
        res.put("atoi", new Operation() {
            public void execute(Context ctx) {
                OpStack stack = ctx.getStack();
                stack.push(Integer.parseInt(stack.popStr()));
            }
        });
        
        res.put("isint ?int", new Operation() {
            public void execute(Context ctx) {
                OpStack stack = ctx.getStack();
                stack.push(stack.pop() instanceof Integer ? 1 : 0);
            }
        });
        
        res.put("isstr ?str", new Operation() {
            public void execute(Context ctx) {
                OpStack stack = ctx.getStack();
                stack.push(stack.pop() instanceof String ? 1 : 0);
            }
        });
        
        res.put("drop", new Operation() {
            public void execute(Context ctx) {
                ctx.getStack().pop();
            }
        });
        
        res.put("dup", new Operation() {
            public void execute(Context ctx) {
                OpStack stack = ctx.getStack();
                stack.push(stack.peek());
            }
        });
        
        res.put("over", new Operation() {
            public void execute(Context ctx) {
                OpStack stack = ctx.getStack();
                stack.push(stack.get(stack.size() - 2));
            }
        });
        
        res.put("swap", new Operation() {
            public void execute(Context ctx) {
                OpStack stack = ctx.getStack();
                Object b = stack.pop(), a = stack.pop();
                stack.pushAll(b, a);
            }
        });
        
        res.put("rot", new Operation() {
            public void execute(Context ctx) {
                OpStack stack = ctx.getStack();
                Object c = stack.pop(), b = stack.pop(), a = stack.pop();
                stack.pushAll(b, c, a);
            }
        });
        
        res.put("[] peek", new Operation() {
            public void execute(Context ctx) {
                OpStack stack = ctx.getStack();
                String idx = stack.pop().toString();
                stack.push(ctx.fetchVar(stack.pop().toString() + idx));
            }
        });
        
        res.put("#stacksize", new Operation() {
            public void execute(Context ctx) {
                System.err.println("STACK SIZE: " + ctx.getStack().size());
            }
        });
        
        res.put("#stackdata", new Operation() {
            public void execute(Context ctx) {
                System.err.println("STACK DATA: " + ctx.getStack());
            }
        });
        
        return res;
    }
    
}
