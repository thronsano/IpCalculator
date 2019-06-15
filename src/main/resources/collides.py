import ipaddr
import sys

networkOne = ipaddr.IPNetwork(sys.argv[1])
networkTwo = ipaddr.IPNetwork(sys.argv[2])
print(networkOne.overlaps(networkTwo))
