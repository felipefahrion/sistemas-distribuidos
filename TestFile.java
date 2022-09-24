import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.Map;

public class TestFile {
    public static void main(String[] args) throws IOException {
        // ClassLoader classLoader = getClass().getClassLoader();
        // FileInputStream in = new FileInputStream("docs/a1.txt");

        // String content = Files.readString(Paths.get("docs/a1.txt"));

        // System.out.println(content);

        Peer p = new Peer("pipo", InetAddress.getByName("127.0.0.1"), 9000, "docs/a1.txt,docs/a2.txt,docs/a3.txt");

        System.out.println(p.getResourceList().keySet());
        // for (Map.Entry<String,String> entry : p.getResourceList()) {
        //     System.out.println(entry);
        // }
    }
}
