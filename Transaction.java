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
    List<Map<String, Object>> transaction_pool;
    List<Map<String, Object>> chain;
    Map<String, Object> block;
    String hash;
    Map<String, Object> transaction;

    public BlockChain(){
        this.transaction_pool = new ArrayList<>();
        this.chain = new ArrayList<Map<String, Object>>();
        create_block("0", "init hash");
    }
    
    public Map<String, Object> create_block(String nonce, String previous_hash){

        this.block = new HashMap<String, Object>();
        long currentTimestamp = System.currentTimeMillis();
        block.put("timestamp", currentTimestamp);
        block.put("transactions", this.transaction_pool);
        block.put("nonce", nonce);
        block.put("previous_hash", previous_hash);
        
        this.chain.add(block);
        this.transaction_pool = new ArrayList<>();

        return block;
    }

    public String hash(Map<String, Object> unsorted_block){
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

    public boolean add_transaction(String sender_blockchain_address, String recipient_blockchain_address, double value){
        this.transaction = new HashMap<String, Object>();
        transaction.put("sender_blockchain_address", sender_blockchain_address);
        transaction.put("recipient_blockchain_address", recipient_blockchain_address);
        transaction.put("value", value);
        this.transaction_pool.add(transaction);
        return true;
    }

    public void pprint(List<Map<String, Object>> blocklist){
        int index = 0;
        for (Map<String, Object> blockmap : blocklist)
        {
            System.out.println("==================== Chain " + index + " ====================");
            System.out.println("nonce         => " + blockmap.get("nonce"));
            System.out.println("previous_hash => " + blockmap.get("previous_hash"));
            System.out.println("timestamp     => " + blockmap.get("timestamp"));
            System.out.println("transactions  => " + blockmap.get("transactions"));
            index++;
        }
        System.out.println("");
        System.out.println("*********************************************");
    }


def pprint(chains):
    for i, chain in enumerate(chains):
        print(f'{"="*25} Chain {i} {"="*25}')
        for k, v in chain.items():
            if k == 'transactions':
                print(k)
                for d in v:
                    print(f'{"-"*40}')
                    for kk, vv in d.items():
                        print(f' {kk:30}{vv}')
            else:
                print(f'{k:15}{v}')
    print(f'{"*"*25}')

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
        block_map = block_chain.create_block("5", previous_hash);
        block_chain.pprint(block_chain.chain);
        
        block_chain.add_transaction("Carol","Daisy", 2.0);
        block_chain.add_transaction("Xmen","Yuta", 3.0);
        previous_hash = block_chain.hash((block_chain.chain.size()>0) ? block_chain.chain.get(block_chain.chain.size()-1) : null);
        block_map = block_chain.create_block("2", previous_hash);
        block_chain.pprint(block_chain.chain);
    }

}
