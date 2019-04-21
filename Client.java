import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.io.IOException;
import java.util.Scanner;


public class Client {
    private static DatagramSocket socket;
    private static InetAddress address;
    private static byte[] buf;
 
    public Client() {
        try {
            socket = new DatagramSocket();
            socket.setSoTimeout(1000);
        } catch (SocketException e) {
            System.out.println("Unable to create socket!");
            return;
        }

        try {
            address = InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }

    public static String receive() {
        byte[] read_buf = new byte[2048];
        DatagramPacket packet = new DatagramPacket(read_buf, read_buf.length);
        try {
                socket.receive(packet);
            } catch (SocketTimeoutException ex) {
                System.out.println("Сервер не отвечает..");
            } catch (IOException e) {
                e.printStackTrace();
            } 

        String received = new String(
          packet.getData(), 0, packet.getLength()).trim();
        return received;
    }

    public static void send_bytes(byte[] arr) {
        send_string("import");
        try {
            Thread.sleep(2000);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        DatagramPacket packet 
          = new DatagramPacket(arr, arr.length, address, 5555);
        try {
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public static void send_string(String msg) {
        byte[] write_buf = msg.getBytes();
        DatagramPacket packet 
          = new DatagramPacket(write_buf, write_buf.length, address, 4444);
        try {
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
 
    public static void close() {
        socket.close();
    }

    public static void main(String[] args) {
        Client client = new Client();
        while (true) {
            Scanner input = new Scanner(System.in);
            String input_line = input.nextLine();
            if (!input_line.trim().isEmpty()){
                String[] command = input_line.split(" ", 2);
                if (command[0].equals("import")){
                    StackHandler stack = new StackHandler((String) command[1], "client_data");
                    client.send_bytes(stack.serialize());
                    System.out.println(Client.receive());
                }
                else {
                    client.send_string(input_line);
                    System.out.println(receive());
                }
            }
        }        
    }
}