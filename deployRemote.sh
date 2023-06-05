#!/bin/bash

RMIHostName="l040101-ws07.ua.pt"
RMIPort=22174

GeneralHost="l040101-ws01.ua.pt"
CollectionSiteHost="l040101-ws02.ua.pt"  
PartiesHost="l040101-ws03.ua.pt"
MuseumHost="l040101-ws06.ua.pt"

user=sd302


# build and zip code 
find . -name "*.class" -type f -print0 | xargs -0 /bin/rm -f


printf "\n[+] Zipping project..."

cd ..
zip -rq MuseumHeist.zip museum-heist-rmi --exclude=password --exclude=*.git* --exclude=*.class
cd museum-heist-rmi
mv -f ../MuseumHeist.zip .

printf "\n[+] Start rmi registry"
sshpass -f password ssh ${user}@${RMIHostName} 'rmiregistry ${RMIPort}'


./deployServer.sh $GeneralHost ServerGeneralRepo
