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

Statistics and Algorithm Differentiation:
Trace-file	Algorithm	Frame Number	Page Faults
gcc.trace	FIFO	8	29011
gcc.trace	FIFO	16	8568
gcc.trace	FIFO	32	1375
gcc.trace	FIFO	64	551
gcc.trace	OPT	8	13364
gcc.trace	OPT	16	3582
gcc.trace	OPT	32	565
gcc.trace	OPT	64	339
gcc.trace	LRU	8	22693
gcc.trace	LRU	16	6075
gcc.trace	LRU	32	736
gcc.trace	LRU	64	409
gzip.trace	FIFO	8	44918
gzip.trace	FIFO	16	42384
gzip.trace	FIFO	32	41120
gzip.trace	FIFO	64	40496
gzip.trace	OPT	8	39882
gzip.trace	OPT	16	39858
gzip.trace	OPT	32	
gzip.trace	OPT	64	
gzip.trace	LRU	8	39906
gzip.trace	LRU	16	39883
gzip.trace	LRU	32	39874
gzip.trace	LRU	64	39874
swim.trace	FIFO	8	13893
swim.trace	FIFO	16	844
swim.trace	FIFO	32	326
swim.trace	FIFO	64	177
swim.trace	OPT	8	4430
swim.trace	OPT	16	395
swim.trace	OPT	32	185
swim.trace	OPT	64	135
swim.trace	LRU	8	9906
swim.trace	LRU	16	531
swim.trace	LRU	32	205
swim.trace	LRU	64	140

Obviously the OPT algorithm is the best, but unfortunately it is impossible to know all the information that we were given.  
A good second choice is the least recently used algorithm with aging pages.  This algorithm’s performance is not quite as good
as the optimal algorithm, but it is a good alternative.  Far better performance than a first in first out algorithm.
