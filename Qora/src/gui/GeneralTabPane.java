package gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.TreeMap;

import gui.assets.AssetsPanel;
import gui.models.WalletBlocksTableModel;
import gui.models.WalletTransactionsTableModel;
import gui.naming.NamingServicePanel;
import gui.transaction.TransactionDetailsFactory;
import gui.voting.VotingPanel;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

import database.wallet.BlockMap;
import database.wallet.TransactionMap;
import qora.transaction.Transaction;

public class GeneralTabPane extends JTabbedPane{

	private static final long serialVersionUID = 2717571093561259483L;

	private WalletTransactionsTableModel transactionsModel;
	private JTable transactionsTable;
	
	public GeneralTabPane()
	{
		super();
		
		//ACCOUNTS
		this.addTab("Accounts", new AccountsPanel());
        
		//SEND
		this.addTab("Send money", new SendMoneyPanel());
        
		//TRANSACTIONS
		this.transactionsModel = new WalletTransactionsTableModel();
		this.transactionsTable = new JTable(this.transactionsModel);
		
		//TRANSACTIONS SORTER
		Map<Integer, Integer> indexes = new TreeMap<Integer, Integer>();
		indexes.put(WalletTransactionsTableModel.COLUMN_CONFIRMATIONS, TransactionMap.TIMESTAMP_INDEX);
		indexes.put(WalletTransactionsTableModel.COLUMN_TIMESTAMP, TransactionMap.TIMESTAMP_INDEX);
		indexes.put(WalletTransactionsTableModel.COLUMN_ADDRESS, TransactionMap.ADDRESS_INDEX);
		indexes.put(WalletTransactionsTableModel.COLUMN_AMOUNT, TransactionMap.AMOUNT_INDEX);
		QoraRowSorter sorter = new QoraRowSorter(transactionsModel, indexes);
		transactionsTable.setRowSorter(sorter);
		
		//TRANSACTION DETAILS
		this.transactionsTable.addMouseListener(new MouseAdapter() 
		{
			public void mouseClicked(MouseEvent e) 
			{
				if(e.getClickCount() == 2) 
				{
					//GET ROW
			        int row = transactionsTable.getSelectedRow();
			        row = transactionsTable.convertRowIndexToModel(row);
			        
			        //GET TRANSACTION
			        Transaction transaction = transactionsModel.getTransaction(row);
			         
			        //SHOW DETAIL SCREEN OF TRANSACTION
			        TransactionDetailsFactory.getInstance().createTransactionDetail(transaction);
			    }
			}
		});			
		this.addTab("Transactions", new JScrollPane(this.transactionsTable));       
		
		//TRANSACTIONS
		WalletBlocksTableModel blocksModel = new WalletBlocksTableModel();
		JTable blocksTable = new JTable(blocksModel);
				
		//TRANSACTIONS SORTER
		indexes = new TreeMap<Integer, Integer>();
		indexes.put(WalletBlocksTableModel.COLUMN_HEIGHT, BlockMap.TIMESTAMP_INDEX);
		indexes.put(WalletBlocksTableModel.COLUMN_TIMESTAMP, BlockMap.TIMESTAMP_INDEX);
		indexes.put(WalletBlocksTableModel.COLUMN_GENERATOR, BlockMap.GENERATOR_INDEX);
		indexes.put(WalletBlocksTableModel.COLUMN_BASETARGET, BlockMap.BALANCE_INDEX);
		indexes.put(WalletBlocksTableModel.COLUMN_TRANSACTIONS, BlockMap.TRANSACTIONS_INDEX);
		indexes.put(WalletBlocksTableModel.COLUMN_FEE, BlockMap.FEE_INDEX);
		sorter = new QoraRowSorter(blocksModel, indexes);
		blocksTable.setRowSorter(sorter);
		
        this.addTab("Generated Blocks", new JScrollPane(blocksTable));
        
        //NAMING
        this.addTab("Naming service", new NamingServicePanel());      
        
        //VOTING
        this.addTab("Voting", new VotingPanel());       
        
        //ASSETS
        this.addTab("Assets", new AssetsPanel());
	}
	
}
