import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.time.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.*;


public class Block {
    
    public static void main(String[] args){
        
        Logger logger = Logger.getLogger(Test.class.getName());
        logger.setLevel(Level.INFO);
        
        BlockChain block_chain = new BlockChain();
        //Map<String, Object> chain = new HashMap<String, Object>();
        List<Map<String, Object>> chain = new ArrayList<Map<String, Object>>();

        chain = block_chain.create_block("5", "hash 1");
        pprint(chain);
        
        chain = block_chain.create_block("2", "hash 2");
        pprint(chain);

    }

    /*
    static void pprint(Map blockmap){
        System.out.println("====================");
        System.out.println("timestamp     => " + blockmap.get("timestamp"));
        System.out.println("transactions  => " + blockmap.get("transactions"));
        System.out.println("nonce         => " + blockmap.get("nonce"));
        System.out.println("previous_hash => " + blockmap.get("previous_hash"));
    }*/
    
    static void pprint(List<Map<String, Object>> blocklist){
        int index = 0;
        for (Map<String, Object> blockmap : blocklist)
        {
            System.out.println("==================== Chain " + index + " ====================");
            System.out.println("timestamp     => " + blockmap.get("timestamp"));
            System.out.println("transactions  => " + blockmap.get("transactions"));
            System.out.println("nonce         => " + blockmap.get("nonce"));
            System.out.println("previous_hash => " + blockmap.get("previous_hash"));
            index++;
        }
        System.out.println("");
        System.out.println("*********************************************");
    }
}

class BlockChain{
    List<String> transaction_pool;
    List<Map<String, Object>> chain;

    public BlockChain(){
        this.transaction_pool = new ArrayList<>();
        this.chain = new ArrayList<Map<String, Object>>();
        create_block("0", "init hash");
    }

    //public Map<String, Object> create_block(String nonce, String previous_hash){
    public List<Map<String, Object>> create_block(String nonce, String previous_hash){
 
        Map<String, Object> block = new HashMap<String, Object>();
        long currentTimestamp = System.currentTimeMillis();
        block.put("timestamp", currentTimestamp);
        block.put("transactions", this.transaction_pool);
        block.put("nonce", nonce);
        block.put("previous_hash", previous_hash);

        this.chain.add(block);
        this.transaction_pool = new ArrayList<>();

        //return block;
        return chain;
    }
}


