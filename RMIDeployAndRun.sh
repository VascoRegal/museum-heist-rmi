echo "Transfering data to the RMIregistry node."
sshpass -f password ssh sd302@l040101-ws08.ua.pt 'mkdir -p deploy/MuseumHeist'
sshpass -f password ssh sd302@l040101-ws08.ua.pt 'rm -rf deploy/MuseumHeist/*'
sshpass -f password ssh sd302@l040101-ws08.ua.pt 'mkdir -p Public/classes/interfaces'
sshpass -f password ssh sd302@l040101-ws08.ua.pt 'rm -rf Public/classes/interfaces/*'
sshpass -f password scp dirRMI.zip sd302@l040101-ws08.ua.pt:deploy/MuseumHeist
echo "Decompressing data sent to the RMIregistry node."
sshpass -f password ssh sd302@l040101-ws08.ua.pt 'cd deploy/MuseumHeist ; unzip -uq dirRMI.zip'
sshpass -f password ssh sd302@l040101-ws08.ua.pt 'cd deploy/MuseumHeist/dirRMI ; cp interfaces/*.class /home/sd302/Public/classes/interfaces ; cp rmi.sh /home/sd302'
echo "Executing program at the RMIregistry node."
sshpass -f password ssh sd302@l040101-ws08.ua.pt 'chmod u+x ./rmi.sh; ./rmi.sh sd302 22315'