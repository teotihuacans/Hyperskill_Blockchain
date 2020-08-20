package blockchain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class BlockCreator implements Callable {
    StringBuilder sB;
    StringBuilder sZ = new StringBuilder();
    Random random = new Random();
    Integer magicNum;
    Integer blockId;
    Long timeStamp;
    String prevBlockHash;
    List<Message> blockData;
    Integer NN;
    Long UId;
    byte[] privateKey;
    Long reward;

    public BlockCreator(Integer blockId, Long timeStamp, String prevBlockHash, List<Message> blockData, Integer NN,
                        Long UId, byte[] privateKey, Long reward) {
        this.blockId = blockId;
        this.timeStamp = timeStamp;
        this.prevBlockHash = prevBlockHash;
        this.blockData = new ArrayList<>();
        this.blockData.addAll(blockData);
        this.NN = NN;
        for (int i = 0; i < NN; i++) {
            sZ.append("0");
        }
        this.UId = UId;
        this.privateKey = privateKey;
        this.reward = reward;
    }

    @Override
    public Block call() {
        try {
            blockData.add(new Message(UId, 100L, "BlockChain", "miner" + Thread.currentThread().getId(), privateKey).call());
        } catch (Exception e) {
            e.printStackTrace();
        }
        do {
            magicNum = random.nextInt(Integer.MAX_VALUE);
            sB = new StringBuilder(StringUtil.applySha256(blockId + timeStamp + prevBlockHash + blockData + magicNum));
        } while (!sB.subSequence(0, NN).equals(sZ.toString()));

        return new Block(Thread.currentThread().getId(), blockId, timeStamp, prevBlockHash,
                        magicNum, sB.toString(), blockData, new Date().getTime() - timeStamp);
    }

}
