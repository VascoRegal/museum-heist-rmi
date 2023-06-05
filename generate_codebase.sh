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

