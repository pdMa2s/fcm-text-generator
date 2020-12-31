# fcm-text-generator

University of Aveiro
Departamento de Electrónica, Telecomunicações e Informática
MEI

This work was developed for course of **Algorithmic Information Theory** (2017/2018).

It is compused of 2 main programs fcm and generator. 
The program fcm creates a finite-context model based on a text file, calculates its entropy
and asks the user if he wants to print that finite-context model.
The program generator also creates a finite-context model but then creates text based on it, prints
that text, the text's entropy and alphabet.
For more details about the programs check this work's report(Report.pdf) or see the USAGE by runnig 
the programs.

How to run the programs:
```bash
$ javac -d class src/*.java

$ cd class

```

Depending o the program that you want to execute either do
```bash

$java fcm
```

or 
```bash

$java generator
```

Follow the USAGE intructions.
