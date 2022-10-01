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

	public p2pPeerClient(String[] args, Map<String, String> resourceList) throws IOException {

		// port = args[2]
		// address = args[0]
		// content = args[1]

		port = Integer.parseInt(args[2]) + 101;

		String vars[] = args[1].split("\\s");

		// method = vars[0]
		// nickname = vars[1]
		// resourceList = vars[2]

		peer = createPeer(vars[0], args[0], port, vars[2]);
		socket = new DatagramSocket(port);
	}

	public Peer createPeer(String nickname, String address, Integer port, String resourceList)
			throws NumberFormatException, UnknownHostException {
		return new Peer(nickname, InetAddress.getByName(address), port, resourceList);
	}

	public void run() {
		BufferedReader obj = new BufferedReader(new InputStreamReader(System.in));
		// String str = "";

		while (true) {
			System.out.println("Exemplo para solicitar um doc: query a1.txt <server_ip>");
			System.out.println("Exemplo para baixar um doc: p2p a1.txt <hash> <peer_ip> <peer_port>"); // WIP
			System.out.println("Exemplo para baixar um doc: peer \"text\" <peer_ip> <peer_port>");

			try {
				String str = obj.readLine();
				String vars[] = str.split("\\s");

				
				switch (vars[0]) {
					case "query":
						peer_port = 9000;
						addr = InetAddress.getByName(vars[2]);
						String contentQuery = vars[0] + " " + vars[1];
						resource = contentQuery.getBytes();
						
						break;
						
					case "peer":
						peer_port = Integer.parseInt(vars[3]);
						addr = InetAddress.getByName(vars[2]);
						resource = vars[1].getBytes();

						break;

					case "p2p":
						// 0   1      2      3         4
						// p2p a1.txt <hash> <peer_ip> <peer_port>"

						peer_port = Integer.parseInt(vars[4]);
						addr = InetAddress.getByName(vars[3]);

						String content = vars[0] + " " + vars[1] + " " + vars[2];
						resource = content.getBytes();

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
