import java.io.*;
import java.net.*;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * UDPClient class to interact with the UDP key-value store server.
 */
public class UDPClient {
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
   * Connects to a server using the provided hostaddress and port,
   *
   * @param args an array of command-line arguments where the first argument should be the host address
   *             and the second argument should be the port number
   */
  public static void main(String[] args) {
    if (args.length != 2) {
      log("Missing arguments. Please use this format: java UDPClient <hostaddress> <port>");
      return;
    }

    String hostAddress = args[0];
    int serverPort;
    try {
      serverPort = Integer.parseInt(args[1]);
    } catch (NumberFormatException e) {
      log("Invalid port number. Please enter a valid integer for the port.");
      return;
    }

    try (DatagramSocket socket = new DatagramSocket()) {
      socket.setSoTimeout(10000);

      log("Starting data pre-population");
      String[] prepopulatedData = {
              "put 1 panda",
              "put 2 bird",
              "put 3 cat",
              "put 4 dog",
              "put 5 fish",
      };

      for (String operation : prepopulatedData) {
        sendRequest(operation, socket, hostAddress, serverPort);
      }

      log("Starting 5 PUTs, 5 GETs, 5 DELETEs");
      String[] execute_commands = {
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
      };
      for (String operation : execute_commands) {
        sendRequest(operation, socket, hostAddress, serverPort);
      }

      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
      while (true) {
        log("Enter the operation followed by arguments: (Put key value, Get key, Delete key) or type 'close' to quit:");
        String command = reader.readLine().trim();

        if ("close".equalsIgnoreCase(command)) {
          log("Closing client connection!");
          break;
        }

        sendRequest(command, socket, hostAddress, serverPort);
      }
    } catch (Exception e) {
      log("Error: " + e.getMessage());
    }
  }

  /**
   * Sends a request to the specified server and waits for a response.
   * It handles potential timeouts when waiting for a response and logs appropriate messages.
   *
   * @param request The request message to be sent to the server.
   * @param socket The DatagramSocket used to send and receive the request and response.
   * @param serverHost The hostname or IP address of the server to send the request to.
   * @param serverPort The port number of the server to send the request to.
   */
  private static void sendRequest(String request, DatagramSocket socket, String serverHost, int serverPort) {
    try {
      byte[] buffer = request.getBytes();
      InetAddress serverAddress = InetAddress.getByName(serverHost);
      DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverAddress, serverPort);
      socket.send(packet);

      byte[] responseBuffer = new byte[1024];
      DatagramPacket responsePacket = new DatagramPacket(responseBuffer, responseBuffer.length);
      try {
        socket.receive(responsePacket);
        String response = new String(responsePacket.getData(), 0, responsePacket.getLength()).trim();
        log("Response from server: " + response);
      } catch (SocketTimeoutException e) {
        log("Timeout while waiting for response to request: " + request);
      }
    } catch (IOException e) {
      log("Error sending request: " + e.getMessage());
    }
  }
}
