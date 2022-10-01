import java.io.*;
import java.net.*;
import java.util.*;

public class p2pPeer {

	public static void main(String[] args) throws IOException {
		String vars[] = args[1].split("\\s");

		Map<String, String> resourceList = new Peer(null, null, null, vars[2]).getResourceList()

		new p2pPeerThread(args, resourceList).start();
		new p2pPeerHeartbeat(args).start();
		new p2pPeerClient(args, resourceList).start();
	}
}
