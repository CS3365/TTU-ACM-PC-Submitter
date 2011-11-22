Programming Competition Grader
==============================

This project is designed by members of the Texas Tech University ACM
organization and is released under the standard MIT license. The original
authors are:

Mike Kent<br>
Austin Ray<br>
Louis Alridge<br>
Kevin Thomas<br>
Rafael Jurado

Usage
-----

This program is desigend to have several parts: a **server** where all
solutions are submitted and graded and where all team's points are updated,
one or more **admin clients** on seperate computers that are used to
administrate the **server**, and many **team clients** where each team or
individual submits their solutions.

Server Settings
---------------

Unfortunatley, it is impossible to run this program without any settings,
but we have attempted to make as many of the settings optional as possible.
That being said, the full directory structure is thus:

    Server Data
    |- Problems
    |  |- 0
    |  |  |- input.txt
    |  |  |- input2.txt
    |  |  |- input3.txt
    |  |  |- output.txt
    |  |  |- output2.txt
    |  |  |- output3.txt
    |  |  |- settings.txt
    |  |  |- statement.txt
    |  |  |- sampleInput.txt
    |  |  \- sampleOutput.txt
    |  |- 1
    |  \- 2
    |- Languages
    |  |- Java.txt
    |  \- Python.txt
    \- settings.txt

The *settings.txt* file is of the format \[setting\]: \[value\] and may
contain the following settings (the default values are listed):

    ServerPort: 1923
    DBFileName: ProgrammingCompetition.db
    SaveDirectory: Solutions

The files in the *Languages* directory may contain files of any name. The
name should suggest the language that is being specified. Only the
languages specified in this directory are allowed to be chosen as the
language of a submission. These files specifiy the commands used to compile
and run the projects in said language. The format of these file are
[setting]: [value], and may contain (Java is shown as an example):

    compile: javac src/<problem>/Main.java
    run: java -cp src <problem>.Main

The \<problem\> flags are parsed at runtime and resolved to the title
specified for the problem the submission should be expected to solve.

In the *Problems* directory, each problem should be in its own folder
named by the order in which it should appear. If a *0* is specified, then
this is assumed to be the practice problem, and will cause the server to
start on the practice phase and not count the results from this problem
towards that of the real competition.

While multiple input and output text files may be specified to test
submissions against, only one is needed. These files are fed one at a time
to the submission after successful compilation. If the submission's output
does not match the specified output exactly, then the submission is counted
as wrong and a time penalty is issued to the team.

The *statement.txt*, *sampleInput.txt*, and *sampleOutput.txt* are
files that allow the teams to download the problem statement and sample
input and output to use while testing their code before submission. This
can remove or supliment paper descriptions of the problems.

The *settings.txt* file in each problem dierectory contain settings in the
form \[settings\]: \[value\]. The following settings are available:

    title: Problem 0