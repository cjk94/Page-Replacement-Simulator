# Page-Replacement-Simulator
CS1550 Project 3
The goal of this project was to compare the results of several different algorithms on traces of memory references. While simulating an 
algorithm, statistics are collected about its performance such as the number of page faults that occur and the number of dirty frames
that had to be written back to disk.

The three algorithms for this project are:
Opt – Simulate what the optimal page replacement algorithm would choose if it had perfect knowledge
FIFO – Implement first-in, first-out
Aging – Implement the aging algorithm
The page table is for a 32-bit address space. All pages are be 4KB in size. The number of frames will be a parameter to the execution 
of the program.

To run from the command-line choose one of these options:
java vmsim –n <numframes> -a opt <tracefile>
java vmsim –n <numframes> -a fifo <tracefile>
java vmsim –n <numframes> -a aging -r <refresh> <tracefile>

Aging Refresh Parameter: After simulating all trace-files on different reset numbers, and analyzing the data, a good choice for
standard refresh is ~8.  A refresh rate of 8 consistently produced good results compared to other options.  The trend as refresh
is increased is that page Faults increase, but there is some improvement gained by increasing refresh from very small numbers up to 
around 8.


Obviously the OPT algorithm is the best, but unfortunately it is impossible to know all the information that we were given.  
A good second choice is the least recently used algorithm with aging pages.  This algorithm’s performance is not quite as good
as the optimal algorithm, but it is a good alternative.  Far better performance than a first in first out algorithm.
