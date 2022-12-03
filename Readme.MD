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

Realised by Aldric VITALI SILVESTRE and Nicolas CIBULKA

# Table of contents

- [Table of contents](#table-of-contents)
- [How to setup](#how-to-setup)
  - [Using javac](#using-javac)
  - [Using scripts](#using-scripts)
- [Basic usage](#basic-usage)
  - [GUI-based-usage](#gui-based-usage)
  - [CLI-based usage](#cli-based-usage)
  - [Agents](#agents)
- [Example scenarios](#example-scenarios)
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

TODO parler des agents, params nécéssaires etc.


# Example scenarios

You will find there an example for each way of launching the system. The example we will present here is to compute the 
integral of the function 1/X between 1 and 10 with a delta of 0.0001 with 5 compute agents.

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

**Arguments :** 5

Once it's done, we will have all the computeAgents created.

We will now have to create the TestParallelAgent to compute the integral

Create a new agent with this parameters

**Agent name :** testParallelAgent

**Class Name :** fr.cibultali.TestParallelAgent

**Arguments :** 1,10,0.0001,1/X

You will then have the resut and the elapsed time for the execution in local and with the computeAgents displayed in the terminal.

## CLI scenario example

To execute the example with the CLI command, we will have to use this command after compiling :

```shell
./run_cli.sh [/path/to/jade.jar] -agents 'creator:fr.cibultali.ComputeCreatorAgent(10);tpa:fr.cibultali.TestParallelAgent(1, 10000, 0.000001)'
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
    