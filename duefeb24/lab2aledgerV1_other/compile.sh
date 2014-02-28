#!/bin/bash

# I recommend aliasing to 'scalacompile'
# use 'unalias scalacompile' to delete alias

#compiles the *.scala file and places the *.class and *$.class files in the classes folder

scalac -d classes $1
