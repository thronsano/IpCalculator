import ipaddress
import sys

network = ipaddress.ip_network(sys.argv[1])
subnetsAmount = int(sys.argv[2])

for network in list(network.subnets(prefixlen_diff=subnetsAmount)):
    print(network)
