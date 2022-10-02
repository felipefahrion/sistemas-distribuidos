import java.io.*;
import java.util.*;

public class p2pPeer {

	public static void main(String[] args) throws IOException {
		String vars[] = args[1].split("\\s");

		Peer p = new Peer(null, null, null, vars[2]);
		Map<String, String> resourceList = p.getResourceList();

		System.out.println("==> Peer criado");
		new p2pPeerThread(args, resourceList).start();
		new p2pPeerHeartbeat(args).start();
		new p2pPeerClient(args, resourceList).start();
	}
}
