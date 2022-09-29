import java.io.*;
import java.net.*;
import java.util.*;

public class p2pPeer {

	public static void main(String[] args) throws IOException {
		new p2pPeerThread(args).start();
		new p2pPeerHeartbeat(args).start();
		new p2pPeerClient(args).start();
	}
}
