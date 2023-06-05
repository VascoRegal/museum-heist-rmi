package clientSide.main;


import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import clientSide.entities.OrdinaryThief;
import consts.HeistConstants;
import consts.Resolver;
import interfaces.CollectionSiteInterface;
import interfaces.GeneralRepoInterface;
import interfaces.MuseumInterface;
import interfaces.PartiesInterface;

/**
 * Client Instantiation
 */
public class ClientOrdinaryThief {
    public static void main(String[] args)
    {
        OrdinaryThief [] ots = new OrdinaryThief[HeistConstants.NUM_THIEVES];
        
        GeneralRepoInterface generalMemoryStub = null;
        CollectionSiteInterface collectionSiteMemoryStub = null;
        PartiesInterface partiesMemoryStub = null;
        MuseumInterface museumMemoryStub = null;
        Registry registry = null;
        
        
        try {
            registry = LocateRegistry.getRegistry(Resolver.RMIHostName, Resolver.RMIPort);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(1);
        }

        try {
            generalMemoryStub = (GeneralRepoInterface) registry.lookup(Resolver.RMIGeneralName);
            collectionSiteMemoryStub = (CollectionSiteInterface) registry.lookup(Resolver.RMIColSiteName);
            partiesMemoryStub = (PartiesInterface) registry.lookup(Resolver.RMIPartiesName);
            museumMemoryStub = (MuseumInterface) registry.lookup(Resolver.RMIMuseumName);
        } catch (RemoteException | NotBoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(1);
        }
        


        for (int i = 0; i < HeistConstants.NUM_THIEVES; i++)
        {
            ots[i] =  new OrdinaryThief(i, generalMemoryStub, collectionSiteMemoryStub, partiesMemoryStub, museumMemoryStub);
            ots[i].start();
        }

        for (int i = 0; i < HeistConstants.NUM_THIEVES; i++)
        {
            try {
                ots[i].join();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}