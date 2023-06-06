echo "Compiling source code."
javac -source 1.8 -target 1.8 */*.java */*/*.java
echo "Distributing intermediate code to the different execution environments."
echo "  RMI registry"
rm -rf dirRMI/interfaces
mkdir -p dirRMI/interfaces
cp interfaces/*.class dirRMI/interfaces
echo "  Register"
rm -rf dirRegister/serverSide dirRegister/interfaces dirRegister/consts
mkdir -p dirRegister/serverSide dirRegister/serverSide/main dirRegister/serverSide/objects dirRegister/interfaces \
         dirRegister/consts
cp serverSide/main/ServerRegister.class dirRegister/serverSide/main
cp serverSide/objects/RegisterRemoteObject.class dirRegister/serverSide/objects
cp consts/Resolver.class dirRegister/consts
cp interfaces/Register.class dirRegister/interfaces
echo "  General Repo"
rm -rf dirGeneral/serverSide dirGeneral/clientSide dirGeneral/interfaces dirGeneral/consts
mkdir -p dirGeneral/serverSide dirGeneral/serverSide/main dirGeneral/serverSide/objects dirGeneral/interfaces \
         dirGeneral/clientSide dirGeneral/clientSide/entities dirGeneral/consts
cp serverSide/main/ServerGeneralRepo.class dirGeneral/serverSide/main
cp serverSide/objects/GeneralRepo.class dirGeneral/serverSide/objects
cp interfaces/Register.class interfaces/GeneralRepoInterface.class dirGeneral/interfaces
cp clientSide/entities/ThiefState.class dirGeneral/clientSide/entities
cp consts/*.class dirGeneral/consts
echo "  Collection Site"
rm -rf dirCollectionSite/serverSide dirCollectionSite/clientSide dirCollectionSite/interfaces dirCollectionSite/consts dirCollectionSite/structs
mkdir -p dirCollectionSite/serverSide dirCollectionSite/serverSide/main dirCollectionSite/serverSide/objects dirCollectionSite/interfaces \
         dirCollectionSite/clientSide dirCollectionSite/clientSide/entities dirCollectionSite/consts dirCollectionSite/structs
cp serverSide/main/ServerCollectionSite.class dirCollectionSite/serverSide/main
cp serverSide/objects/CollectionSite.class dirCollectionSite/serverSide/objects
cp interfaces/Register.class interfaces/CollectionSiteInterface.class dirCollectionSite/interfaces
cp clientSide/entities/ThiefState.class dirCollectionSite/clientSide/entities
cp consts/*.class dirCollectionSite/consts
cp structs/MemQueue.class dirCollectionSite/structs

echo "  Parties"
rm -rf dirParties/serverSide dirParties/clientSide dirParties/interfaces dirParties/consts dirParties/structs
mkdir -p dirParties/serverSide dirParties/serverSide/main dirParties/serverSide/objects dirParties/serverSide/entities dirParties/interfaces \
         dirParties/clientSide dirParties/clientSide/entities dirParties/consts dirParties/structs
cp serverSide/main/ServerParties.class dirParties/serverSide/main
cp serverSide/objects/Parties.class dirParties/serverSide/objects
cp serverSide/entities/RoomState.class dirParties/serverSide/entities
cp interfaces/Register.class interfaces/PartiesInterface.class dirParties/interfaces
cp clientSide/entities/ThiefState.class clientSide/entities/Thief.class dirParties/clientSide/entities
cp consts/*.class dirParties/consts
cp structs/*.class dirParties/structs

echo "  Museum"
rm -rf dirMuseum/serverSide dirMuseum/interfaces dirMuseum/consts dirMuseum/structs
mkdir -p dirMuseum/serverSide dirMuseum/serverSide/main dirMuseum/serverSide/objects dirMuseum/serverSide/entities dirMuseum/interfaces \
        dirMuseum/consts dirMuseum/structs
cp serverSide/main/ServerMuseum.class dirMuseum/serverSide/main
cp serverSide/objects/Museum.class dirMuseum/serverSide/objects
cp serverSide/entities/*.class dirMuseum/serverSide/entities
cp interfaces/Register.class interfaces/MuseumInterface.class dirMuseum/interfaces
cp consts/*.class dirMuseum/consts
cp structs/Utils.class dirMuseum/structs


echo "  MasterThief"
rm -rf dirMasterThief/clientSide dirMasterThief/clientSide/main dirMasterThief/clientSide/entities dirMasterThief/interfaces dirMasterThief/consts
mkdir -p dirMasterThief/clientSide dirMasterThief/clientSide/entities dirMasterThief/clientSide/main dirMasterThief/interfaces \
        dirMasterThief/consts
cp clientSide/main/ClientMasterThief.class dirMasterThief/clientSide/main
cp clientSide/entities/*.class dirMasterThief/clientSide/entities
cp consts/*.class dirMasterThief/consts
cp interfaces/*.class dirMasterThief/interfaces


echo "  OrdinaryThief"
rm -rf dirOrdinaryThief/clientSide dirOrdinaryThief/clientSide/main dirOrdinaryThief/clientSide/entities dirOrdinaryThief/interfaces dirOrdinaryThief/consts \
        dirOrdinaryThief/structs
mkdir -p dirOrdinaryThief/clientSide dirOrdinaryThief/clientSide/entities dirOrdinaryThief/clientSide/main dirOrdinaryThief/interfaces \
        dirOrdinaryThief/consts dirOrdinaryThief/structs
cp clientSide/main/ClientOrdinaryThief.class dirOrdinaryThief/clientSide/main
cp clientSide/entities/*.class dirOrdinaryThief/clientSide/entities
cp consts/*.class dirOrdinaryThief/consts
cp interfaces/*.class dirOrdinaryThief/interfaces
cp structs/Utils.class dirOrdinaryThief/structs

echo "Compressing execution environments."
echo "  RMI registry"
rm -f  dirRMI/dirRMI.zip
zip -rq dirRMI.zip dirRMI
echo "  Register"
rm -f dirRegister/dirRegister.zip
zip -rq dirRegister.zip dirRegister
echo "  General Repo"
rm -f  dirGeneral.zip
zip -rq dirGeneral.zip dirGeneral
echo "  Collection Site"
rm -f dirCollectionSite.zip
zip -rq dirCollectionSite.zip dirCollectionSite
echo "  Parties"
rm -f dirParties.zip
zip -rq dirParties.zip dirParties
echo "  Museum"
rm -f dirMuseum.zip
zip -rq dirMuseum.zip dirMuseum
echo "  MasterThief"
rm -f dirMasterThief.zip
zip -rq dirMasterThief.zip dirMasterThief
echo "  OrdinaryThief"
rm -f dirOrdinaryThief.zip
zip -rq dirOrdinaryThief.zip dirOrdinaryThief


