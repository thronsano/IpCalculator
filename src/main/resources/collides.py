import ipaddress
import sys

networkOne = ipaddress.ip_network(sys.argv[1])
networkTwo = ipaddress.ip_network(sys.argv[2])
print(networkOne.overlaps(networkTwo))
