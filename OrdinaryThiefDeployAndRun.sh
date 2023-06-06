echo "Transfering data to the ordinary thief."
sshpass -f password ssh sd302@l040101-ws08.ua.pt 'mkdir -p deploy/MuseumHeist'
sshpass -f password ssh sd302@l040101-ws08.ua.pt 'rm -rf deploy/MuseumHeist/*'
sshpass -f password scp dirOrdinaryThief.zip sd302@l040101-ws08.ua.pt:deploy/MuseumHeist
echo "Decompressing data sent to the ordinary thief node."
sshpass -f password ssh sd302@l040101-ws08.ua.pt 'cd deploy/MuseumHeist ; unzip -uq dirOrdinaryThief.zip'
echo "Executing program at the oridnary thief node."
sshpass -f password ssh sd302@l040101-ws08.ua.pt 'cd deploy/MuseumHeist/dirOrdinaryThief; chmod u+x ./ordinary_thief.sh; ./ordinary_thief.sh sd302'