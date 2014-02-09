function Parser() {

}

Parser.prototype.parse = function(code) {
    var lines = code.split(/\n/);
    var prog = [];
    var cur = null;

    for (var i = 0; i < lines.length; i++) {
        var line = lines[i];
        if (/^\s*\#/.test(line)) {
            continue;
        }
        if (/^(?:sub|func)\s/.test(line)) {
            cur = [];
            prog[line] = cur;
        } else if (/^\s+\S+/.test(line)) {
            cur.push(this.parseLine(line));
        }
    }

    return this.makeSubroutines(prog);
}

Parser.prototype.parseLine = function(s) {
    var res = [];
    var chars = s.trim();
    var cur = 0;
    
    while (true) {
        var peek = chars.charAt(cur);
        if (peek == '"') {
            var sb = '';
            var start = cur++;
            while (true) {
                var c = chars.charAt(cur++);
                if (c == '"') {
                    break;
                } else if (c != '\\') {
                    sb += c;
                } else {
                    sb += chars.charAt(cur++);
                }
            }
            res.push({literal: sb});
        } else if (peek == "'") {
            var w = this.fetchNonSpace(chars, ++cur);
            cur += w.length;
            res.push({literal: w});
        } else {
            var ww = this.fetchNonSpace(chars, cur);
            cur += ww.length;
            if (/\-?\d+/.test(ww)) {
                res.push(parseInt(ww));
            } else {
                res.push(ww);
            }
        }
        if (cur >= chars.length) {
            break;
        }
        
        while (/\s/.test(chars.charAt(cur))) {
            cur++;
        }
    }
    
    return res;
}

Parser.prototype.fetchNonSpace = function(chars, from) {
    var to = from;
    while (to < chars.length && !/\s/.test(chars.charAt(to))) {
        to++;
    }
    return chars.substr(from, to - from);
}

Parser.prototype.makeSubroutines = function(map) {
    var prog = {};
    for (var key in map) {
        var value = map[key];
        var subName = key;
        var isFunc = /^func/.test(subName);
        subName = subName.replace(/\S+\s+/, '');
        var parts = subName.split(/\s+/);
        subName = parts[0];
        parts.shift();
        prog[subName] = {'name': subName, lines: value, args: parts, func: isFunc};
    }
    return prog;
}
