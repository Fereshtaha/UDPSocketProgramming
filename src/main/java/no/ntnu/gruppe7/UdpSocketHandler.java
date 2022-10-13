package no.ntnu.gruppe7;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Communicate with the UDP task server, solve the problems according to the protocol
 * Solution for UDP Socket programming exercise in IDATA2304 Computer Networking
 */
public class UdpSocketHandler {

    // Server IP
    private static final String SERVER_ADDRESS = "129.241.152.12";

    // Server UDP port
    private static final int SERVER_UDP_PORT = 1234;

    // UDP socket used for communication
    DatagramSocket clientSocket;

    // Buffer where the incoming data from UDP socket will be stored
    byte[] responseDataBuffer = new byte[1024]; // Reserve a bit more space than one would normally need

    // We will reuse this packet for incoming UDP packets
    DatagramPacket receivePacket = new DatagramPacket(responseDataBuffer, responseDataBuffer.length);

    /**
     * Communicate with the UDP server according to the protocol
     */
    public void run() throws IOException {
        System.out.println("Starting UDP sender");
        for (int i = 0; i<3; i++){
            if (sendTaskRequest()){
                System.out.println("Task sent successfully");
                String task = listenForResponse();
                String answer = TaskLogic.solveTask(task);
                if (answer != null){
                    sendToServer(answer);
                    String response = listenForResponse();
                    if (TaskLogic.hasServerApproval(response)){
                        System.out.println("Good, task solved");
                    } else {
                        System.out.println("Error, task not solved");
                    }
                }
            }
        }
    }




    /**
     * Wait for a response from the UDP task server
     * @return True on success; false on error
     */
    private String listenForResponse() throws IOException {

        // Code adapted from
        // https://github.com/ntnu-datakomm/server-side/blob/main/example-udp-server/src/main/java/no/ntnu/UdpClient.java

        String response = null;

        try {
            this.clientSocket.receive(receivePacket);
            response = new String(receivePacket.getData(), 0, receivePacket.getLength());
        } catch (IOException e){
            throw new RuntimeException(e);
        }
        return response;
    }

    /**
     * Send a new task request to the server
     */
    private boolean sendTaskRequest() {
        return sendToServer(TaskLogic.TASK_REQUEST);
    }

    /**
     * Send a UDP message to the server
     * @param message The message to send
     *
     * @return True on success, false on error
     */
    private boolean sendToServer(String message) {
        boolean success = false;

        // Code adapted from
        // https://github.com/ntnu-datakomm/server-side/blob/main/example-udp-server/src/main/java/no/ntnu/UdpClient.java
        byte[] dataToSend = message.getBytes();
        try {
            this.clientSocket = new DatagramSocket();
            InetAddress serverAddress = InetAddress.getByName(SERVER_ADDRESS);
            DatagramPacket sendPacket = new DatagramPacket(dataToSend, dataToSend.length, serverAddress, SERVER_UDP_PORT);
            clientSocket.send(sendPacket);
            if (message.equals("task")){
                success = true;
            }

        } catch (Exception e){
            System.out.println("Could not be sent to server" + e.getMessage());
            throw new RuntimeException(e);
        }
       return success;
    }
}

