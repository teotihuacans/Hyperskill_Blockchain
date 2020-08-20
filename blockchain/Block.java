package blockchain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Block implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long minerId;
    private Integer id;
    private Long timeStamp;
    private String prevHash;
    private Integer magicNum;
    private String currentHash;
    private List<Message> blockData;
    private Long genTime;

    public Block(Long minerId, Integer id, Long timeStamp, String prevHash, Integer magicNum, String currentHash,
                 List<Message> blockData, Long genTime) {
        this.minerId = minerId;
        this.id = id;
        this.timeStamp = timeStamp;
        this.prevHash = prevHash;
        this.magicNum = magicNum;
        this.currentHash = currentHash;
        this.blockData = new ArrayList<>();
        this.blockData.addAll(blockData);
        this.genTime = genTime;
    }

    public Long getMinerId() { return minerId; }

    public Integer getId() {
        return id;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public String getPrevHash() {
        return prevHash;
    }

    public Integer getMagicNum() {
        return magicNum;
    }

    public String getCurrentHash() {
        return currentHash;
    }

    public List<Message> getBlockData() {
        return blockData;
    }

    public Long getGenTime() { return genTime; }
}
