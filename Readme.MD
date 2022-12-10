```
    _____ __  __                      _____                            _        _   _             
  / ____|  \/  |   /\               / ____|                          | |      | | (_)            
 | (___ | \  / |  /  \     ______  | |     ___  _ __ ___  _ __  _   _| |_ __ _| |_ _  ___  _ __  
  \___ \| |\/| | / /\ \   |______| | |    / _ \| '_ ` _ \| '_ \| | | | __/ _` | __| |/ _ \| '_ \ 
  ____) | |  | |/ ____ \           | |___| (_) | | | | | | |_) | |_| | || (_| | |_| | (_) | | | |
 |_____/|_|  |_/_/    \_\           \_____\___/|_| |_| |_| .__/ \__,_|\__\__,_|\__|_|\___/|_| |_|
                                                         | |                                     
                                                         |_|                                     
 ```

Realized by Aldric VITALI SILVESTRE and Nicolas CIBULKA

# Table of contents

- [Table of contents](#table-of-contents)
- [How to setup](#how-to-setup)
  - [Using javac](#using-javac)
  - [Using scripts](#using-scripts)
- [Basic usage](#basic-usage)
  - [GUI-based-usage](#gui-based-usage)
  - [CLI-based usage](#cli-based-usage)
  - [Agents](#agents)
    - [ComputeAgent](#computeagent)
    - [ComputeCreatorAgent](#computecreatoragent)
    - [TestParallelAgent](#testparallelagent)
  - [Functions](#functions)
- [Example scenarios](#example-scenarios)
  - [GUI scenario example](#gui-scenario-example)
  - [CLI scenario example](#cli-scenario-example)
- [Work repartition](#work-repartition)

# How to setup

You must have the `jade.jar` file: it can be downloaded [here](https://jade.tilab.com/maven/com/tilab/jade/jade/4.5.0/jade-4.5.0.jar).

There is multiple methods for compiling and launching the application.

## Using javac

First of all, javac can be natively used. For doing that, you have to add the path of the Jade jar
to the `CLASSPATH` environment variable. You can then compile the necessary files :
- ComputeAgent.java
- ComputeCreatorAgent.java
- TestParallelAgent.java


## Using scripts

As this first method is a bit tedious, multiple shell scripts are created in order 
to facilitate compilation and execution.

The script `compile.sh` will automatically compile all classes, and put the result in a 
"out" folder aside this script. The script accepts one optional parameter : the path of the 
jade jar archive.

If you already have the jar in the classpath, you simply have to type :

```shell
./compile.sh
```

Otherwise, add the path of the jar like this :

```shell
./compile.sh /path/to/jade.jar
```

# Basic usage

It is possible to simply call the `java jade.Boot ...` command if the CLASSPATH is set,
and while on the folder where classes were generated (see the previous section).

However, if you have used the `compile.sh` script to compile, you can use other schell scripts
for launching Jade :

## GUI-based-usage

The `run_gui.sh` script can be used to launch the GUI of Jade with all agents loaded.

As well as `compile.sh`, the path to the Jade jar can be passed as argument in order to link it.

```shell
./run_gui.sh [/path/to/jade.jar]
```

## CLI-based usage

The `run_cli.sh` script can be used to launch the GUI of Jade with all agents loaded.

The script accepts multiple arguments :
- The path to jade jar : if not needed, you can simply set this argument to `.`
- The remaining arguments will be passed to the `jade.Boot` program.

For example:

```shell
./run_cli.sh /path/to/jade.jar -agents '...'
```

## Agents

There are 3 main agents in this application :

### ComputeAgent

The ComputeAgent subscribe to the service `COMPUTE` at setup and wait indefinitely for compute requests (this agent does not wait any parameter).

A compute request is a simple message with the following format:
```shell
[FUNCTION_NAME],[LOWER_BOUND],[UPPER_BOUND],[DELTA]
```

After receiving the message, the Compute agent will compute the integral of the wanted function 
and send the result to the requester: the response message's content will only be the value of the integral found.

### ComputeCreatorAgent

As we can create multiple ComputeAgent by hand, it became a bit tedious to do so when we want dozens of them.

This agent then allows the user to easily create multiple ComputeAgent with one command.

Usage is fairly simple: create the ComputeCreatorAgent while passing as agent parameter the number of ComputeAgent wanted, and the wanted number (or 5 if not number is provided) of ComputeAgent will be created.

In order to be recognizable, the created agents will share a common generation id (a random string of 5 characters) in their names.

Upon terminating, the agent removes itself from the container, as it does not have any more utility.

### TestParallelAgent

This is the main agent that will launch all computations.

This agent can take up to 4 parameters:
  - the lower bound of the function to compute (default: 1.0)
  - the upper bound of the function to compute (default: min + 1)
  - the delta parameter (default: 0.001)
  - the name of the function to test (default: "1/X")

As the parameters are positional, it is not possible to change the function name while keeping default 
arguments for the other parameters.

After creating the function, this agent will then search for all ComputeAgent (see [above](#computeagent)) that are
registered to the `COMPUTE` service. With `N` agents found, the TestParallelAgent will then split the function in `N` equal parts. The computation can now begin.

First, the agent compute locally the integral of each function part and display the sum of the results, along with
the time taken to do so. Then, it sends one compute request message to each ComputeAgent found with a different part of the function to compute. It simply waits for each response to come in order to display the sum of each response as the "distributed" result.

## Functions 

The base class is `fr.cibultali.Function`, an abstract class providing a method for computing the integral using [approximation methods](https://en.wikipedia.org/wiki/Numerical_integration#Quadrature_rules_based_on_interpolating_functions).

Each new function wanted must inherit this base class and implement the `f` method.

In order to be used by agents the function must also be registered in the `FunctionFactory`: this factory simply take the name of the function (as string), along with the bounds and delta wanted and returns the 
corresponding function.

At the moment there are 3 functions registered:
  - `1/X` : The reverse function
  - `X^2` : The square function
  - `X^1/2` : The square root function

# Example scenarios

You will find there an example for each way of launching the system. The example we will present here is to compute the 
integral of the function 1/X between 1 and 10 with a delta of 0.0001 with 10 compute agents.

## GUI scenario example

First we need to launch the program in GUI version after executing the compilation script.

```shell
./run_gui.sh [/path/to/jade.jar]
```
We write the path to jade.jar if we did not add in the classpath previously.

Then we can reach to the main container and add a new agent.

First we will create an agent that will generate all the computeAgents we want. For this, we will write 
this in the opened window :

**Agent name :** agentCreation

**Class Name :** fr.cibultali.ComputeCreatorAgent

**Arguments :** 10

Once it's done, we will have all the computeAgents created.

We will now have to create the TestParallelAgent to compute the integral

Create a new agent with this parameters

**Agent name :** testParallelAgent

**Class Name :** fr.cibultali.TestParallelAgent

**Arguments :** 1,10,0.0001,1/X

You will then have the result and the elapsed time for the execution in local and with the ComputeAgents displayed in the terminal.

## CLI scenario example

To execute the example with the CLI command, we will have to use this command after compiling :

```shell
./run_cli.sh [/path/to/jade.jar] -agents 'creator:fr.cibultali.ComputeCreatorAgent(10);tpa:fr.cibultali.TestParallelAgent(1, 10, 0.000001)'
```
We write the path to jade.jar if we did not add in the classpath previously.


# Work repartition

The work has been distributed like this :

| Part of the project                          | Nicolas            | Aldric             |
|----------------------------------------------|--------------------|--------------------|
| Creation and intialisation of the repository |                    | :heavy_check_mark: |
| Function classes                             | :heavy_check_mark: |                    | 
| Compute Agents                               | :heavy_check_mark: |                    | 
| ComputeCreatorAgent                          |                    | :heavy_check_mark: | 
| 'Master' Agent                               |                    | :heavy_check_mark: | 
| README                                       | :heavy_check_mark: |                    | 
| shell scripts                                |                    | :heavy_check_mark: | 
    