import java.io.*;
import java.net.*;
import java.util.*;

public class p2pPeer {

	public static void main(String[] args) throws IOException {

		// System.out.println("ARGS ====> " + args.length);

		// if (args.length != 5) {

		// 	System.out.println("Uso: java p2pPeer <server> \"<message>\" <localport>");
		// 	System.out.println("<message> is:");
		// 	System.out.println("registry <nickname> docs/a1,docs/a2,docs/a3");
		// 	System.out.println("list nickname");
		// 	System.out.println("wait");

		// 	return;
		// } else {
			new p2pPeerThread(args).start();
			new p2pPeerHeartbeat(args).start();
			new p2pPeerClient(args).start();
		// }
	}
}
