public class Packet {

    private String sourceIpString;
    private String destIpString;
    private long sourceIp;
    private long destIp;
    private long port;

    /**
     * Contructor for a packet with just an ip
     * @param sourceIp
     */
    public Packet (String sourceIp) {

        this.sourceIpString = sourceIp;
        this.sourceIp = ipToLong(sourceIp);

    }

    /**
     * Constructor for a packet with a dest and source ip
     * @param sourceIp
     * @param destIp
     */
    public Packet (String sourceIp, String destIp) {

        this.sourceIpString = sourceIp;
        this.destIpString = destIp;
        this.sourceIp = ipToLong(sourceIp);
        this.destIp = ipToLong(destIp);
        this.port = 0;

    }

    /**
     * Contructor for a packet with a dest ip, source ip, and port
     * @param sourceIp
     * @param destIp
     * @param port
     */
    public Packet (String sourceIp, String destIp, String port) {

        this.sourceIpString = sourceIp;
        this.destIpString = destIp;
        this.sourceIp = ipToLong(sourceIp);
        this.destIp = ipToLong(destIp);
        this.port = Long.parseLong(port);

    }

    // getters

    public String getSourceIpString () {
        return sourceIpString;
    }

    public String getDestIpString () {
        return destIpString;
    }

    public long getSourceIp () {
        return sourceIp;
    }

    public long getDestIp () {
        return destIp;
    }

    public long getPort () {
        return port;
    }

    /**
     * Mathod to convert IPs for bitwise masking and manipulation
     * @param ipAddress - address being converted
     * @return - converted address
     */
    public static long ipToLong(String ipAddress) {
        
        String[] ipAddressArr = ipAddress.split("\\.");
        long results = 0;

        for (int i = 0 ; i < 4; i++) {

            long ipAddressValue = Long.parseLong(ipAddressArr[i]);

            results |= (ipAddressValue << (24 - (8 * i)));

        }

        return results;

    }
    
}
