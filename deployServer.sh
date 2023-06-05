#!/bin/bash

host=$1
server=$2
user=sd302

printf "\n[+] Deploying $2 to $host..."

sshpass -f password ssh sd302@$host 'mkdir -p run/'
sshpass -f password ssh sd302@$host 'rm -rf run/*'
sshpass -f password scp MuseumHeist.zip sd302@$host:run/
printf "\n[+] Decompressing data sent to $host."
sshpass -f password ssh sd302@$host 'cd run/ ; unzip -uq MuseumHeist.zip'
