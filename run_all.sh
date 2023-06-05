#!/bin/bash

"find . -name "*.class" -type f -print0 | xargs -0 /bin/rm -f"

serverDir="serverSide/main/"
servers=(ServerGeneralRepo ServerCollectionSite ServerParties ServerMuseum)
clientDir="clientSide/main/"
clients=(ClientMasterThief ClientOrdinaryThief)

printf "[+] Building Servers...\n"
for s in ${servers[@]}; do
    printf "\t $s\n"
    javac ${serverDir}${s}.java
    printf "\n"
done


printf "\n\n[+] Building Clients...\n"
for c in ${clients[@]}; do
    printf "\t $c\n" 
    javac ${clientDir}${c}.java
    printf "\n"
done


read -p ">>> Press any key to launch servers"

printf "[+] Launching Servers...\n"
for s in ${servers[@]}; do
    printf "\t $s\n"
    xterm -T $s -hold -e java ${serverDir}${s} &
    printf "\n"
    sleep 1
done


read -p ">>> Press any key to launch clients"

printf "\n\n[+] Launching Clients...\n"
for c in ${clients[@]}; do
    printf "\t $c\n" 
    xterm -T $c -hold -e java ${clientDir}${c} &
    printf "\n"
    sleep 1
done