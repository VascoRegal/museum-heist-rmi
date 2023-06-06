echo "Transfering data to the master thief node."
sshpass -f password ssh sd302@l040101-ws05.ua.pt 'mkdir -p deploy/MuseumHeist'
sshpass -f password ssh sd302@l040101-ws05.ua.pt 'rm -rf deploy/MuseumHeist/*'
sshpass -f password scp dirMasterThief.zip sd302@l040101-ws05.ua.pt:deploy/MuseumHeist
echo "Decompressing data sent to the master thief node."
sshpass -f password ssh sd302@l040101-ws05.ua.pt 'cd deploy/MuseumHeist ; unzip -uq dirMasterThief.zip'
echo "Executing program at the master thief node."
sshpass -f password ssh sd302@l040101-ws05.ua.pt 'cd deploy/MuseumHeist/dirMasterThief; chmod u+x ./master_thief.sh; ./master_thief.sh sd302'