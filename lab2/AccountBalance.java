import java.util.TreeMap;
import java.util.Map;
import java.util.Set;


/** 
  *   AccountBalance defines the balance of the users at a given time
      in the ledger model of bitcoins
  */

public class AccountBalance {

    /** 
     * The current balance of each user, with each account's name mapped to its 
     *    current balance.
     */
    
    private TreeMap<String, Integer> accountBalanceBase;

    /** 
     * Constructor for creating empty AccountBalance
     */

    public AccountBalance() {
	accountBalanceBase = new TreeMap<String, Integer>();
    }

    /** 
     * Constructor for creating AccountBalance from a map from string to integers
     */
    
    public AccountBalance(TreeMap<String, Integer> accountBalanceBase) {
	this.accountBalanceBase = accountBalanceBase;
    }

    /** 
     * obtain the underlying Treemap from string to integers
     */   
    
    public TreeMap<String,Integer> getAccountBalanceBase(){
	return accountBalanceBase;
    };

    /** 
      * obtain the list of users in the tree map
      */   
    
    public Set<String> getUsers(){
	return getAccountBalanceBase().keySet();
    };




    /** 
     * Adds an account for user with balance.
     *
     * if there was an entry it is overridden.  
     */

    public void addAccount(String user, int balance) {
	accountBalanceBase.put(user, balance);
    }

    /** 
     * @return true if the {@code user} exists in AccountBalance
     */
    
    public boolean hasUser(String user) {
	return accountBalanceBase.containsKey(user);
    }


    /** 
     * @return the balance for this account {@code account}
     *
     *  if there was no entry, return zero
     *
     */
    
    public int getBalance(String user) {
	if (hasUser(user)){
		return accountBalanceBase.get(user);
	    } else
	    {  return 0;
	    }
    }


    /** 
     * set the balance for {@code user} to {@code amount}
     *  this will override any existing entries
     */

    
    public void setBalance(String user, int amount){
	accountBalanceBase.put(user,amount);
	    };


    /** 
     *  Adds amount to balance for {@code user}
     * 
     *  if there was no entry for {@code user} add one with 
     *       {@code balance}
     */
    
    public void addBalance(String user, int amount) {
	setBalance(user,getBalance(user) + amount);
    }


    /** 
     *   Subtracts amount from balance for {@code user}
     */
    
    public void subtractBalance(String user, int amount) {
	setBalance(user,getBalance(user) - amount);
    }


    /** 
     * Check balance has at least amount for {@code user}
     */

    public boolean checkBalance(String user, int amount) {
	return (getBalance(user) >= amount);
    }    

    /** 
     *
     *  Task 1: Fill in the body of method checkAccountBalanceDeductable()
     *          It has been commented out so that the code compiles.
     *
     * Check all items in accountBalance2 can be deducted from the current one
     *
     *   accountBalance2 is usually obtained
     *   from a list of inputs of a transaction
     *
     * Checking that a TxOutputList  can be deducted will be later done
     *  by first converting that TxOutputList into an AccountBalance and then using
     *    this method
     *
     * A naive check would just check whether each entry of a TxOutputList can be 
     *   deducted
     *
     * But there could be an output for the same user Alice of say 10 units twice
     *   where there are not enough funds to deduct it twice but enough
     *   funds to deduct it once
     * The naive check would succeed, but after converting the TxOutputList
     *  to AccountBalance we have that for Alice 20 units have to be deducted
     *  so the deduction of the accountBalance created fails.
     *
     * One could try for checking that one should actually deduct each entry in squence
     *   but then one has to backtrack again.
     * Converting the TxOutputList into a AccountBalance is a better approach since the
     *   TxOutputList is usually much smaller than the main AccountBalance.
     * 
     */    


    public boolean checkAccountBalanceDeductable(AccountBalance accountBalance2){
        for (String user:accountBalance2.getUsers()) {
            if (!checkBalance(user, accountBalance2.getBalance(user))){
                return false;
            }
        }
        return true;
    };


    /** 
     *
     *  Task 2: Fill in the method checkTxELdeductable 
     *          It has been commented out so that the code compiles.
     *
     *  It checks that a list of txEntries (which will be inputs of a transactions)
     *     can be deducted from AccountBalance
     *
     *   done by first converting the list of txEntries into an accountBalance
     *     and then checking that the resulting accountBalance can be deducted.
     *   
     */    

    public boolean checkTxELdeductable(TxEntryList txel){
	    return checkAccountBalanceDeductable(txel.toAccountBalance());
    };
    
    
    /** 
     *  Task 3: Fill in the methods subtractTxEL and  addTxEL.
     *
     *   Subtract a list of txEntries (txel, usually transaction inputs) from the accountBalance 
     *
     *   requires that the list to be deducted is deductable.
     *   
     */    
    
    
    public void subtractTxEL(TxEntryList txel){
	    if (checkTxELdeductable(txel)){
	        AccountBalance txelAccount = txel.toAccountBalance();
	        for (String user:txelAccount.getUsers()){
	            subtractBalance(user,txelAccount.getBalance(user));
            }
        }
    }

    



    /** 
     * Add a list of txEntries (txel, usually transaction outputs) to the current accountBalance
     *
     */    

       public void addTxEL(TxEntryList txel){
           AccountBalance txelAccount = txel.toAccountBalance();
           for (String user:txelAccount.getUsers()){
               addBalance(user,txelAccount.getBalance(user));
           }
       }


    /** 
     *
     *  Task 4: Fill in the method checkTxValid
     *          It has been commented out so that the code compiles.
     *
     * Check a transaction is valid:
     *    the sum of outputs is less than the sum of inputs
     *    and the inputs can be deducted from the accountBalance.
     *
     */    

    /*
    public boolean checkTxValid(Tx tx){
	// fill in Body 		
    };
    */

    /** 
     *
     *  Task 5: Fill in the method processTx
     *
     * Process a transaction
     *    by first deducting all the inputs
     *    and then adding all the outputs.
     *
     */    
    
    public void processTx(Tx tx){
	// fill in Body 
    };


    /** 
     * Prints the current state of the accountBalance. 
     */
    
    public void print() {
	for (String user : accountBalanceBase.keySet()) {
	    Integer value = accountBalanceBase.get(user).intValue();
	    System.out.println("The balance for " + user + " is " + value); 
	}

    }


    /** 
     *  Task 6: Fill in the testcases as described in the labsheet
     *    
     * Testcase
     */
    
    public static void test() {
        // Create accounts
	    AccountBalance accounts = new AccountBalance();
        // Add users to those accounts
        System.out.println("----- Add users -----");
	    accounts.addAccount("Alice",0);
        accounts.addAccount("Bob",0);
        accounts.addAccount("Carol",0);
        accounts.addAccount("David",0);
        // Output Users
        accounts.print();
        System.out.println("\n");

        // Set balance for Alice to 20
        System.out.println("----- Set balance for Alice to 20 -----");
        accounts.setBalance("Alice",20);
        accounts.print();
        System.out.println("\n");

        // Set balance for Bob to 15
        System.out.println("----- Set balance for Bob to 15 -----");
        accounts.setBalance("Bob",15);
        accounts.print();
        System.out.println("\n");

        // Subtract 5 from Bob
        System.out.println("----- Subtract 5 from Bob -----");
        accounts.subtractBalance("Bob",5);
        accounts.print();
        System.out.println("\n");

        // TxEntryList1
        System.out.println("----- TxEntryList1 -----");
        TxEntryList txel1 = new TxEntryList("Alice",15,"Bob",5);
        txel1.print();
        System.out.println(accounts.checkTxELdeductable(txel1));
        System.out.println("\n");

        // TxEntryList2
        System.out.println("----- TxEntryList2 -----");
        TxEntryList txel2 = new TxEntryList("Alice",15,"Alice",15);
        txel2.print();
        System.out.println(accounts.checkTxELdeductable(txel2));
        System.out.println("\n");

        // Subtract transaction1
        System.out.println("----- Subtract transaction1 -----");
        accounts.subtractTxEL(txel1);
        accounts.print();
        System.out.println("\n");

        // Add transaction2
        System.out.println("----- Add transaction2 -----");
        accounts.addTxEL(txel2);
        accounts.print();
        System.out.println("\n");

        // Create Transaction tx1
        System.out.println("----- Create Transaction Tx1 -----");
        Tx tx1 = new Tx(new TxEntryList("Alice",40),new TxEntryList("Bob",5,"Carol",
                20));

        // Check Transaction tx1 is valid
        System.out.println("----- Check Tx1 is valid -----");
        System.out.println(tx1.checkTxAmountsValid());
        System.out.println("\n");

        // Create Transaction tx2
        System.out.println("----- Create Transaction Tx2 -----");
        Tx tx2 = new Tx(new TxEntryList("Alice",20),new TxEntryList("Bob",5,"Carol",
                20));

        // Check Transaction tx2 is valid
        System.out.println("----- Check Tx2 is valid -----");
        System.out.println(tx2.checkTxAmountsValid());
        System.out.println("\n");

        // Create Transaction tx3
        System.out.println("----- Create Transaction Tx3 -----");
        Tx tx3= new Tx(new TxEntryList("Alice",25),new TxEntryList("Bob",5,"Carol",
                20));

        // Check Transaction tx3 is valid
        System.out.println("----- Check Tx3 is valid -----");
        System.out.println(tx3.checkTxAmountsValid());
        System.out.println("\n");

        // Run tx3
        System.out.println("----- Run Tx3 -----");
        // Set Alice's balance to 25
        accounts.setBalance("Alice",25);
        // Subtract inputs
        accounts.subtractTxEL(tx3.toInputs());
        accounts.addTxEL(tx3.toOutputs());
        accounts.print();
        System.out.println("\n");

        // Create Transaction tx4
        System.out.println("----- Create Transaction Tx4 -----");
        Tx tx4 = new Tx(new TxEntryList("Alice",5, "Alice",5),new TxEntryList("Bob",
                10));

        // Run Transaction tx4
        System.out.println("----- Run Transaction Tx4 -----");
        // Set Alice's to 10
        accounts.setBalance("Alice",10);
        accounts.subtractTxEL(tx4.toInputs());
        accounts.addTxEL(tx4.toOutputs());
        accounts.print();
        System.out.println("\n");


    }
    
    /** 
     * main function running test cases
     */            

    public static void main(String[] args) {
	AccountBalance.test();	
    }
}
