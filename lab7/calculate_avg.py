import sys

filename = sys.argv[1]
sum_times = 0
lines = 0

file = open(filename, 'r')
for i in file:
    lines += 1
    sum_times += int(i)

avg = sum_times / lines
print(str(avg))
