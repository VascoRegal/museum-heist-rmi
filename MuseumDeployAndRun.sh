echo "Transfering data to the museum repository node."
sshpass -f password ssh sd302@l040101-ws06.ua.pt 'mkdir -p deploy/MuseumHeist'
sshpass -f password ssh sd302@l040101-ws06.ua.pt 'rm -rf deploy/MuseumHeist/*'
sshpass -f password scp dirMuseum.zip sd302@l040101-ws06.ua.pt:deploy/MuseumHeist
echo "Decompressing data sent to the museum repository node."
sshpass -f password ssh sd302@l040101-ws06.ua.pt 'cd deploy/MuseumHeist ; unzip -uq dirMuseum.zip'
echo "Executing program at the museum repository node."
sshpass -f password ssh sd302@l040101-ws06.ua.pt 'cd deploy/MuseumHeist/dirMuseum; chmod u+x ./museum.sh; ./museum.sh sd302'