import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The TCPServer class implements a TCP server that listens for client requests,
 * processes key-value store commands, and sends the results back to the client.
 */
public class TCPServer {
  private final KeyValueStoreOperations keyValueStore = new KeyValueStoreOperations();
  private Socket socket;

  /**
   * Sets the socket value
   * @param socket socket value
   */
  public void setSocket(Socket socket) {
    this.socket = socket;
  }

  /**
   * Handles incoming client requests, processes them, and sends responses.
   * The method also logs incoming requests and outgoing responses.
   *
   */
  public void execute() {
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

      String input;
      while ((input = reader.readLine()) != null) {

        if (input.trim().isEmpty()) {
          log("Received malformed request from " + socket.getInetAddress() + ":" + socket.getPort());
          writer.write("Error: Malformed request.");
          writer.newLine();
          writer.flush();
          return;
        }
        log("Received request: " + input + " from " + socket.getInetAddress() + ":" + socket.getPort());
        String result = executeOperations(input);

        writer.write(result);
        writer.newLine();
        writer.flush();
        log("Sent this response back to the client: " + result);
      }
      log("Client disconnected: " + socket.getInetAddress() + ":" + socket.getPort());
    } catch (IOException e) {
      log("An error occurred: " + e.getMessage());
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
    try {
      switch (operation.toLowerCase()) {
        case "put":
          if (arguments.length < 3) {
            return "Invalid input. Need to provide both key and value for Put command";
          }
          String value = arguments[2];
          if (keyValueStore.containsKey(key)){
            return "Key " + key + " already exists in the key value store.";
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
    } catch (Exception e) {
      return "Error processing operation: " + e.getMessage();
    }
  }

  /**
   * Logs a message with a timestamp in milliseconds.
   *
   * @param message The message to log.
   */
  private static void log(String message) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    String timestamp = sdf.format(new Date());
    System.out.println("[" + timestamp + "] " + message);
  }

  /**
   * Initializes the server by parsing the port number from the command-line arguments,
   * creating a server socket, and waiting for a client connection.
   *
   * @param args an array of command-line arguments where the first argument should be the port number
   * @throws NumberFormatException if the provided port is not a valid integer
   */
  public static void main(String[] args) {
    if (args.length != 1) {
      log("Missing arguments. Please use this format: java TCPServer <port>");
      return;
    }

    int port;
    try {
      port = Integer.parseInt(args[0]);
    } catch (NumberFormatException e) {
      log("Invalid port number. Please enter a valid integer for the port.");
      return;
    }

    try (ServerSocket serverSocket = new ServerSocket(port)) {
      log("TCP Server is running... Waiting for a connection...");

      while(true) {
        Socket socket = serverSocket.accept();
        log("Client connected! "+ socket.getInetAddress() + ":" + socket.getPort());

        TCPServer keyValueServer = new TCPServer();
        keyValueServer.setSocket(socket);
        keyValueServer.execute();
      }
    }
    catch (Exception e) {
      log("An error occured: " + e.getMessage());
    }
  }
}
