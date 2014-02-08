function Processor() {
    this.ctx = new Context();
    this.stack = this.ctx.stack;

    this.executors = null;
    this.subrs = null;
    this.funcs = null;
    this.operations = null;

    this.setupExecutors();
}

Processor.prototype.setupExecutors = function() {
    this.executors = [];
    this.executors['call'] = new Processor.prototype.subrExecutor(this);
    this.executors['let'] = new Processor.prototype.letExecutor(this);
    this.executors['eval'] = new Processor.prototype.evalExecutor(this);
    this.operations = [];
    this.addOperations(new Builtins().operations());
}

Processor.prototype.addOperations = function(ops) {
    for (var key in ops) {
        var names = key.split(' ');
        for (var i in names) {
            this.operations[names[i]] = ops[key];
        }
    }
}

Processor.prototype.loadProgram = function(prog) {
    this.subrs = [];
    this.funcs = [];
    for (var key in prog) {
        var value = prog[key];
        if (value.func) {
            this.funcs[key] = value;
        } else {
            this.subrs[key] = value;
        }
    }
}

Processor.prototype.execSubroutine = function(name) {
    var subr = this.subrs[name];
    if (typeof(subr) == 'undefined') {
        return false;
    }
    this.executeSubOrFunc(subr);
    return true;
}

Processor.prototype.execFunction = function(name) {
    var subr = this.funcs[name];
    if (subr == undefined) {
        return false;
    }
    this.executeSubOrFunc(subr);
    return true;
}

Processor.prototype.executeSubOrFunc = function(subr) {
    this.ctx.increaseCallDepth();
    try {
        this.loadArguments(subr.args);
        for (var i = 0; i < subr.lines.length; i++) {
            var line = subr.lines[i];
            try {
                this.execFullLine(line, 0);
            } catch (e) {
                alert("ERROR AT " + subr.name + ":" + i + "\n" + e);
                throw e;
            }
        }
    } finally {
        this.ctx.decreaseCallDepth();
    }
}

Processor.prototype.loadArguments = function(args) {
    for (var i = args.length - 1; i >= 0; i--) {
        this.ctx.assignVar(args[i], this.stack.pop());
    }
}

Processor.prototype.execFullLine = function(line, offset) {
    var delim;
    if (line[offset] == 'if') {
        delim = this.findDelimiter(line, offset + 1);
        this.evalExpression(line, offset + 1, delim);
        if (this.stack.pop() !== 0) {
            this.execLine(line, delim + 1);
        }
    } else if (line[offset] == 'while') {
        delim = this.findDelimiter(line, offset + 1);
        while (true) {
            this.evalExpression(line, offset + 1, delim);
            if (this.stack.pop() === 0) {
                break;
            }
            this.execLine(line, delim + 1);
        }
    } else {
        this.execLine(line, offset);
    }
}

Processor.prototype.execLine = function(line, offset) {
    while (offset < line.length) {
        var stop = this.findDelimiter(line, offset)
        try {
            var executor = this.executors[line[offset]];
            if (typeof(executor) != 'undefined') {
                executor.exec(line, offset, stop);
            } else {
                throw "COMMAND NOT FOUND: " + line[offset];
            }
        } catch (e) {
            throw e + "\nERROR OFFSET: " + offset;
        }
        offset = stop + 1;
    }
}

Processor.prototype.findDelimiter = function(line, offset) {
    while (offset < line.length) {
        if (line[offset] == ';') {
            break;
        }
        offset++;
    }
    return offset;
}

Processor.prototype.evalExpression = function(line, offset, stop) {
    for (; offset < stop; offset++) {
        var v = line[offset];
        if (typeof(v) == 'number') {
            this.stack.push(v);
        } else if (typeof(v) == 'object' && v['literal'] !== 'undefined') {
            this.stack.push(v.literal);
        } else {
            if (v.charAt(0) == '$') {
                this.stack.push(this.ctx.fetchVar(v.substring(1)));
            } else {
                var op = this.operations[v];
                if (typeof(op) != 'undefined') {
                    op.execute(this.ctx);
                } else {
                    var found = this.execFunction(v);
                    if (!found) {
                        throw "FUNC NOT FOUND: " + v;
                    }
                }
            }
        }
    }
}

Processor.prototype.subrExecutor = function(proc) {
    var processor = proc;
    this.exec = function(line, offset, stop) {
        var stack = processor.stack;
        var stackTop = stack.length;
        processor.evalExpression(line, offset + 1, stop);
        var name = stack[stackTop];
        var subrExecuted = processor.execSubroutine(name);
        if (!subrExecuted) {
            processor.operations[name].execute(processor.ctx);
        }
        stack.pop();
        if (stackTop != stack.length) {
            throw "STACK SPOILED: " + (stack.length - stackTop);
        }
    }
}

Processor.prototype.letExecutor = function(proc) {
    var processor = proc;
    this.exec = function(line, offset, stop) {
        processor.evalExpression(line, offset + 1, stop);
        var val = processor.stack.pop();
        var name = processor.stack.pop();
        processor.ctx.assignVar(name, val);
    }
}

Processor.prototype.evalExecutor = function(proc) {
    var processor = proc;
    this.exec = function(line, offset, stop) {
        processor.evalExpression(line, offset + 1, stop);
    }
}
