# String Operations TCP Client-Server Program

## Overview
This Java program implements a TCP client-server application and a UDP client-server application that implements a key value store and allows the user to perform PUT, GET and Delete operations.

## Files
- **TCPServer.java**: Listens for client connections on a dynamic port via TCP and performs key value store operations
- **TCPClient.java**: Connects to the TCP server, sends commands, and displays the server results.
- **UDPServer.java**: Listens for client connections on a dynamic port via UDP and performs key value store operations
- **UDClient.java**: Sends commands to the UDP server and displays the server results.
- **KeyValueStoreOperations.java**: Implements the KeyValueStoreOperation interface and contains the Hashmap and PUT, GET, DELETE and ContainsKey logic

## How to Run
Unzip the files and store in your desired location. Then proceed with the given steps:

### 1. **Run the TCP Server**:
1. Open Terminal 1. We will run the TCP server here.
2. Navigate to the folder where the code is saved. Use 'cd' to enter the src folder.
3. Compile the server file:
```bash
javac TCPServer.java
```
4. Provide the port number
```bash 
java TCPServer <port>
````
If no port is provided, the server will ask for it again.

### 2. **Run the TCP Client**:
1. Open Terminal 2. We will run the client here.
2. Navigate to the folder where the code is saved. Use 'cd' to enter the src folder.
3. Compile the client file:
```bash
javac TCPClient.java
```
4. Provide the server address and port number
```bash
java TCPClient <server address> <port>

```
### 3. **Run the UDP Server**:
1. Open Terminal 3. We will run the UDP server here.
2. Navigate to the folder where the code is saved. Use 'cd' to enter the src folder.
3. Compile the server file:
```bash
javac UDPServer.java
```
4. Provide the port number
```bash 
java UDPServer <port>
````
If no port is provided, the server will ask for it again.

### 4. **Run the UDP Client**:
1. Open Terminal 4. We will run the UDP client here.
2. Navigate to the folder where the code is saved. Use 'cd' to enter the src folder.
3. Compile the client file:
```bash
javac UDPClient.java
```
4. Provide the server address and port number
```bash
java UDPClient <server address> <port>

```

### 5. **Key Value Store operations**:
This section remains common for both TCP and UDP. Once your client is up and running (in the case TCP connected to the server) it will start to prepopulate the Hashmap with 5 data points by running the following commands:

              "put 1 panda",
              "put 2 bird",
              "put 3 cat",
              "put 4 dog",
              "put 5 fish",

Then it will perform 5 PUT operations, 5 GET operations and 5 DELETE operations as follows:

              "put 6 monkey",
              "put 7 elephant",
              "put 8 raccoon",
              "put 9 swan",
              "put 10 duck",
              "get 6",
              "get 7",
              "get 8",
              "get 9",
              "get 10",
              "delete 6",
              "delete 7",
              "delete 8",
              "delete 9",
              "delete 10",

After this it will prompt the user to input any additional commands they wish to execute, as follows:

        Enter the operation followed by arguments: (Put key value, Get key, Delete key) or type 'close' to exit client:

Enter the operation with the required arguments to complete the operation execution. Enter 'close' to close client connection

### 6. **View Output**:
The responses from the server at every step will be displayed with timestamps as follows:

[2025-01-31 20:52:26.531] Response from server for prepopulating data : Successfully added key: 3 with value: cat


### 5. **Example**:
#### On Terminal 1: 
```bash

javac TCPServer.java

java TCPServer 32000
```
(You will now see this message: TCP Server is running... Waiting for a connection...)

#### On Terminal 2:
```bash

javac TCPClient.java

java TCPClient 127.0.0.1 32000
```

(On Terminal 1 you will now see this message: Client connected! /127.0.0.1:61485)

Now you will see all the responses for the prepopulation task as well as 5 PUTs, 5 GETs and 5 DELETEs

Next you will be prompted to enter your command as follows:
```bash
[2025-01-31 20:52:26.533] Enter the operation followed by arguments: (Put key value, Get key, Delete key) or type 'close' to exit client:
```

Request:
```bash
Put 11 deer
```
Response on server:
```bash
[2025-01-31 20:52:26.533]Received request: put 11 deer from /127.0.0.1:61485
[2025-01-31 20:52:26.533] Sent this response back to the client: Successfully added key: 11 with value: deer
```
Response on client:
```bash
[2025-01-31 20:52:26.533] Response from server for executing commands data : Successfully added key: 11 with value: deer
```

Similar example can be followed for UDP as well. Just replace the TCP commands with UDP commands as explained in the sections above.