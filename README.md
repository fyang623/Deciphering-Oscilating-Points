# Deciphering Oscilating Points
An independent project for CMSC421 Intro to Artificial Intelligence at University of Maryland College Park, summer 2017

## Description

The goal of this project is to implement a search/optimization program that finds a real-valued function that fits the input 2D dataset well. For this assignment, a good fit is defined as a function f(x) that has small sum of squared error:

&nbsp;&nbsp;&nbsp;&nbsp;<img src="figs/2.png" alt="error" width="130">

We used JSON-expressions to represent expression trees. The first element of any expression is the operator, all elements after that are arguments to that operator; any argument can be the result of another function.

For example, the function:

&nbsp;&nbsp;&nbsp;&nbsp;<img src="figs/1.png" alt="function" width="180">

as a JSON-expression would be :

&nbsp;&nbsp;&nbsp;&nbsp;<img src="figs/3.png" alt="JSON-expression" width="280">

JSON-expressions support the following binary operators:
- "+" - addition
- "-" - subtraction
- "*" - multiplication

And the following unary operators:
- "e" - exponentiation (["e", "x"] is equivalent to e<sup>x</sup>
- "sin" - sine
- "cos" - cosine

Arguments can either be any real number or "x".

## Submitted files
Students provided the following executable files in the submission:
- `$ ./initial` (no arguments) prints a valid, random JSON-expression to standard output; repeatedly running `initial` produces 
expressions of varying sizes with varying arguments (random real numbers and “x”) and operators. The trees are always complete/full 
but not necessarily balanced. Tree height is no greater than 8.
- `$ ./mutate '[JSON-expression]'` replaces (and prints to standard output) some subtree of the JSON-expression with a new, random
subtree. Like with `initial`, repeatedly running `mutate` produces varying random ‘mutations’.
- `$ ./crossover '[JSON-expression]' '[JSON-expression]'` calculates the result of ‘swapping’ one random subtree of the first 
JSON-expression with a random subtree of the second JSON-expression and prints the result to standard output (one JSON-expression
per line). Like with `initial` and `mutate`, repeatedly running crossover produces varying results. Every possible crossover has
a nonzero probability of ocurring.
- `$ ./error data_file.json '[JSON-expression]'` calculates (and prints to standard output) the sum of squared error for a given
JSON-expression and data file. For example: `./error line.json '["+", "x", 1]'` would output `33.85309903600001`
- `$ ./optimize data_file.json` runs genetic programming (create initial random population, calculate fitness, select fit memebers
of the population for crossover, mutate, repeat) to find a good fitting function for the data in data_file.json. The program decides
when to stop, and prints out the best scoring JSON-expression, its sum of squared error and its fitness.

## Two Notes on the algorithm
#### 1. Dealing with "bloat"
Genetic programming often suffers from ‘bloat’, where the trees become very large over time. One way to mitigate this problem is to 
modify the fitness function to include a ‘penalty’ for large trees. According to Occam's razor, simpler solutions are more likely 
to be correct than complex ones.

#### 2. Reproductive competition
Frequently a genetic programming application progesses no more while the room for improvement is still plenty after some generations 
of evolution. One cause for this issue is that we keep losing good functions as they mutate or crossover with other functions, because 
mutation/crossover don't necessarily result in a better generation. 

There are two methods to deal with this problem. One way is to give the good functions less chance to mutate and more chance to 
reproduce (crossover), the other is to let the best ones live longer than one generation (i.e., mix them into the children 
after they finish crossovering). In my implementation the two methods were used in combination which led to very good results. For 
sets of 20 points selected from 2D parabolas, my program can often achieve sum of squared errors smaller than 1e<sup>-4</sup>.

## Author
[Fan Yang](mailto:fyang3@umd.edu) 
