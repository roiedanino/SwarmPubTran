
def _56To32(x):
    return x * (32/56) - 16

with open("SiouxFalls_node.tntp") as file:
    lines = file.readlines()

    for line in lines[1:]:
        parts = line.split('\t')
        origin = parts[0]
        x = int(parts[1])/10000
        y = int(parts[2])/10000
        print("{} ({}, {})".format(origin, int(_56To32(x)), 32 - int(_56To32(y))))
