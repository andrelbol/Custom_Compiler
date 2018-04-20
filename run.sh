#!/bin/bash

if [ $# -eq 0 ]
then
  echo "ERROR: A file must be passed as argument."
elif [ $# -gt 1 ]
then
    echo "ERROR: Only 1 file must be passed as argument."
fi 

java -cp bin main.Main $1