#!/bin/bash

RMIHostName="l040101-ws07.ua.pt"
RMIPort=22174

GeneralHost="l040101-ws01.ua.pt"
CollectionSiteHost="l040101-ws02.ua.pt"  
PartiesHost="l040101-ws03.ua.pt"
MuseumHost="l040101-ws06.ua.pt"

user=sd302


# build and zip code 
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

zip -rq MuseumHeist.zip ../museum-heist-rmi

# Start rmi registry
# sshpass -f password ssh ${user}@${RMIHostName} 'rmiregistry ${RMIPort}'
