import java.io.*;
import java.net.*;
import java.time.Period;
import java.util.*;
public class p2pServer {

	private static final Integer TIME_MILISSECONDS = 25;

	public static List<Peer> registry(List<Peer> peers, String nickname, InetAddress address, Integer port, String resourceList, List<Integer> timeoutVal, DatagramSocket socket) throws IOException {
		byte[] response = new byte[1024];
		
		int j;
	
		for (j = 0; j < peers.size(); j++) {
			if (peers.get(j).getNickname().equals(nickname))
			break;
		}
		
		if (j == peers.size()) {
			Peer p = new Peer(nickname, address, port, resourceList);
			peers.add(p);
			timeoutVal.add(TIME_MILISSECONDS);

			response = "OK".getBytes();
		} else {
			response = "NOT OK".getBytes();
		}
		
		DatagramPacket packet = new DatagramPacket(response, response.length, address, port);
		socket.send(packet);

		return peers;
	}

	public static String query(String fileName, List<Peer> peers, InetAddress address, Integer port, DatagramSocket socket) throws IOException {
		String returnedList = null;
		byte[] response = new byte[1024];

		for (Peer peer : peers) {
			String hash = (String) peer.getResourceList().get(fileName);
			String content = "\nFilename: " + fileName + " Hash: " + hash + " => " + peer.getNickname() + " port: " + peer.getPort() + " address: " + peer.getAddress() + "\n";

			returnedList+=content;
		}

		DatagramPacket packet = new DatagramPacket(returnedList.getBytes(), returnedList.getBytes().length, address, port);
		socket.send(packet);

		return returnedList;
	}

	public static List<Integer> hearbeat(String nickname, List<Peer> peers, List<Integer> timeoutVal) {
		System.out.println("Heartbeat from " + nickname);

		for (int i = 0; i < peers.size(); i++) {
			if (peers.get(i).getNickname().equals(nickname)){
				timeoutVal.set(i, TIME_MILISSECONDS);
			}
		}

		return timeoutVal;
	}

	public static List<Integer> removePeer(List<Peer> peers, List<Integer> timeoutVal) {
		for (int i = 0; i < timeoutVal.size(); i++) {
			timeoutVal.set(i, timeoutVal.get(i) - 1);
			if (timeoutVal.get(i) == 0) {
				System.out.println("\nPeer " + peers.get(i).getNickname() + " is dead.");
				peers.remove(i);
				timeoutVal.remove(i);
			}
		}
		return timeoutVal;
	}

	public static void main(String[] args) throws IOException {
		// String content = null;
		DatagramSocket socket = new DatagramSocket(9000);
		DatagramPacket packet;
		// InetAddress addr;
		// int port;
		byte[] resource = new byte[1024];
		
		// List<String> resourceList = new ArrayList<>();
		// List<InetAddress> resourceAddr = new ArrayList<>();
		// List<Integer> resourcePort = new ArrayList<>();
				
		// String vars[] = content.split("\\s");
		
		List<Peer> peers = new ArrayList<>();
		List<Integer> heartbeatRegister = new ArrayList<>();
		
		while (true) {
			try {
				// recebe datagrama
				packet = new DatagramPacket(resource, resource.length);
				socket.setSoTimeout(500);
				socket.receive(packet);

				System.out.println("\nDatagram received!");
								
				// processa o que foi recebido, adicionando a uma lista
				String contentReceived = new String(packet.getData(), 0, packet.getLength());
				InetAddress addressReceived = packet.getAddress();
				Integer portReceived = packet.getPort();
				String vars[] = contentReceived.split("\\s");

				if(vars.length > 1){
					switch (vars[0]) {
						case "registry":
							peers = registry(peers, vars[1], addressReceived, portReceived, vars[2], heartbeatRegister, socket);
							break;

						case "query":
							query(vars[1], peers, addressReceived, portReceived, socket);
							break;

						case "p2p":
							// dado peer, hash, port, address, nome do arquivo e hash enviar o aquivo via UDP
							
							break;

						case "heartbeat":
							heartbeatRegister = hearbeat(vars[1], peers, heartbeatRegister);
							break;
					
						default:
							break;
					}
				}

			} catch (IOException e) {
				heartbeatRegister = removePeer(peers, heartbeatRegister);
				System.out.print(".");
			}
		}
	}
}
