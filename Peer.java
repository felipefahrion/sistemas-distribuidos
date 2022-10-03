import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class Peer {
    private String nickname;
    private InetAddress address;
    private Integer port;
    private Map<String, String> resourceList;

    public Peer(String nickname, InetAddress address, Integer port, String resourceList) {
        this.nickname = nickname;
        this.address = address;
        this.port = port;
        this.resourceList = resourceMap(resourceList);
    }

    private Map<String, String> resourceMap(String resourceList){

        Map<String, String> resource = new HashMap<>();

        try {
            String docs[] = resourceList.split("\\,");

            for (String e : docs) {
                String hash = generateHash(fileContent(e));
                String fileName = e.replaceAll("docs/", "");

                resource.put(fileName, hash);
            }       

        } catch (IOException e) {
            System.out.println(e);
        }
        return resource;
    }

    private String fileContent(String path) throws IOException {
        return Files.readString(Paths.get(path));
    }

    private String generateHash(String fileContent){
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        
        BigInteger hash = new BigInteger(1, md.digest(fileContent.getBytes()));

        return hash.toString(16);
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public InetAddress getAddress() {
        return address;
    }

    public void setAddress(InetAddress address) {
        this.address = address;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Map<String, String> getResourceList() {
        return resourceList;
    }

    public void setResourceList(Map<String, String> resourceList) {
        this.resourceList = resourceList;
    }
}
