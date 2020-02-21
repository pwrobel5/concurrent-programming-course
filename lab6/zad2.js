function printAsync(s, cb) {
   var delay = Math.floor((Math.random()*1000)+500);
   setTimeout(function() {
       console.log(s);
       if (cb) cb();
   }, delay);
}

function inparallel(parallel_functions, final_function) {
    var functions_number = parallel_functions.length;
    var running_func_counter = 0;
    var is_something_running = false;
    for(var i = 0; i < functions_number; i++) {
        running_func_counter += 1;
        parallel_functions[i](function() { running_func_counter -= 1 });
        is_something_running = true;
    }

    var func_check = setInterval(function() {
        if(is_something_running && running_func_counter == 0) {
            clearInterval(func_check);
            final_function(null);
        }
    }, 100);    
}

A=function(cb){printAsync("A",cb);}
B=function(cb){printAsync("B",cb);}
C=function(cb){printAsync("C",cb);}
D=function(cb){printAsync("Done",cb);}

inparallel([A,B,C],D) 
