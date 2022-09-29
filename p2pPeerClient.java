import java.io.*;
import java.net.*;
import java.util.*;

public class p2pPeerClient extends Thread {
	protected DatagramSocket socket = null;
	protected DatagramPacket packet = null;
	protected InetAddress addr = null;
	protected byte[] resource = new byte[1024];
	protected byte[] response = new byte[1024];
	protected int port, peer_port;
	private Peer peer;

	public p2pPeerClient(String[] args) throws IOException {

		// port = args[2]
		// address = args[0]
		// content = args[1]

		port = Integer.parseInt(args[2]) + 101;

		String vars[] = args[1].split("\\s");

		// method = vars[0]
		// nickname = vars[1]
		// resourceList = vars[2]

		peer = createPeer(vars[0], args[0], args[2], vars[2]);
		socket = new DatagramSocket(port);
	}

	public Peer createPeer(String nickname, String address, String port, String resourceList)
			throws NumberFormatException, UnknownHostException {
		return new Peer(nickname, InetAddress.getByName(address), Integer.parseInt(port), resourceList);
	}

	public void run() {
		BufferedReader obj = new BufferedReader(new InputStreamReader(System.in));
		String str = "";

		while (true) {
			System.out.println("Exemplo para solicitar um doc: query a1.txt <server_ip>");
			System.out.println("Exemplo para baixar um doc: p2p a1.txt <hash> <peer_ip> <peer_port>"); // WIP

			try {
				str = obj.readLine();
				String vars[] = str.split("\\s");

				addr = InetAddress.getByName(vars[2]);

				switch (vars[0]) {
					case "query":
						peer_port = 9000;
						String contentQuery = vars[0] + " " + vars[1];
						resource = contentQuery.getBytes();

						break;

					case "p2p":

							// filename = vars[1]
							// hash = vars[2]
							// peer_ip = vars[3]
							// peer_port = vars[4]

							String contentP2p = vars[0] + " " + vars[1]  + " " + vars[2]  + " " + vars[3]  + " " + vars[4];
							resource = contentP2p.getBytes();

						break;

					default:
						peer_port = Integer.parseInt(vars[3]);
						break;
				}

				packet = new DatagramPacket(resource, resource.length, addr, peer_port);
				socket.send(packet);
			} catch (Exception e) {
				System.out.println(e);
			}

			while (true) {
				try {
					// obtem a resposta
					packet = new DatagramPacket(response, response.length);
					socket.setSoTimeout(500);
					socket.receive(packet);

					// mostra a resposta
					String resposta = new String(packet.getData(), 0, packet.getLength());
					System.out.println("recebido: " + resposta);
				} catch (IOException e) {
					break;
				}
			}
		}
	}
}
