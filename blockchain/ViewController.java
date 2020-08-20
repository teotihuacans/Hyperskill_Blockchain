package blockchain;

public class ViewController {

    public static void print(Block input, Integer NN) {
        System.out.println("Block:");
        System.out.println("Created by: miner" + input.getMinerId());
        System.out.println("miner" + input.getMinerId() + " gets " + 100 + " VC");
        System.out.println("Id: " + input.getId());
        System.out.println("Timestamp: " + input.getTimeStamp());
        System.out.println("Magic number: " + input.getMagicNum());
        System.out.println("Hash of the previous block: \n" + input.getPrevHash());
        System.out.println("Hash of the block: \n" + input.getCurrentHash());
        System.out.println("Block data: ");
        if (input.getBlockData().size() > 0) {
            input.getBlockData().forEach(m -> System.out.println(m.getText()));
        } else {
            System.out.println("No transactions");
        }
        System.out.println("Block was generating for " + String.format("%.3f", input.getGenTime() / 1000.00) + " seconds");

        Integer prevN = 0;
        for (int l = 0; l < input.getCurrentHash().length(); l++) {
            if (input.getCurrentHash().charAt(l) == '0') {
                prevN++;
            } else {
                break;
            }
        }

        if (NN > prevN) {
            System.out.println("N was increased to " + NN);
        } else if (NN < prevN) {
            System.out.println("N was decreased by " + NN);
        } else {
            System.out.println("N stays the same");
        }
        System.out.println("");
    }

    public static void printNNL(String input) {
        System.out.print(input);
    }
}
