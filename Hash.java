import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.time.*;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.util.StringJoiner;
import java.math.BigInteger;
import java.security.MessageDigest;

class BlockChain{
    List<String> transaction_pool;
    List<Map<String, Object>> chain;
    Map<String, Object> block;
    String hash;

    public BlockChain(){
        this.transaction_pool = new ArrayList<>();
        this.chain = new ArrayList<Map<String, Object>>();
        create_block("0", "init hash");
    }
    
    public Map<String, Object> create_block(String nonce, String previous_hash){
    //public List<Map<String, Object>> create_block(String nonce, String previous_hash){

        this.block = new HashMap<String, Object>();
        long currentTimestamp = System.currentTimeMillis();
        block.put("timestamp", currentTimestamp);
        block.put("transactions", this.transaction_pool);
        block.put("nonce", nonce);
        block.put("previous_hash", previous_hash);
        

        //block = block_sort(block);
        //block_str = block_string(block);
        this.chain.add(block);
        this.transaction_pool = new ArrayList<>();

        return block;
        //return chain;
    }

    
  /*  
    public Map<String, Object> block_sort(Map<String, Object> unsorted_block){
        //unsorted_block.entrySet().stream()
        //    .sorted(Entry.comparingByKey())
        //     .forEach(System.out::println);
        Map<String, Object> sorted_block = unsorted_block.entrySet().stream()
                                             .sorted(Entry.comparingByKey())
                                             .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
        return sorted_block;
    }
*/
/*
    public String block_string(Map<String, Object> unsorted_block){
        String block_str;
        StringJoiner sj = new StringJoiner("");
        unsorted_block.entrySet().stream()
            .sorted(Entry.comparingByKey())
             .forEach(i -> sj.add(String.valueOf(i)));
        block_str = sj.toString();
        System.out.println("block_str:" + block_str);
        return block_str;
    }*/

    public String hash(Map<String, Object> unsorted_block){
        //sorted_block = json.dumps(this.block, sort_keys=True)
        //return hashlib.sha256(sorted_block.encode()).hexdigest()
        String sha256 = "";
        String sorted_block_str;
        StringJoiner sj = new StringJoiner("");
        unsorted_block.entrySet().stream()
            .sorted(Entry.comparingByKey())
             .forEach(i -> sj.add(String.valueOf(i)));
        sorted_block_str = sj.toString();
        
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] result = digest.digest(sorted_block_str.getBytes());
            sha256 = String.format("%040x", new BigInteger(1, result));
        } catch (Exception e){
            e.printStackTrace();
        }
        return sha256;
    }

    public void pprint(List<Map<String, Object>> blocklist){
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

public class Hash {

    public static void main(String[] args){

        Logger logger = Logger.getLogger(Hash.class.getName());
        logger.setLevel(Level.INFO);
        
        Map<String, Object> block_map = new HashMap<String, Object>();
        List<Map<String, Object>> chain_list = new ArrayList<Map<String, Object>>();
        String previous_hash = "";
        
        BlockChain block_chain = new BlockChain();
        block_chain.pprint(block_chain.chain);
        
        //chain_list = block_chain.create_block("5", previous_hash);
        //block_chain.pprint(chain_list);
        previous_hash = block_chain.hash((block_chain.chain.size()>0) ? block_chain.chain.get(block_chain.chain.size()-1) : null);
        block_map = block_chain.create_block("5", previous_hash);
        block_chain.pprint(block_chain.chain);

        //chain_list = block_chain.create_block("2", previous_hash);
        //block_chain.pprint(chain_list);
        previous_hash = block_chain.hash((block_chain.chain.size()>0) ? block_chain.chain.get(block_chain.chain.size()-1) : null);
        block_map = block_chain.create_block("2", previous_hash);
        block_chain.pprint(block_chain.chain);


    }

}
