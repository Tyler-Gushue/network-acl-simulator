import java.util.*;

import java.io.*;

public class ExtendedACLMain {
    public static void main(String[] args) {

        File aclRules = new File("extended_rules3.txt");
        File input = new File("extended_input3.txt");

        ArrayList<ExtendedRule> ruleList = new ArrayList<>();
        ArrayList<Packet> packetList = new ArrayList<>();

        // Saving acl rules
        try (Scanner myReader = new Scanner(aclRules)) {

            while (myReader.hasNextLine()) {

                String line = myReader.nextLine();
                String[] aclParts = line.split("\\s+");

                // checks if the line is a rule
                if (aclParts[0].equals("access-list")) {

                    // checks if packet contains two anys
                    if (line.contains("any any")){

                        ruleList.add(new ExtendedRule(aclParts[2], "any", "", "any", ""));

                    } 

                    // checks if the line contains any
                    else if (line.contains("any")) {

                        // checks if source ip is any
                        if (aclParts[4].equals("any")) {

                            // checks if line contains a port range
                            if (line.contains("range")) {

                                String[] ports = aclParts[8].split("\\-");
                                ruleList.add(new ExtendedRule(aclParts[2], "any", "", aclParts[5], aclParts[6], ports[0], ports[1]));

                            }

                            // checks if line contains a port
                            else if (line.contains("eq")) {

                                ruleList.add(new ExtendedRule(aclParts[2], "any", "", aclParts[5], aclParts[6], aclParts[8]));

                            }

                            // line doesn't contain a port
                            else {

                                ruleList.add(new ExtendedRule(aclParts[2], "any", "", aclParts[5], aclParts[6]));

                            }

                        } 

                        // dest ip is any
                        else {

                            // checks if line contains a port range
                            if (line.contains("range")) {

                                String[] ports = aclParts[8].split("\\-");
                                ruleList.add(new ExtendedRule(aclParts[2], aclParts[4], aclParts[5], "any", "", ports[0], ports[1]));

                            }

                            // checks if line contains a port
                            else if (line.contains("eq")) {

                                ruleList.add(new ExtendedRule(aclParts[2], aclParts[4], aclParts[5], "any", "", aclParts[8]));

                            }

                            // line doesn't contain a port
                            else {

                                ruleList.add(new ExtendedRule(aclParts[2], aclParts[4], aclParts[5], "any", ""));

                            }

                        }

                    } 

                    // check if line contains a port range
                    else if (line.contains("range")) {

                        String[] ports = aclParts[9].split("\\-");
                        ruleList.add(new ExtendedRule(aclParts[2], aclParts[4], aclParts[5], aclParts[6], aclParts[7], ports[0], ports[1]));

                    } 

                    // check if line contains a port
                    else if (line.contains("eq")) {

                        ruleList.add(new ExtendedRule(aclParts[2], aclParts[4], aclParts[5], aclParts[6], aclParts[7], aclParts[9]));

                    } 

                    // line doesn't contain a port
                    else {

                        ruleList.add(new ExtendedRule(aclParts[2], aclParts[4], aclParts[5], aclParts[6], aclParts[7]));

                    }

                }

            }

        } catch (FileNotFoundException e) {

            System.out.println("An error occurred.");
            e.printStackTrace();

        }

        // saving packets
        try (Scanner myReader = new Scanner(input)) {

            while (myReader.hasNextLine()) {

                String line = myReader.nextLine();
                String[] packetParts = line.split("\\s+");

                // checks if packet has a port
                if (packetParts.length == 3) {

                    packetList.add(new Packet(packetParts[0], packetParts[1], packetParts[2]));

                } 

                // packet doesn't have a port
                else {

                    packetList.add(new Packet(packetParts[0], packetParts[1]));

                }

            }

        } catch (FileNotFoundException e) {

            System.out.println("An error occurred.");
            e.printStackTrace();

        }

        // Checks packets against rules
        for (Packet packet : packetList) {

            boolean match = false;

            for (ExtendedRule rule : ruleList) {

                // checks if packet matches a rule
                if (rule.ipMatch(packet.getSourceIp(), packet.getDestIp(), packet.getPort())) {

                    // checks if packet has a port to print an appropriate message
                    if (packet.getPort() == 0) {

                        System.out.println("Packet from " + packet.getSourceIpString() + " to " + packet.getDestIpString() + " " + rule.getAction());
                        match = true;
                        break;

                    } 
                    
                    // packet has a port
                    else {

                        System.out.println("Packet from " + packet.getSourceIpString() + " to " + packet.getDestIpString() + " on port " + packet.getPort() + " " + rule.getAction());
                        match = true;
                        break;

                    }

                }

            }

            // if the packet matches no rules then denied
            if (!match) {

                // checks if packet has a port to print an appropriate message
                if (packet.getPort() == 0) {

                    System.out.println("Packet from " + packet.getSourceIpString() + " to " + packet.getDestIpString() + " denied");
                    match = true;

                } 
                
                // packet has a port
                else {

                    System.out.println("Packet from " + packet.getSourceIpString() + " to " + packet.getDestIpString() + " on port " + packet.getPort() + " denied");
                    match = true;

                }

            }

        }

    }
    
}