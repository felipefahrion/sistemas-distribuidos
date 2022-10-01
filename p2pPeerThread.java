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

		for (int i = 0; i < args.length; i++) {
			System.out.println(i + " " + args[i]);
		}

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
			// envia um packet
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

				System.out.println("PACKET ====> " + response);
				
				// mostra a resposta
				String data = new String(packet.getData(), 0, packet.getLength());
				System.out.println("Received:\n" + data);

				// 0   1      2      
				// p2p a1.txt <hash>
				String vars[] = data.split("\\s");

				if(vars.length > 1 && vars[0].equals("p2p")){

					if(resourceList.get(vars[1]).equals(vars[2])){

						File sendFile = new File("received/copy_" + vars[1]);
						String content = Files.readString(Paths.get("docs/" + vars[1]));
								
						FileOutputStream fos = new FileOutputStream(sendFile);
				
						fos.write(content.getBytes(StandardCharsets.UTF_8), 0, content.length());
				
						fos.flush();
						fos.close();

						ByteArrayOutputStream b = new ByteArrayOutputStream(content.length());
						b.writeTo(fos);

						b.flush();
						b.close();

						DatagramPacket filePacket = new DatagramPacket(b.toByteArray(), resource.length, addr, packet.getPort());
						socket.send(filePacket);
					}
					
				}

				System.out.println("DATA ====> " + packet.getData());
				System.out.println("String ====> " + data);
				
			} catch (IOException e) {
				// System.out.println(e);
			}
		}

	}
}
