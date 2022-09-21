import java.io.*;
import java.net.*;
import java.util.*;

public class p2pPeerHeartbeat extends Thread {
	protected DatagramSocket socket = null;
	protected DatagramPacket packet = null;
	protected InetAddress address = null;
	protected byte[] data = new byte[1024];
	protected int port;

	private final int timeheartbeat = 10000;

	public p2pPeerHeartbeat(String[] args) throws IOException {
		String vars[] = args[1].split("\\s");

		data = ("heartbeat " + vars[1]).getBytes();
		address = InetAddress.getByName(args[0]);
		port = Integer.parseInt(args[2]) + 100;
		socket = new DatagramSocket(port);
	}

	public void run() {
		while (true) {
			try {
				packet = new DatagramPacket(data, data.length, address, 9000);

				System.out.println(packet.toString());
				socket.send(packet);
			} catch (IOException e) {
				System.out.println("Error in send hearbeat ====> " + e);
				socket.close();
			}
			
			try {
				Thread.sleep(timeheartbeat);
			} catch(InterruptedException e) {
				System.out.println("Error in Thread Sleep ====> " + e);
			}

			System.out.println("\n sent heartbeat <3!");
		}
	}
}
