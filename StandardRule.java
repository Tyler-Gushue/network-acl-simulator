public class StandardRule {

    private String action;
    private long sourceIp;
    private long mask;
    
    /**
     * Contructor for Standard Rules of an ACL
     * @param action - action being deny or permit
     * @param sourceIp - the source ip of the packet
     * @param mask - the mask to know what matters in the source ip
     */
    public StandardRule (String action, String sourceIp, String mask){
        
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
            this.mask = 0;

        }
        else {

            this.sourceIp = ipToLong(sourceIp);
            this.mask = ~ipToLong(mask);

        }

    }

    // getter for action
    public String getAction () {
        return action;
    }

    /**
     * Method for checking if the ip matches the rule
     * @param ipAddress - address being checked
     * @return - returns a boolean if it matches or not
     */
    public boolean ipMatch (long ipAddress) {

        long savedIp = sourceIp & mask;
        long checkedIp = ipAddress & mask;

        return (savedIp == checkedIp);

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
