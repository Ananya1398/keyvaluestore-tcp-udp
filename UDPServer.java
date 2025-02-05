import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The UDPServer class implements a UDP server that listens for client requests,
 * processes key-value store commands, and sends the results back to the client.
 */
public class UDPServer {
  private final KeyValueStoreOperations keyValueStore = new KeyValueStoreOperations();
  private DatagramSocket socket;


  /**
   * Starts listening for incoming UDP requests.
   * If the request is malformed or an error occurs, an appropriate message is sent back to the client.
   *
   * @param port The port on which the server listens for incoming requests.
   * @throws SocketException If an error occurs while setting up the DatagramSocket.
   *
   */
  public void execute(int port) throws SocketException {
    log("UDP Server is running...");
    byte[] buffer = new byte[1024];
    socket = new DatagramSocket(port);
    log("Socket initialized successfully on port " + port);
    while (true) {
      try {
        DatagramPacket requestPacket = new DatagramPacket(buffer, buffer.length);
        socket.receive(requestPacket);

        String input = new String(requestPacket.getData(), 0, requestPacket.getLength()).trim();
        InetAddress clientAddress = requestPacket.getAddress();
        int clientPort = requestPacket.getPort();

        if (input.isEmpty()) {
          log("Received malformed request from " + clientAddress + ":" + clientPort);
          sendResponse("Error: Malformed request.", clientAddress, clientPort);
          continue;
        }

        log("Received request: " + input + " from " + clientAddress + ":" + clientPort);
        String response = executeOperations(input);

        sendResponse(response, clientAddress, clientPort);
        log("Sent response to client: " + response);
      } catch (IOException e) {
        log("An error occurred here: " + e.getMessage());
        e.printStackTrace();
      }
    }
  }

  /**
   * Sends a response to the client.
   *
   * @param response The message to send to the client.
   * @param address The address of the client to send the response to.
   * @param port The port number on which to send the response.
   */
  private void sendResponse(String response, InetAddress address, int port) {
    try {
      byte[] responseData = response.getBytes();
      DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length, address, port);
      socket.send(responsePacket);
    } catch (IOException e) {
      log("Error sending response: " + e.getMessage());

    }
  }

  /**
   * Executes the given operation command (put, get, delete) on the key-value store.
   * The method also handles exceptions that may arise during the execution
   * of the operations.
   *
   * @param command The operation command in the format: <operation> <key> [<value>].
   * @return A string containing the result of the operation or an error message if the operation is invalid.
   */
  public String executeOperations(String command) {
    String[] arguments = command.split(" ");
    if (arguments.length < 2) {
      return "Invalid input. Need to provide at least 1 operation and 1 argument";
    }

    String operation = arguments[0];
    String key = arguments[1];
    try{
    switch (operation.toLowerCase()) {
      case "put":
        if (arguments.length < 3) {
          return "Invalid input. Need to provide both key and value for Put command";
        }
        String value = arguments[2];
        if (keyValueStore.containsKey(key)) {
          return "Key " + key + " already exists in the key-value store.";
        }
        keyValueStore.put(key, value);
        return "Successfully added key: " + key + " with value: " + value;

      case "get":
        String result = keyValueStore.get(key);
        return (result == null) ? "Key not found: " + key : "Value for key " + key + ": " + result;

      case "delete":
        String deletedValue = keyValueStore.delete(key);
        return (deletedValue == null) ? "Key not found for deletion: " + key : "Successfully deleted key: " + key;

      default:
        return "Invalid operation. Please use 'put', 'get', or 'delete' commands. Or 'close' to exit the client";
    }
  }
    catch (Exception e) {
      return "Error processing operation: " + e.getMessage();
    }
  }

  /**
   * Logs a message with a timestamp.
   * @param message The message to log.
   */
  private static void log(String message) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    String timestamp = sdf.format(new Date());
    System.out.println("[" + timestamp + "] " + message);
  }

  /**
   * Main entry point of the UDP server application.
   * Takes the server port as a command-line argument, validates it, and starts the UDP server
   * to listen for incoming requests.
   *
   * @param args Command-line arguments. The first argument should be the port number on which the server listens.
   */
  public static void main(String[] args) {
    if (args.length != 1) {
      log("Missing arguments. Please use this format: java UDPServer <port>");
      return;
    }

    int port;
    try {
      port = Integer.parseInt(args[0]);
    } catch (NumberFormatException e) {
      log("Invalid port number. Please enter a valid integer for the port.");
      return;
    }

    try {
      UDPServer keyValueServer = new UDPServer();
      keyValueServer.execute(port);
    }
    catch (Exception e) {
      log("An error occurred: " + e.getMessage());
      e.printStackTrace();
    }
  }
}
