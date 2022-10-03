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
				socket.send(packet);
				Thread.sleep(timeheartbeat);
			} catch (IOException e) {
				System.out.println("Erro enviando hearbeat ====> " + e);
				socket.close();
			} catch(InterruptedException e) {
				System.out.println("Error na Thread Sleep ====> " + e);
			}
		}
	}
}
