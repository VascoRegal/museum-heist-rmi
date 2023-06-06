echo "Transfering data to the parties node."
sshpass -f password ssh sd302@l040101-ws03.ua.pt 'mkdir -p deploy/MuseumHeist'
sshpass -f password ssh sd302@l040101-ws03.ua.pt 'rm -rf deploy/MuseumHeist/*'
sshpass -f password scp dirParties.zip sd302@l040101-ws03.ua.pt:deploy/MuseumHeist
echo "Decompressing data sent to the parties node."
sshpass -f password ssh sd302@l040101-ws03.ua.pt 'cd deploy/MuseumHeist ; unzip -uq dirParties.zip'
echo "Executing program at the parties node."
sshpass -f password ssh sd302@l040101-ws03.ua.pt 'cd deploy/MuseumHeist/dirParties; chmod u+x ./parties.sh; ./parties.sh sd302'