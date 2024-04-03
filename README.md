Assuming that you are on the root folder of bft smart here are examples of how to run the code:
cd build/install/library && ./smartrun.sh intol.dti.imp.client.DTIInteractiveClient 4
cd build/install/library && ./smartrun.sh intol.dti.imp.server.DTIServer 0

Instructions of commands appear after running the code and typing help will make them reappear.

For this project i decided to follow a object oriented approach and tried to abstract as much as possible the code from 
the bft smart library. I developed 2 classes containing the NFT and Coin Logic that would work on any other library with 
few adaptations then this classes are saved in the snapshot and the relation between them is set when it is installed.
For the message i developed 2 types of messages to better separate the nft and coin concepts. When the server receives them
he tries to check the type of message and processes in its own way.
Read requests are implemented in unordered requests to improve performance but in case that operation fails they are also
implemented in ordered requests in the server side.
The client has access to an abstraction that uses server proxy to communicate with the servers, the only thing client does
is sending the requests parameters and receive the response, the rest of the logic is all contained in the server side.
In order to maintain a valid and safe state.

Bruno Cotrim, FC54406, Group 12

```