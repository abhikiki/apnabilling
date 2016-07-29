#!/bin/bash
#
# Create a port forward so we can monitor the production Memcache instance using the local phpMemcachedAdmin tool
#
echo -n "Username: "
read USERNAME
ssh -L 11212:lngsacprclsvc00.examen.com:11211 $USERNAME@lngsacprclsvc00.examen.com
