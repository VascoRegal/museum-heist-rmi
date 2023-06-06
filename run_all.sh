#!/bin/bash

servers=(Register GeneralRepo CollectionSite Parties Museum)
clients=(MasterThief OrdinaryThief)


xterm -T RMI -hold -e "sh RMIDeployAndRun.sh" &

sleep 3

for s in ${servers[@]}; do
    xterm -T $s -hold -e "sh ${s}DeployAndRun.sh" &
    sleep 3
done

sleep 2

for c in ${clients[@]}; do
    xterm -T $c -hold -e "sh ${c}DeployAndRun.sh" &
    sleep 3
done
