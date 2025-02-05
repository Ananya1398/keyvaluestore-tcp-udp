import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * TCPClient class to interact with the TCP key-value store server.
 */
public class TCPClient {

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
    if (args.length < 2) {
      log("Missing arguments. Please use this format: java TCPClient <hostaddress> <port>");
      return;
    }

    String hostAddress = args[0];
    int port;
    try {
      port = Integer.parseInt(args[1]);
    } catch (NumberFormatException e) {
      log("Invalid port number. Please enter a valid integer for the port.");
      return;
    }

    try (Socket socket = new Socket(hostAddress, port);
         BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
         BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
         BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

      socket.setSoTimeout(10000);
      log("Connected to TCP server at " + hostAddress + ":" + port);

      log("Starting data pre-population");
      String[] prepopulatedData = {
              "put 1 panda",
              "put 2 bird",
              "put 3 cat",
              "put 4 dog",
              "put 5 fish",
      };

      for (String operation : prepopulatedData) {
        writer.write(operation);
        writer.newLine();
        writer.flush();

        try {
          String response = reader.readLine();
          if (response != null) {
            log("Response from server for prepopulating data : " + response);
          } else {
            log("Received unexpected response: " + response + " for operation: " + operation);
          }
        } catch (SocketTimeoutException e) {
          log("Timeout while waiting for response to prepopulate data request: " + operation);
        }
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
        writer.write(operation);
        writer.newLine();
        writer.flush();

        try {
          String response = reader.readLine();
          if (response != null) {
            log("Response from server for executing commands data : " + response);
          } else {
            log("Received unexpected response: " + response + " for operation: " + operation);
          }
        } catch (SocketTimeoutException e) {
          log("Timeout while waiting for response to executing commands: " + operation);
        }
      }

      while (true) {
        log("Enter the operation followed by arguments: (Put key value, Get key, Delete key) or type 'close' to exit client:");
        String input = consoleReader.readLine();

        if (input.equalsIgnoreCase("close")) {
          log("Closing client connection!");
          break;
        }

        writer.write(input);
        writer.newLine();
        writer.flush();

        try {
          String response = reader.readLine();
          if (response != null) {
            log("Response from server: " + response);
          } else {
            log("Received unexpected response for input: " + input);
          }
        } catch (SocketTimeoutException e) {
          log("Timeout while waiting for response to request: " + input);
        }
      }

    } catch (ConnectException e) {
      log("Server is not running on this port, please start server before running client");
    } catch (SocketException e) {
      log("Connection broken. Server may have stopped");
    } catch (UnknownHostException e){
      log("Incorrect Host Address entered");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

