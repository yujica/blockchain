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
    List<Map<String, Object>> chain;
    List<Map<String, Object>> transaction_pool;
    Map<String, Object> block;
    Map<String, Object> transaction;
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

    public void add_transaction(String sender_blockchain_address, String recipient_blockchain_address, double value){
        this.transaction = new HashMap<String, Object>();
        transaction.put("sender_blockchain_address", sender_blockchain_address);
        transaction.put("recipient_blockchain_address", recipient_blockchain_address);
        transaction.put("value", value);
        this.transaction_pool.add(transaction);
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T autoCast(Object obj) {
        T castObj = (T) obj;
        return castObj;
    }

    public void pprint(List<Map<String, Object>> blocklist){
        int index = 0;
        for (Map<String, Object> blockmap : blocklist)
        {
            System.out.println("==================== Chain " + index + " ====================");
            System.out.println(" nonce            => " + blockmap.get("nonce"));
            System.out.println(" previous_hash    => " + blockmap.get("previous_hash"));
            System.out.println(" timestamp        => " + blockmap.get("timestamp"));
            int index_trans = 0;
            List<Map<String, Object>> transactions_list = autoCast(blockmap.get("transactions"));
            for (Map<String, Object> transactions_map : transactions_list) {
            	    System.out.println("   ##### transactions " + index_trans + " #####");
                	System.out.println("   sender_blockchain_address    => " + transactions_map.get("sender_blockchain_address"));
                	System.out.println("   recipient_blockchain_address => " + transactions_map.get("recipient_blockchain_address"));
                	System.out.println("   value  => " + transactions_map.get("value"));
                	index_trans++;
            }
            index++;
        }
        System.out.println("");
        System.out.println("*********************************************");
    }

}

public class Transaction {

    public static void main(String[] args){

        Logger logger = Logger.getLogger(Transaction.class.getName());
        logger.setLevel(Level.INFO);
        
        Map<String, Object> block_map = new HashMap<String, Object>();
        List<Map<String, Object>> chain_list = new ArrayList<Map<String, Object>>();
        String previous_hash = "";
        
        BlockChain block_chain = new BlockChain();
        block_chain.pprint(block_chain.chain);
        
        block_chain.add_transaction("Tom","Bob", 1.0);
        previous_hash = block_chain.hash((block_chain.chain.size()>0) ? block_chain.chain.get(block_chain.chain.size()-1) : null);
        block_chain.create_block(5, previous_hash);
        block_chain.pprint(block_chain.chain);
        
        block_chain.add_transaction("Carol","Daisy", 2.0);
        block_chain.add_transaction("Xmen","Yuta", 3.0);
        previous_hash = block_chain.hash((block_chain.chain.size()>0) ? block_chain.chain.get(block_chain.chain.size()-1) : null);
        block_chain.create_block(2, previous_hash);
        block_chain.pprint(block_chain.chain);
    }

}
