import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class p2pPeerThread extends Thread {
	protected DatagramSocket socket = null;
	protected DatagramPacket packet = null;
	protected InetAddress addr = null;
	protected byte[] resource = new byte[1024];
	protected byte[] response = new byte[1024];
	protected int port;
	protected String[] vars;
	protected Map<String, String> resourceList = null;


	public p2pPeerThread(String[] args, Map<String, String> resourceContent) throws IOException {
		// envia um packet
		resource = args[1].getBytes();
		addr = InetAddress.getByName(args[0]);
		port = Integer.parseInt(args[2]);
		// cria um socket datagrama
		socket = new DatagramSocket(port);
		vars = args[1].split("\\s");

		resourceList = resourceContent;
	}

	public void run() {
		
		try {
			DatagramPacket packet = new DatagramPacket(resource, resource.length, addr, 9000);
			socket.send(packet);
		} catch (IOException e) {
			socket.close();
		}
		
		while (true) {
			try {
				// obtem a resposta
				packet = new DatagramPacket(response, response.length);
				socket.setSoTimeout(500);
				socket.receive(packet);
				
				// mostra a resposta
				String data = new String(packet.getData(), 0, packet.getLength());
				System.out.println("Received:\n" + data);

				// 0   1      2      
				// p2p a1.txt <hash>
				String vars[] = data.split("\\s");

				if(vars.length > 1 && vars[0].equals("p2p")){
					System.out.println("P2P BOLADAO");

					for (int i = 0; i < vars.length; i++) {
						System.out.println(i + " " + vars[i]);
					}

					if(resourceList.get(vars[1]).equals(vars[2])){
						String contentFile = Files.readString(Paths.get("docs/" + vars[1]));
						String content = "newfile" + ";" + vars[1] + ";" + contentFile;

						System.out.println("CONTENT ====> " + content);

						ByteArrayOutputStream b = new ByteArrayOutputStream(contentFile.length());
						b.write(content.getBytes());
						
						DatagramPacket filePacket = new DatagramPacket(b.toByteArray(), content.length(), packet.getAddress(), packet.getPort());
						socket.send(filePacket);

						b.flush();
						b.close();
					}
					
				}

			} catch (IOException e) {
				// System.out.println(e);
			}
		}

	}
}
