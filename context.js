function Context() {
    this.stack = [];
    this.globals = [];
    this.locals = [];
    this.curLocals = null;
    this.callDepth = 0;
}

Context.prototype.increaseCallDepth = function() {
    while (this.locals.length <= this.callDepth) {
        this.locals.push([]);
    }
    this.curLocals = this.locals[this.callDepth];
    this.callDepth++;
}

Context.prototype.decreaseCallDepth = function() {
    this.callDepth--;
    this.curLocals = (this.callDepth > 0) ? this.locals[this.callDepth - 1] : null;
}

Context.prototype.assignVar = function(name, v) {
    if (name.length > 1) {
        this.globals[name] = v;
    } else {
        this.curLocals[name] = v;
    }
}

Context.prototype.fetchVar = function(name) {
    if (name.length > 1) {
        return this.globals[name];
    } else {
        return this.curLocals[name];
    }
}
