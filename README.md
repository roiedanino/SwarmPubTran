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
![Screenshot/NetLogoModel.png](https://github.com/roiedanino/SwarmPubTran/blob/master/Screenshots/Net%20Logo%20Model.png)
## Installation Steps: 
1. [Download](https://ccl.northwestern.edu/netlogo/6.1.0/) and install Net Logo 6.1.0
2. [Download this repo as zip](https://github.com/roiedanino/SwarmPubTran/archive/master.zip) or clone using: `git clone https://github.com/roiedanino/SwarmPubTran.git`
3. The extension jar for NetLogo is located under `SwarmPubTran/NetLogoExtensions/build/libs/read-origins.jar`, you can re-generate it using: `gradlew fatJar`.
4. Inside the `<NetLogo installation folder>/extentions` create a folder named `read-origins`
5. Locate the extension jar file under `NetLogo 6.1.0/extensions/read-origins`

## Running the model
1. Launch NetLogo 6.1.0
2. File -> Open -> Choose `SwarmPubTran/Net Logo Code/pubtran.nlogo` from the cloned repository.
3. Click the `reset` button on the top right corner
4. Change parameters (described later) as needed
5. Click `setup`
6. Click `run`

## Model Parameters
The net logo model receives paths for 3 files:

1. `nodes-file-path` - a `<City>_node.tntp` file (probably after some map-size-related transformation using the additional utility tools)
2. `map-path` - a city road map png image, drawn (green over grey colored to be compatible with the model) by the `Map Drawer` utility, which will be described in more detail later.
3. `trips-path` - a `<City>_trips.tntp` file, the amount of passengers (ants) going from a specific origin to a specific destination will be relative to the amount in the file, 
while considering the `number-of-passengers` as the total amount of passengers

#### Other parameters
`color-sensitivity` -- changes the intensity of the pheromones, so it will be easier to adjust it as needed while the model is running. 

#### Research Parameters
Parameters related to determining the "Natural Conditions" for the model, in general the model moves ants from different origin pixels to their destination pixels,
anywhere an ant passes its "eating" some of the grass in that pixel, making that pixel a little bit darker, while diffusing 2 kinds of pheromones:
1. 'stay-away' - tells the other ants to stay away and prevent collisions
2. 'come-here' - tells the other ants to come near, just like real ants attraction pheromones

Research Parameter | Description | Example Value
--- | --- | --- |
`growth-decelration` | Determines how slow the grass grows back after being "eaten" by an ant | 0.001
`chipping-factor` |  The precentage of the grass left after an ant passes over it | 0.995
`stay-away-incr` | Determines the addition factor of the stay-away pheromone | 0.2
`stay-away-decay` | Determines the multiplication factor of the stay-away pheromone | 0.99
`come-here-incr` | Determines the addition factor of the come-here pheromone | 0.045
`come-here-decay` | Determines the multiplication factor of the come-here pheromone | 0.5


# The Map Drawer
![Map Drawer Screenshot](https://github.com/roiedanino/SwarmPubTran/blob/master/Screenshots/MapDrawerScale.png)
Map Drawer is a Java application for converting origin/destination and road files in TNTP format to a png image with patch colors suitable for reading by the NetLogo environment.
The application would list the available cities from the data-set of your choosing, you may also choose the result image size, while 100 works seems to work the best, the graph would scale automatically as big as it can fit. The application also creates a new fixed coordinates file which fits the scaled graph and suite the NetLogo coordinate system.

