#!/bin/bash

servers=(GeneralRepo CollectionSite Parties Museum)
clients=(MasterThief OrdinaryThief)


for s in ${servers[@]}; do
    xterm -T $s -hold -e "sh ${s}DeployAndRun.sh" &
    sleep 3
done

sleep 2

for c in ${clients[@]}; do
    xterm -T $c -hold -e "sh ${c}DeployAndRun.sh" &
    sleep 3
done
