package blockchain;

import java.util.List;
import java.util.stream.Collectors;

public class BlockChainUtils {
    public static boolean validateBlockChain(List<Block> input) {
        /*for (Block vS : input) {
            String compare = StringUtil.applySha256(vS.getId() + vS.getTimeStamp() + vS.getPrevHash() +
                    vS.getBlockData() + vS.getMagicNum());
            if (!compare.equals(vS.getCurrentHash())) {
                return false;
            }
        }
        return true;*/
        return input.stream().allMatch(b -> StringUtil.applySha256(b.getId() + b.getTimeStamp() + b.getPrevHash() +
                b.getBlockData() + b.getMagicNum()).equals(b.getCurrentHash()));
    }

    public static boolean validateBlock(Block vS) {
        String compare = StringUtil.applySha256(vS.getId() + vS.getTimeStamp() + vS.getPrevHash() +
                vS.getBlockData() + vS.getMagicNum());

        return compare.equals(vS.getCurrentHash());
    }

    public static Long allTransactionsCount(List<Block> blockChain, String fromName) {
        /*blockChain.stream().flatMap(b -> b.getBlockData().stream())
                .forEach(m -> System.out.print(m.getMessageId() + "_" + m.getFromName() + "=" + m.getSentAmount() + " "));*/
        return blockChain.stream().flatMapToLong(b ->
                b.getBlockData().stream().filter(m -> fromName.equals(m.getFromName())).mapToLong(Message::getSentAmount))
                .sum();
    }

    public static Long balanceCount(List<Block> blockChain, String name) {

        return blockChain.stream().flatMapToLong(b ->
                b.getBlockData().stream().filter(m -> name.equals(m.getToName())).mapToLong(Message::getSentAmount))
                .sum() - allTransactionsCount(blockChain, name);
    }
}
