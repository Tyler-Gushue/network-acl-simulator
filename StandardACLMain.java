import java.util.*;
import java.io.*;

public class StandardACLMain {
    public static void main(String[] args) {

        File aclRules = new File("standard_rules3.txt");
        File input = new File("standard_input3.txt");

        ArrayList<StandardRule> ruleList = new ArrayList<>();
        ArrayList<Packet> packetList = new ArrayList<>();

        // Saving acl rules
        try (Scanner myReader = new Scanner(aclRules)) {

            while (myReader.hasNextLine()) {

                String line = myReader.nextLine();
                String[] aclParts = line.split("\\s+");

                // checks to see if the line is a rule
                if (aclParts[0].equals("access-list")) {

                    // checks to see if the source is any
                    if (line.contains("any")) {

                        ruleList.add(new StandardRule(aclParts[2], "any", ""));

                    }
                    else {

                        ruleList.add(new StandardRule(aclParts[2], aclParts[3], aclParts[4]));

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

                String packet = myReader.nextLine();
                packetList.add(new Packet(packet));

            }

        } catch (FileNotFoundException e) {

            System.out.println("An error occurred.");
            e.printStackTrace();

        }

        // Checks packets against rules
        for (Packet packet : packetList) {

            boolean match = false;

            for (StandardRule rule : ruleList) {

                // checks to see if the packet matches a rule
                if (rule.ipMatch(packet.getSourceIp())) {

                    System.out.println("Packet from " + packet.getSourceIpString() + " " + rule.getAction());
                    match = true;
                    break;

                }

            }

            // if the packet matches no rules then denied
            if (!match) {

                System.out.println("Packet from " + packet.getSourceIpString() + " denied");

            }

        }

    }
    
}