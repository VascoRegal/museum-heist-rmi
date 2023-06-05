echo "Transfering data to the general repository node."
sshpass -f password ssh sd302@l040101-ws01.ua.pt 'mkdir -p deploy/MuseumHeist'
sshpass -f password ssh sd302@l040101-ws01.ua.pt 'rm -rf deploy/MuseumHeist/*'
sshpass -f password scp dirGeneral.zip sd302@l040101-ws01.ua.pt:deploy/MuseumHeist
echo "Decompressing data sent to the general repository node."
sshpass -f password ssh sd302@l040101-ws01.ua.pt 'cd deploy/MuseumHeist ; unzip -uq dirGeneral.zip'
echo "Executing program at the general repository node."
sshpass -f password ssh sd302@l040101-ws01.ua.pt 'cd deploy/MuseumHeist/dirGeneral; chmod u+x ./general.sh; ./general.sh sd302'