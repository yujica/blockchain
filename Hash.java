import java.io.IOException;
import java.security.MessageDigest;
import java.time.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringJoiner;
import java.util.stream.Stream;
import java.util.stream.Collectors;

class BlockChain{
    List<String> transaction_pool;
    List<Map<String, Object>> chain;
    Map<String, Object> block;
    String hash;

    public BlockChain(){
        this.transaction_pool = new ArrayList<>();
        this.chain = new ArrayList<Map<String, Object>>();
        create_block(0, "init hash");
    }
    
    public void create_block(int nonce, String previous_hash){

        this.block = new HashMap<String, Object>();
        long currentTimestamp = System.currentTimeMillis();
        block.put("timestamp", currentTimestamp);
        block.put("transactions", this.transaction_pool);
        block.put("nonce", nonce);
        block.put("previous_hash", previous_hash);
        
        this.chain.add(block);
        this.transaction_pool = new ArrayList<>();
    }

    public String hash(Map<String, Object> unsorted_block){
        StringBuilder sha256 = new StringBuilder();
        String sorted_block_str;
        StringJoiner sj = new StringJoiner("");
        unsorted_block.entrySet().stream()
            .sorted(Entry.comparingByKey())
             .forEach(i -> sj.add(String.valueOf(i)));
        sorted_block_str = sj.toString();
        
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] result = digest.digest(sorted_block_str.getBytes());
            for (byte b : result) {
                sha256.append(String.format("%02x", b));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return sha256.toString();
    }

    public void pprint(List<Map<String, Object>> blocklist){
        int index = 0;
        for (Map<String, Object> blockmap : blocklist)
        {
            System.out.println("==================== Chain " + index + " ====================");
            System.out.println(" nonce            => " + blockmap.get("nonce"));
            System.out.println(" previous_hash    => " + blockmap.get("previous_hash"));
            System.out.println(" timestamp        => " + blockmap.get("timestamp"));
            System.out.println(" transactions     => " + blockmap.get("transactions"));
            index++;
        }
        System.out.println("");
        System.out.println("*********************************************");
    }


}

public class Hash {

    public static void main(String[] args){

        Logger logger = Logger.getLogger(Hash.class.getName());
        logger.setLevel(Level.INFO);
        
        String previous_hash = "";
        
        BlockChain block_chain = new BlockChain();
        block_chain.pprint(block_chain.chain);
        
        previous_hash = block_chain.hash((block_chain.chain.size()>0) ? block_chain.chain.get(block_chain.chain.size()-1) : null);
        block_chain.create_block(5, previous_hash);
        block_chain.pprint(block_chain.chain);

        previous_hash = block_chain.hash((block_chain.chain.size()>0) ? block_chain.chain.get(block_chain.chain.size()-1) : null);
        block_chain.create_block(2, previous_hash);
        block_chain.pprint(block_chain.chain);
    }

}
