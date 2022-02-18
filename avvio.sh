#!/bin/sh

if [ $# -ne 2 ]; then
	echo "Uso $0: $0 <ip> <port>"
	exit 1
fi

java -jar crAIzy.jar $1 $2