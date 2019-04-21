import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.IOException;

class MrSender implements Runnable{

	private static String msg;
	private static InetAddress address;
	private static int port;
	private static DatagramSocket socket;

	public MrSender(String msg, InetAddress address, int port, DatagramSocket socket) {
		this.msg = msg;
		this.address = address;
		this.port = port;
		this.socket = socket;
	}

	public void run() {
		try {
        	byte[] message = msg.getBytes();
           	DatagramPacket msg_packet = new DatagramPacket(message, message.length, address, port);
            socket.send(msg_packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}