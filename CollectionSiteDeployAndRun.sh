echo "Transfering data to the collection site repository node."
sshpass -f password ssh sd302@l040101-ws02.ua.pt 'mkdir -p deploy/MuseumHeist'
sshpass -f password ssh sd302@l040101-ws02.ua.pt 'rm -rf deploy/MuseumHeist/*'
sshpass -f password scp dirCollectionSite.zip sd302@l040101-ws02.ua.pt:deploy/MuseumHeist
echo "Decompressing data sent to the collection site repository node."
sshpass -f password ssh sd302@l040101-ws02.ua.pt 'cd deploy/MuseumHeist ; unzip -uq dirCollectionSite.zip'
echo "Executing program at the collection site repository node."
sshpass -f password ssh sd302@l040101-ws02.ua.pt 'cd deploy/MuseumHeist/dirCollectionSite; chmod u+x ./collection_site.sh; ./collection_site.sh sd302'