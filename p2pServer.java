import java.io.*;
import java.net.*;
import java.time.Period;
import java.util.*;
public class p2pServer {

	private static final Integer TIME_MILISSECONDS = 10;

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
			String content = "Filename: " + fileName + " Hash: " + hash + " => " + peer.getNickname() + " port: " + peer.getPort() + " address: " + peer.getAddress() + " | \n";

			returnedList+=content;
		}

		DatagramPacket packet = new DatagramPacket(returnedList.getBytes(), response.length, address, port);
		socket.send(packet);

		return returnedList;
	}

	public static List<Integer> hearbeat(String nickname, List<Peer> peers, List<Integer> timeoutVal) {
		System.out.println("Heartbeat from " + nickname);

		System.out.println(timeoutVal.size());

		for (int i = 0; i < peers.size(); i++) {
			if (peers.get(i).getNickname().equals(nickname)){
				System.out.println("é o " + nickname);
				timeoutVal.set(i, TIME_MILISSECONDS);
			}
		}

		System.out.println("SOPAAA");
		for (Integer integer : timeoutVal) {
			System.out.println(integer);
		}

		return timeoutVal;
	}

	public static List<Integer> removePeer(List<Peer> peers, List<Integer> timeoutVal) {
		for (int i = 0; i < timeoutVal.size(); i++) {
			timeoutVal.set(i, timeoutVal.get(i) - 1);
			System.out.println("olahndo valor: " + timeoutVal.get(i));
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

				System.out.println("\nRecebi!");
								
				// processa o que foi recebido, adicionando a uma lista
				String contentReceived = new String(packet.getData(), 0, packet.getLength());
				InetAddress addressReceived = packet.getAddress();
				Integer portReceived = packet.getPort();
				String vars[] = contentReceived.split("\\s");

				if(vars.length > 1){
					switch (vars[0]) {
						case "registry":
							// se já existe o peer com o mesmo nickname - "Peer already exists!"
							// adicona na lista de peers o novo peer com os metadados - "Peer created!"
							// envia datagrama	

							// registry
								// vars[0] method
								// vars[1] nickname
								// vars[2] resource
								
							peers = registry(peers, vars[1], addressReceived, portReceived, vars[2], heartbeatRegister, socket);
							break;

						case "query":
							// dado um nome de arquivo enviar o nome do peer, hash, port, address 

							// query
								// vars[0] method
								// vars[1] filename

							query(vars[1], peers, addressReceived, portReceived, socket);
							break;

						case "p2p":
							// dado peer, hash, port, address, nome do arquivo e hash enviar o aquivo via UDP
							
							break;

						case "heartbeat":
							//verificar se o peer está ativo
							heartbeatRegister = hearbeat(vars[1], peers, heartbeatRegister);
							break;
					
						default:
							break;
					}
				}
			
				// if (vars[0].equals("create") && vars.length > 1) {
				// 	int j;
					
				// 	for (j = 0; j < resourceList.size(); j++) {
				// 		if (resourceList.get(j).equals(vars[1]))
				// 			break;
				// 	}
					
				// 	if (j == resourceList.size()) {
				// 		resourceList.add(vars[1]);
				// 		resourceAddr.add(addr);
				// 		resourcePort.add(port);
				// 		timeoutVal.add(timeoutSeconds);		/* 500ms * timeoutSeconds = 7.5s (enough for 5s heartbeat) */
						
				// 		response = "OK".getBytes();
						
				// 	} else {
				// 		response = "NOT OK".getBytes();
				// 	}
					
				// 	packet = new DatagramPacket(response, response.length, addr, port);
				// 	socket.send(packet);
				// }
				
				// if (vars[0].equals("list") && vars.length > 1) {
				// 	for (int j = 0; j < resourceList.size(); j++) {
				// 		if (resourceList.get(j).equals(vars[1])) {
				// 			for (int i = 0; i < resourceList.size(); i++) {
				// 				String data = new String(resourceList.get(i) + " " + resourceAddr.get(i).toString() + " " + resourcePort.get(i).toString());
				// 				response = data.getBytes();
								
				// 				packet = new DatagramPacket(response, response.length, addr, port);
				// 				socket.send(packet);
				// 			}
				// 			break;
				// 		}
				// 	}
				// }
				
				// if (vars[0].equals("heartbeat") && vars.length > 1) {
				// 	System.out.print("\nheartbeat: " + vars[1]);
				// 	for (int i = 0; i < resourceList.size(); i++) {
				// 		if (resourceList.get(i).equals(vars[1]))
				// 			timeoutVal.set(i, timeoutSeconds);
				// 	}
				// }

			} catch (IOException e) {
				// decrementa os contadores de timeout a cada 500ms (em função do receive com timeout)
				// for (int i = 0; i < timeoutVal.size(); i++) {
				// 	System.out.println(timeoutVal.get(i));
				// 	timeoutVal.set(i, timeoutVal.get(i) - 1);
				// 	System.out.println(timeoutVal.get(i));

				// 	if (timeoutVal.get(i) == 0) {
				// 		System.out.println("\nuser " + peers.get(i) + " is dead.");
				// 		peers.remove(i);
				// 		timeoutVal.remove(i);
				// 	}
				// }
				heartbeatRegister = removePeer(peers, heartbeatRegister);
				System.out.print(".");
			}
		}
	}
}
