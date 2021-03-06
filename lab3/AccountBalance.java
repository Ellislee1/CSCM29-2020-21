import java.security.PublicKey;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.InvalidKeyException;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;


/** 
 *   AccountBalance defines an accountBalance in the ledger model of bitcoins
 */

public class AccountBalance {

    /** 
     * The current accountBalance, with each account's public Key mapped to its 
     *    account balance.
     */
    
    private Hashtable<PublicKey, Integer> accountBalanceBase;

    /**
     *  In order to print out the accountBalance in a good order
     *  we maintain a list of public Keys,
     *  which will be the set of public keys maped by it in the order
     *  they were added
     **/

    private ArrayList<PublicKey> publicKeyList;


    /** 
     * Creates a new accountBalance
     */
    public AccountBalance() {
	accountBalanceBase = new Hashtable<PublicKey, Integer>();
	publicKeyList = new ArrayList<PublicKey>();
	
    }

    /** 
     * Creates a new accountBalance from a map from string to integers
     */
    
    public AccountBalance(Hashtable<PublicKey, Integer> accountBalanceBase) {
	this.accountBalanceBase = accountBalanceBase;
	publicKeyList = new ArrayList<PublicKey>();	
	for (PublicKey pbk : accountBalanceBase.keySet()){
	    publicKeyList.add(pbk);
	}
    }

    /** obtain the underlying Hashtable from string to integers
     */   
    
    public Hashtable<PublicKey,Integer> getAccountBalanceBase(){
	return accountBalanceBase;
    };

    /** 
      * obtain the list of publicKeys in the tree map
      */   
    
    public Set<PublicKey> getPublicKeys(){
	return getAccountBalanceBase().keySet();
    };

    /** 
      * obtain the list of publicKeys in the order they were added
      */   

    public ArrayList<PublicKey> getPublicKeysOrdered(){
	return publicKeyList;
    };        

    
    

    /** 
     * Adds a mapping from new account's name {@code publicKey} to its 
     * account balance {@code balance} into the accountBalance. 
     *
     * if there was an entry it is overridden.  
     */

    public void addAccount(PublicKey publicKey, int balance) {
	accountBalanceBase.put(publicKey, balance);
	if (! publicKeyList.contains(publicKey)){
	    publicKeyList.add(publicKey);
	}
    }

    /** 
     * @return true if the {@code publicKey} exists in the accountBalance.
     */
    
    public boolean hasPublicKey(PublicKey publicKey) {
	return accountBalanceBase.containsKey(publicKey);
    }


    /** 
     * @return the balance for this account {@code account}
     *
     *  if there was no entry, return zero
     *
     */
    
    public int getBalance(PublicKey publicKey) {
	if (hasPublicKey(publicKey)){
		return accountBalanceBase.get(publicKey);
	    } else
	    {  return 0;
	    }
    }


    /** 
     * set the balance for {@code publicKey} to {@code amount}
     */

    
    public void setBalance(PublicKey publicKey, int amount){
	accountBalanceBase.put(publicKey,amount);
	if (! publicKeyList.contains(publicKey)){
	    publicKeyList.add(publicKey);
	}	
	    };
	

    /** 
     * Imcrements Adds amount to balance for {@code publicKey}
     * 
     *  if there was no entry for {@code publicKey} add one with 
     *       {@code balance}
     */
    
    public void addToBalance(PublicKey publicKey, int amount) {
	setBalance(publicKey,getBalance(publicKey) + amount);
    }


    /** 
     * Subtracts amount from balance for {@code publicKey}
     */
    
    public void subtractFromBalance(PublicKey publicKey, int amount) {
	setBalance(publicKey,getBalance(publicKey) - amount);
    }


    /** 
     * Check balance has at least amount for {@code publicKey}
     */
    public boolean checkBalance(PublicKey publicKey, int amount) {
	return (getBalance(publicKey) >= amount);
    }


    /* checks whether an accountBalance can be deducted 
       this is an auxiliary function used to define checkTxInputListCanBeDeducted */

    public boolean checkAccountBalanceCanBeDeducted(AccountBalance accountBalance2){
	for (PublicKey publicKey : accountBalance2.getPublicKeys()) {
	    if (getBalance(publicKey) < accountBalance2.getBalance(publicKey))
		return false;
	};
	return true;
    };


    /** 
     *  Check that a list of publicKey amounts can be deducted from the 
     *     current accountBalance
     *
     *   done by first converting the list of publicKey amounts into an accountBalance
     *     and then checking that the resulting accountBalance can be deducted.
     *   
     */    


    public boolean checkTxInputListCanBeDeducted(TxInputList txInputList){
	return checkAccountBalanceCanBeDeducted(txInputList.toAccountBalance());
    };


    /** 
     * Subtract a list of TxInput from the accountBalance
     *
     *   requires that the list to be deducted is deductable.
     *   
     */    
    

    public void subtractTxInputList(TxInputList txInputList){
	for (TxInput entry : txInputList.toList()){
	    subtractFromBalance(entry.getSender(),entry.getAmount());
	}
    }


    
    /** 
     * Adds a list of txOutput of a transaction to the current accountBalance
     *
     */    

    public void addTxOutputList(TxOutputList txOutputList){
	for (TxOutput entry : txOutputList.toList()){
	    addToBalance(entry.getRecipient(),entry.getAmount());
	}
    }


    /** 
     *
     *  Task 4 Check a transaction is valid.
     *
     *  this means that 
     *    the sum of outputs is less than or equal to the sum of inputs
     *    all signatures are valid
     *    and the inputs can be deducted from the accountBalance.

     *    This method has been set to true so that the code compiles - that should
     *    be changed
     */    

    public boolean checkTransactionValid(Transaction tx){
	return true;
	/* this is not the correct value, only used here so that the code
	   compiles */
    };


    /** 
     * Process a transaction
     *    by first deducting all the inputs
     *    and then adding all the outputs.
     *
     */    
    
    public void processTransaction(Transaction tx){
	subtractTxInputList(tx.toTxInputs());
	addTxOutputList(tx.toTxOutputs());
    };

    
    /** 
     * Prints the current state of the accountBalance. 
     */

    public void print(PublicKeyMap pubKeyMap) {
	for (PublicKey publicKey : publicKeyList ) {
	    Integer value = getBalance(publicKey);
	    System.out.println("The balance for " +
			       pubKeyMap.getUser(publicKey) + " is " + value); 
	}

    }



    /** 
     * Testcase
     */

    public static void test()
	throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {

	Wallet exampleWallet = SampleWallet.generate(new String[]{ "Alice"});
	byte[] exampleMessage = KeyUtils.integer2ByteArray(1);
	byte[] exampleSignature = exampleWallet.signMessage(exampleMessage,"Alice");

	/***   Task 5
               add  to the test case the test as described in the lab sheet
                
               you can use the above exampleSignature, when a sample signature is needed
	       which cannot be computed from the data.

	**/

	    // =========================== CASE 1 ===========================
        // Create a sample wallet for alice, bob, carol and david
        System.out.println("\n========== Test 1 ==========\n");
	    Wallet aliceWallet = SampleWallet.generate(new String[] {"A1","A2"});
        Wallet bobWallet = SampleWallet.generate(new String[] {"B1","B2"});
        Wallet carolWallet = SampleWallet.generate(new String[] {"C1","C2","C3"});
        Wallet davidWallet = SampleWallet.generate(new String[] {"D1"});

        // =========================== CASE 2 ===========================
        // Create the public key map
        System.out.println("\n========== Test 2 ==========\n");
        PublicKeyMap keyMap = new PublicKeyMap();
        keyMap.addPublicKeyMap(aliceWallet.toPublicKeyMap());
        keyMap.addPublicKeyMap(bobWallet.toPublicKeyMap());
        keyMap.addPublicKeyMap(carolWallet.toPublicKeyMap());
        keyMap.addPublicKeyMap(davidWallet.toPublicKeyMap());

        System.out.println(keyMap.getUsers());

        // =========================== CASE 3 ===========================
        // Create empty Account Balance
        System.out.println("\n========== Test 3 ==========\n");

        AccountBalance accounts = new AccountBalance();

        for(String user:keyMap.getUsers()) {
            accounts.addAccount(keyMap.getPublicKey(user), 0);
        }

        System.out.println("A1: "+ accounts.getBalance(aliceWallet.getPublicKey("A1")));
        System.out.println("A2: "+ accounts.getBalance(aliceWallet.getPublicKey("A2")));
        System.out.println("B1: "+ accounts.getBalance(bobWallet.getPublicKey("B1")));
        System.out.println("B2: "+ accounts.getBalance(bobWallet.getPublicKey("B2")));
        System.out.println("C1: "+ accounts.getBalance(carolWallet.getPublicKey("C1")));
        System.out.println("C2: "+ accounts.getBalance(carolWallet.getPublicKey("C2")));
        System.out.println("C3: "+ accounts.getBalance(carolWallet.getPublicKey("C3")));
        System.out.println("D1: "+ accounts.getBalance(davidWallet.getPublicKey("D1")));

        // =========================== CASE 4 ===========================
        // Set the balance for A1 to 20
        System.out.println("\n========== Test 4 ==========\n");

        accounts.setBalance(aliceWallet.getPublicKey("A1"),20);
        System.out.println("A1: "+ accounts.getBalance(aliceWallet.getPublicKey("A1")));

        // =========================== CASE 5 ===========================
        // Add 15 to the balance for B1
        System.out.println("\n========== Test 5 ==========\n");

        accounts.addToBalance(bobWallet.getPublicKey("B1"),15);
        System.out.println("B1: "+ accounts.getBalance(bobWallet.getPublicKey("B1")));

        // =========================== CASE 6 ===========================
        // Subtract 5 from the balance for B1.
        System.out.println("\n========== Test 6 ==========\n");

        accounts.subtractFromBalance(bobWallet.getPublicKey("B1"),5);
        System.out.println("B1: "+ accounts.getBalance(bobWallet.getPublicKey("B1")));

        // =========================== CASE 7 ===========================
        // Set the balance for C1 to 10.
        System.out.println("\n========== Test 7 ==========\n");

        accounts.setBalance(carolWallet.getPublicKey("C1"),10);
        System.out.println("C1: "+ accounts.getBalance(carolWallet.getPublicKey("C1")));

        // =========================== CASE 8 ===========================
        // TxInputList txil1
        System.out.println("\n========== Test 8 ==========\n");

        byte[] exampleMessage1 = KeyUtils.integer2ByteArray(1);
        byte[] exampleMessage2 = KeyUtils.integer2ByteArray(2);

        TxInputList txil1 = new TxInputList(aliceWallet.getPublicKey("A1"),15,
                aliceWallet.signMessage(exampleMessage,"A1"));
        txil1.addEntry(bobWallet.getPublicKey("B1"),5,bobWallet.signMessage(exampleMessage,"B1"));

        System.out.println(accounts.checkTxInputListCanBeDeducted(txil1));

        // =========================== CASE 9 ===========================
        // TxInputList txil2
        System.out.println("\n========== Test 9 ==========\n");

        TxInputList txil2 = new TxInputList(aliceWallet.getPublicKey("A1"),15,
                aliceWallet.signMessage(exampleMessage,"A1"));
        txil2.addEntry(aliceWallet.getPublicKey("A1"),15,aliceWallet.signMessage(exampleMessage,"A1"));

        System.out.println(accounts.checkTxInputListCanBeDeducted(txil2));

        // =========================== CASE 10 ===========================
        // Subtract txil1
        System.out.println("\n========== Test 10 ==========\n");
        System.out.println("A1: "+ accounts.getBalance(aliceWallet.getPublicKey("A1")));
        System.out.println("B1: "+ accounts.getBalance(bobWallet.getPublicKey("B1")));
        System.out.println("Subtracting txil1");
        accounts.subtractTxInputList(txil1);
        System.out.println("A1: "+ accounts.getBalance(aliceWallet.getPublicKey("A1")));
        System.out.println("B1: "+ accounts.getBalance(bobWallet.getPublicKey("B1")));

        // =========================== CASE 11 ===========================
        // TxInputList txol1= txil2
        System.out.println("\n========== Test 11 ==========\n");
        System.out.println("Creating txol1");
        TxOutputList txol1 = new TxOutputList(aliceWallet.getPublicKey("A1"),15,
                aliceWallet.getPublicKey("A1"),15);
        System.out.println("A1: "+ accounts.getBalance(aliceWallet.getPublicKey("A1")));
        System.out.println("Adding txol1");
        accounts.addTxOutputList(txol1);
        System.out.println("A1: "+ accounts.getBalance(aliceWallet.getPublicKey("A1")));

        // =========================== CASE 12 ===========================
        // Correctly signed input
        System.out.println("\n========== Test 12 ==========\n");
        TxOutputList txop = new TxOutputList(bobWallet.getPublicKey("B2"),10,
                carolWallet.getPublicKey("C1"),20);
        byte[] message = txop.getMessageToSign(aliceWallet.getPublicKey("A1"),30);
        byte[] signature = aliceWallet.signMessage(message,"A1");
        TxInputList txin = new TxInputList(aliceWallet.getPublicKey("A1"),30,signature
                );

        System.out.println(txin.checkSignature(txop));

        // =========================== CASE 13 ===========================
        // Incorrectly signed input
        System.out.println("\n========== Test 13 ==========\n");
        TxOutputList txop2 = new TxOutputList(bobWallet.getPublicKey("B2"),10,
                carolWallet.getPublicKey("C1"),20);
        TxInputList txin2 = new TxInputList(aliceWallet.getPublicKey("A1"),30,exampleSignature
        );

        System.out.println(txin2.checkSignature(txop2));
    }

    /** 
     * main function running test cases
     */            

    public static void main(String[] args)
	throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
	AccountBalance.test();
    }
}
