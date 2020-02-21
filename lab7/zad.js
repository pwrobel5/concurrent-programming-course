var Fork = function(id) {
    this.state = 0;
    this.id = id;
    return this;
}

Fork.prototype.acquire = function(philosopher_id, cb, prev_waiting_time) { 
    var increasingInterval = function(cb, currentWaitingTime, fork, philosopher_id) {
        setTimeout(function() {
            if(fork.state == 0) {
                fork.state = 1;
                console.log("Fork " + fork.id + " taken by philosopher " + philosopher_id);
                if(cb) cb(currentWaitingTime + prev_waiting_time);
            }
            else {
                currentWaitingTime = currentWaitingTime * 2
                console.log("Philosopher " + philosopher_id + " is going to wait " + currentWaitingTime + " ms for fork " + fork.id);
                increasingInterval(cb, currentWaitingTime, fork, philosopher_id);
            }
        }, currentWaitingTime);
    };

    increasingInterval(cb, 1, this, philosopher_id);
}

Fork.prototype.release = function() { 
    this.state = 0;
}

var Philosopher = function(id, forks, conductor) {
    this.id = id;
    this.forks = forks;
    this.f1 = id % forks.length;
    this.f2 = (id+1) % forks.length;
    this.conductor = conductor;
    return this;
}

Philosopher.prototype.startNaive = function(count) {
    var forks = this.forks,
        f1 = this.f1,
        f2 = this.f2,
        id = this.id;

    var single_task = function (n) {
        forks[f1].acquire(id, function() {
            forks[f2].acquire(id, function() {
                console.log("Philosopher " + id + " is eating");
                var eating_time = Math.random() * 10;
                setTimeout(function () {
                    forks[f1].release();
                    forks[f2].release();
                    console.log("Philosopher " + id + " ended eating");
                    if(n == 1) {                    
                        console.log("Philosopher " + id + " is going out from the table");
                    } else {
                        single_task(n - 1);
                    }
                }, eating_time);                    
            })
        });
    };

    single_task(count);
}

Philosopher.prototype.startAsym = function(output_name, count) {
    var forks = this.forks,
        id = this.id,
        firstFork = (id % 2 == 0) ? this.f2 : this.f1,
        secondFork = (id % 2 == 0) ? this.f1 : this.f2;        
    const fs = require('fs');
    
    var single_task = function (n) {
        forks[firstFork].acquire(id, function(waiting_time) {
            forks[secondFork].acquire(id, function(second_waiting_time) {
                fs.appendFile(output_name, second_waiting_time + "\n", function() {  } );

                console.log("Philosopher " + id + " is eating");
                var eating_time = Math.random() * 10;
                setTimeout(function () {
                    forks[firstFork].release();
                    forks[secondFork].release();
                    console.log("Philosopher " + id + " ended eating");
                    if(n == 1) {                    
                        console.log("Philosopher " + id + " is going out from the table");
                    } else {
                        single_task(n - 1);
                    }
                }, eating_time);                    
            }, waiting_time);
        }, 0);
    };

    single_task(count);
}

Philosopher.prototype.startConductor = function(output_name, count) {
    var forks = this.forks,
        f1 = this.f1,
        f2 = this.f2,
        id = this.id,
        conductor = this.conductor;
    const fs = require('fs');
    
    var single_task = function (n) {
        conductor.askForPlace(id, function(cond_waiting_time) {
            forks[f1].acquire(id, function(first_fork_waiting_time) {
                forks[f2].acquire(id, function(all_waiting_time) {
                    fs.appendFile(output_name, all_waiting_time + "\n", function() {  } );

                    console.log("Philosopher " + id + " is eating");
                    var eating_time = Math.random() * 10;
                    setTimeout(function () {
                        forks[f1].release();
                        forks[f2].release();
                        console.log("Philosopher " + id + " ended eating");  
                        conductor.leaveTable(id);
                        
                        if(n == 1) {                    
                            console.log("Philosopher " + id + " is going home");
                        } else {
                            single_task(n - 1);
                        }                      
                    }, eating_time);                    
                }, first_fork_waiting_time);
            }, cond_waiting_time);
        }, 0);        
    };

    single_task(count);
}

var Conductor = function(philosophers_number) {
    this.max_sitting = philosophers_number - 1;
    this.state = 0;
    return this;
}

Conductor.prototype.askForPlace = function(philosopher_id, cb, prev_waiting_time) {
    var increasingInterval = function(cb, currentWaitingTime, conductor, philosopher_id) {
        setTimeout(function() {
            if(conductor.state < conductor.max_sitting) {
                conductor.state += 1;
                console.log("Philosopher " + philosopher_id + " is now allowed to seat");
                if(cb) cb(currentWaitingTime + prev_waiting_time);
            }
            else {
                currentWaitingTime = currentWaitingTime * 2
                console.log("Philosopher " + philosopher_id + " is going to wait " + currentWaitingTime + " ms for place at the table");
                increasingInterval(cb, currentWaitingTime, conductor, philosopher_id);
            }
        }, currentWaitingTime);
    };

    increasingInterval(cb, 1, this, philosopher_id);
}

Conductor.prototype.leaveTable = function(philosopher_id) {
    this.state -= 1;
    console.log("Philosopher " + philosopher_id + " left the table");
}

var N = 20;
var forks = [];
var philosophers = [];
var conductor = new Conductor(N);
for (var i = 0; i < N; i++) {
    forks.push(new Fork(i));
}

for (var i = 0; i < N; i++) {
    philosophers.push(new Philosopher(i, forks, conductor));
}

for (var i = 0; i < N; i++) {
    //philosophers[i].startNaive(10);
    //philosophers[i].startAsym("asym_" + N, 10);
    philosophers[i].startConductor("cond_" + N, 10);
}
