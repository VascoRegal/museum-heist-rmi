CODEBASE="http://l040101-ws07.ua.pt/"$1"/classes/"
rmiregistry -J-Djava.rmi.server.codebase=$CODEBASE\
            -J-Djava.rmi.server.hostname="l040101-ws07.ua.pt"\
            -J-Djava.rmi.server.useCodebaseOnly=true $2