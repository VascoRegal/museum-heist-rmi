echo "Transfering data to the registry node."
sshpass -f password ssh sd302@l040101-ws07.ua.pt 'mkdir -p deploy/MuseumHeist'
sshpass -f password scp dirRegister.zip sd302@l040101-ws07.ua.pt:deploy/MuseumHeist
echo "Decompressing data sent to the registry node."
sshpass -f password ssh sd302@l040101-ws07.ua.pt 'cd deploy/MuseumHeist ; unzip -uq dirRegister.zip'
echo "Executing program at the registry node."
sshpass -f password ssh sd302@l040101-ws07.ua.pt 'cd deploy/MuseumHeist/dirRegister ; chmod u+x ./register.sh; ./register.sh sd302'
