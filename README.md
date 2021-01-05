# SwarmPubTran
A Research - Applying swarm intelligence for public transportation, NetLogo model and additional proprietary tools such as map drawer and NetLogo extension for parsing input files.

# Dataset
The datasets for cities roads graph and transportation data was taken from this repository: https://github.com/bstabler/TransportationNetworks, 
which is an update to Dr. Hillel Bar-Gera's TNTP project.

Ideally, for our research needs, each city folder contains the following files:

* `<City>_trips.tntp` - contains a sampled origin-destination data.
* `<City>_node.tntp` - contains the bus stations list (each can be either an origin or/and a destination).
* `<City>_net.tntp` - contains the list of existing roads between those nodes (from the node.tntp file).


# The NetLogo Model
The net logo model receives paths for 3 files:

1. `nodes-file-path` - a `<City>_node.tntp` file (probably after some map-size-related transformation using the additional utility tools)
2. `map-path` - a city road map png image, drawn (green over grey colored to be compatible with the model) by the `Map Drawer` utility, which will be described in more detail later.
3. `trips-path` - a `<City>_trips.tntp` file, the amount of passengers (ants) going from a specific origin to a specific destination will be relative to the amount in the file, 
while considering the `number-of-passengers` as the total amount of passengers
