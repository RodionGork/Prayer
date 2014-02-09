function Builtins() {
}

Builtins.prototype.operations = function() {
    var res = [];
    var buf = '';

    res['print'] = {execute: function(ctx) {
        buf += ctx.stack.pop();
    }};

    res['println'] = {execute: function(ctx) {
        buf += ctx.stack.pop();
        alert(buf);
        buf = '';
    }};

    res['+ add'] = {execute: function(ctx) {
        var stack = ctx.stack;
        var b = stack.pop();
        stack.push(stack.pop() + b);
    }};

    res['- sub'] = {execute: function(ctx) {
        var stack = ctx.stack;
        var b = stack.pop();
        stack.push(stack.pop() - b);
    }};

    res['* mul'] = {execute: function(ctx) {
        var stack = ctx.stack;
        stack.push(stack.pop() * stack.pop());
    }};

    res['/ div'] = {execute: function(ctx) {
        var stack = ctx.stack;
        var b = stack.pop();
        stack.push(Math.floor(stack.pop() / b));
    }};

    res['% mod'] = {execute: function(ctx) {
        var stack = ctx.stack;
        var b = stack.pop();
        stack.push(stack.pop() % b);
    }};
    
    res['neg'] = {execute: function(ctx) {
        var stack = ctx.stack;
        stack.push(- stack.pop());
    }};
    
    res['= eq'] = {execute: function(ctx) {
        var stack = ctx.stack;
        stack.push(stack.pop() === stack.pop() ? 1 : 0);
    }};
    
    res['!= <> ne'] = {execute: function(ctx) {
        var stack = ctx.stack;
        stack.push(stack.pop() === stack.pop() ? 0 : 1);
    }};
    
    res['! not'] = {execute: function(ctx) {
        var stack = ctx.stack;
        stack.push(!stack.pop());
    }};
    
    res['&& and'] = {execute: function(ctx) {
        var stack = ctx.stack;
        var a = stack.pop() != 0;
        var b = stack.pop() != 0;
        stack.push(a && b ? 1 : 0);
    }};
    
    res['|| or'] = {execute: function(ctx) {
        var stack = ctx.stack;
        var a = stack.pop() != 0;
        var b = stack.pop() != 0;
        stack.push(a || b ? 1 : 0);
    }};

    res['< lt'] = {execute: function(ctx) {
        var stack = ctx.stack;
        var b = stack.pop()
        stack.push(stack.pop() < b ? 1 : 0);
    }};
    
    res['<= le'] = {execute: function(ctx) {
        var stack = ctx.stack;
        var b = stack.pop()
        stack.push(stack.pop() <= b ? 1 : 0);
    }};
    
    res['> gt'] = {execute: function(ctx) {
        var stack = ctx.stack;
        var b = stack.pop()
        stack.push(stack.pop() > b ? 1 : 0);
    }};
    
    res['>= => ge'] = {execute: function(ctx) {
        var stack = ctx.stack;
        var b = stack.pop()
        stack.push(stack.pop() >= b ? 1 : 0);
    }};
    
    res['itoa'] = {execute: function(ctx) {
        var stack = ctx.stack;
        stack.push('' + stack.pop());
    }};
    
    res['atoi'] = {execute: function(ctx) {
        var stack = ctx.stack;
        stack.push(parseInt(stack.pop()));
    }};
    
    res['?int isint'] = {execute: function(ctx) {
        var stack = ctx.stack;
        stack.push(typeof(stack.pop()) === 'number' ? 1 : 0);
    }};
    
    res['?str isstr'] = {execute: function(ctx) {
        var stack = ctx.stack;
        stack.push(typeof(stack.pop()) === 'string' ? 1 : 0);
    }};
    
    res['drop'] = {execute: function(ctx) {
        ctx.stack.pop();
    }};
    
    res['dup'] = {execute: function(ctx) {
        var stack = ctx.stack;
        stack.push(stack[stack.length - 1]);
    }};

    res['over'] = {execute: function(ctx) {
        var stack = ctx.stack;
        stack.push(stack[stack.length - 2]);
    }};

    res['swap'] = {execute: function(ctx) {
        var stack = ctx.stack;
        var b = stack.pop(), a = stack.pop();
        stack.push(b, a);
    }};

    res['rot'] = {execute: function(ctx) {
        var stack = ctx.stack;
        var c = stack.pop(), b = stack.pop(), a = stack.pop();
        stack.push(b, c, a);
    }};
    
    res['[] peek'] = {execute: function(ctx) {
        var stack = ctx.stack;
        var i = stack.pop(), a = stack.pop();
        stack.push(ctx.fetchVar(a + '' + i));
    }};

    res['#stacksize'] = {execute: function(ctx) {
        alert('STACK SIZE: ' + ctx.stack.length);
    }};
    
    res['#stackdata'] = {execute: function(ctx) {
        var stack = ctx.stack;
        var res = 'STACK DATA:';
        for (var i = 0; i < stack.length; i++) {
            res += '\n' + stack[i];
        }
        alert(res);
    }};
    
    return res;
}
