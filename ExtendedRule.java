public class ExtendedRule {

    private String action;
    private long sourceIp;
    private long destIp;
    private long sourceMask;
    private long destMask;
    private long startPort;
    private long endPort;
    private boolean onePort;
    private boolean rangeOfPorts;
    
    /**
     * Contructor for no ports
     * @param action
     * @param sourceIp
     * @param destIp
     * @param sourceMask
     * @param destMask
     */
    public ExtendedRule (String action, String sourceIp, String sourceMask, String destIp, String destMask){
        
        // checks what the action is and saves it accordingly
        if (action.equals("permit")){

            this.action = "permitted";

        } 
        else {

            this.action = "denied";

        }

        // checks if the source is any
        if (sourceIp.equals("any")) {

            this.sourceIp = 0;
            this.sourceMask = 0;

        } 
        else {

            this.sourceMask = ~ipToLong(sourceMask);
            this.sourceIp = ipToLong(sourceIp) & this.sourceMask;

        }

        // checks if the dest is any
        if (destIp.equals("any")) {

            this.destIp = 0;
            this.destMask = 0;

        } 
        else {

            this.destMask = ~ipToLong(destMask);
            this.destIp = ipToLong(destIp) & this.destMask;

        }

        onePort = false;
        rangeOfPorts = false;

    }


    /**
     * Contructor for one port
     * @param action
     * @param sourceIp
     * @param destIp
     * @param sourceMask
     * @param destMask
     * @param port
     */
    public ExtendedRule (String action, String sourceIp, String sourceMask, String destIp, String destMask, String port){
        
        // checks what the action is and saves it accordingly
        if (action.equals("permit")){

            this.action = "permitted";

        } 
        else {

            this.action = "denied";

        }

        // checks if source is any
        if (sourceIp.equals("any")) {

            this.sourceIp = 0;
            this.sourceMask = 0;

        } 
        else {

            this.sourceIp = ipToLong(sourceIp);
            this.sourceMask = ~ipToLong(sourceMask);

        }

        // checks if dest is any
        if (destIp.equals("any")) {

            this.destIp = 0;
            this.destMask = 0;

        } 
        else {

            this.destIp = ipToLong(destIp);
            this.destMask = ~ipToLong(destMask);

        }

        startPort = Long.parseLong(port);

        onePort = true;
        rangeOfPorts = false;

    }

    /**
     * Contructor for a range of ports
     * @param action
     * @param sourceIp
     * @param destIp
     * @param sourceMask
     * @param destMask
     * @param startPort
     * @param endPort
     */
    public ExtendedRule (String action, String sourceIp, String sourceMask, String destIp, String destMask, String startPort, String endPort){
        
        // checks what the action is and saves it accordingly
        if (action.equals("permit")){

            this.action = "permitted";

        } 
        else {

            this.action = "denied";

        }

        // checks if source is any
        if (sourceIp.equals("any")) {

            this.sourceIp = 0;
            this.sourceMask = 0;

        } 
        else {

            this.sourceIp = ipToLong(sourceIp);
            this.sourceMask = ~ipToLong(sourceMask);

        }

        // checks if dest is any
        if (destIp.equals("any")) {

            this.destIp = 0;
            this.destMask = 0;

        } 
        else {

            this.destIp = ipToLong(destIp);
            this.destMask = ~ipToLong(destMask);

        }

        this.startPort = Long.parseLong(startPort);
        this.endPort = Long.parseLong(endPort);

        onePort = false;
        rangeOfPorts = true;

    }

    // getter
    public String getAction () {
        return action;
    }

    /**
     * Method for checking if the packet matches the rule
     * @param sourceIpAddress - source ip being checked
     * @param destIpAddress - dest ip being checked
     * @param port - port being checked
     * @return - returns a boolean if it matches or not
     */
    public boolean ipMatch (long sourceIpAddress, long destIpAddress, long port) {

        long ruleSourceIp = sourceIp & sourceMask;
        long checkedSourceIp = sourceIpAddress & sourceMask;

        long ruleDestIp = destIp & destMask;
        long checkedDestIp = destIpAddress & destMask;
        
        boolean portMatched = true;

        if (onePort) {

            portMatched = startPort == port;

        } 
        else if (rangeOfPorts) {

            portMatched = (startPort <= port && port <= endPort);

        }

        return (ruleSourceIp == checkedSourceIp && ruleDestIp == checkedDestIp && portMatched);

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
