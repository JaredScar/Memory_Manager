# Memory_Manager
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://github.com/TheWolfBadger/Memory_Manager/blob/master/LICENSE)

Memory_Manager was a program designed to show how the memory is stored for jobs using the different memory fit algorithms. The program was made for Sister Jane's COM 310 Operating Systems assignment #2.
# Assumptions we made for this project:
- There is a max of 9 processes.
- There is a limit to the minimum size of a process based on how much total memory entered(A message will pop up if the entered number is too small).
- Each process can only be in memory once at a time.

# Memory Fit Algorithms Included:
## First Fit 
Every process is added to the first hole it can fit in. If it can not fit in any hole, it will not be added.
## Best Fit 
Every process is added into the smallest hole it can fit in. If it can not fit in any hole, it will not be added.
## Worst Fit 
Every process is added into the largest hole it. If it can not fit in any hole, it will not be added.
