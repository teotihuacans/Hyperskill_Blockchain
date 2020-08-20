package blockchain;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.*;
import java.util.concurrent.*;

public class Main implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final double MINMINERLIMIT = 0.1;
    private static final double MAXMINERLIMIT = 0.5;
    private static volatile List<Block> mainLst;
    private static transient volatile int NN = 0;
    private static final transient int N = 15;
    private static final transient List<String> msgs = List.of("Shop1", "Shop2", "Shop3", "Shop4");
    private static volatile Long MID = 0L;
    private static transient byte[] publicKey;
    private static transient byte[] privateKey;
    private static volatile transient List<Message> chat = new ArrayList<>();
    private static transient Long REWARD = 100L;

    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException {
        /*Scanner in = new Scanner(System.in);
        ViewController.printNNL("Enter how many zeros the hash must start with: ");
        NN = in.nextInt();*/

        GenerateKeys myKeys = new GenerateKeys(1024);
        myKeys.createKeys();
        publicKey =  myKeys.getPublicKey().getEncoded();
        privateKey = myKeys.getPrivateKey().getEncoded();

        String filename = "BlockchainNew.data";
        File bchStore = new File(filename);
        mainLst = new ArrayList<>(N); //Id, timeStamp, prevHash, currentHash, magicNum

        try {
            if (bchStore.exists()) {
                List<Block> mainLstRecovered = (List<Block>) SerializationUtils.deserialize(filename);
                //System.out.println("BlockChain validation result: " + validateBlockChain(mainLstRecovered));
                //System.out.println(mainLstRecovered);
                mainLst.addAll(mainLstRecovered);
            }

            SerializationUtils.serialize(blockChainCreate(), filename);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        //System.out.println(validateBlockChain(mainLst));
    }

    public static List<Block> blockChainCreate() {
        Integer magicNum;
        int j = mainLst.size();
        MID += mainLst.stream().flatMapToLong(b -> b.getBlockData().stream().mapToLong(Message::getMessageId))
                .max().orElse(0L);

        int poolSize = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(poolSize);
        ExecutorService executorMsg = Executors.newFixedThreadPool(poolSize);
        Random sumSend = new Random();

        for (Integer i = 1 + j; i <= N + j; i++) {
            Long timeStamp = new Date().getTime();
            String prevHashTmp;


            if (i == 1) {
                prevHashTmp = "0";
            } else {
                prevHashTmp = mainLst.get(i - 2).getCurrentHash();
            }

            //List<Future<Block>> mtLst = new ArrayList<>();
            //Callable<Block> blockCall = new BlockCreator(i, timeStamp, prevHashTmp, NN);
            Collection blockCallList = new ArrayList<>();
            Collection msgCallList = new ArrayList<>();
            Long tempUId = ++MID;

                for (int h = 1; h <= poolSize; h++) {
                    blockCallList.add(new BlockCreator(i, timeStamp, prevHashTmp, chat, NN, tempUId, privateKey, REWARD));
                }
            //BlockChainUtils.balanceCount(mainLst, "");
            msgs.forEach(n -> msgCallList.add(new Message(++MID, Long.parseLong("" + sumSend.nextInt(100)),
                    "", n, privateKey)));

            try {
                Object tempBlockListFirst = executor.invokeAny(blockCallList);
                List<Future<Message>> tempMsgList = executorMsg.invokeAll(msgCallList);

                if (BlockChainUtils.validateBlock((Block) tempBlockListFirst)) {
                    mainLst.add((Block) tempBlockListFirst);
                }

                chat.clear();
                for (Future<Message> m : tempMsgList) {
                    chat.add(m.get());
                }

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            if ((mainLst.get(i - 1).getGenTime()) / 1000.0 < MINMINERLIMIT) { NN++; }
            else if ((mainLst.get(i - 1).getGenTime()) / 1000.0 > MAXMINERLIMIT) { NN--;}

                ViewController.print(mainLst.get(i - 1), NN);
        }

        executor.shutdownNow();
        executorMsg.shutdownNow();
        try {
            executor.awaitTermination(100, TimeUnit.MICROSECONDS);
            executorMsg.awaitTermination(100, TimeUnit.MICROSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //System.out.println("\n all sent = " + BlockChainUtils.allTransactionsCount(mainLst, "miner19"));
        //System.out.println("\n balance = " + BlockChainUtils.balanceCount(mainLst, "miner19"));

        return mainLst;
    }

}
