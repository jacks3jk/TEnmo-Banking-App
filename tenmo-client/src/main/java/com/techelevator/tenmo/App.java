package com.techelevator.tenmo;

import com.techelevator.tenmo.models.Account;
import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transaction;
import com.techelevator.tenmo.models.UserCredentials;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.AuthenticationServiceException;
import com.techelevator.view.ConsoleService;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

public class App {

private static final String API_BASE_URL = "http://localhost:8080/";
    
    private static final String MENU_OPTION_EXIT = "Exit";
    private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
	private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
	private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
	private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
	private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
	private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS, MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS, MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	
    private AuthenticatedUser currentUser;
    private ConsoleService console;
    private AuthenticationService authenticationService;
    private AccountService accountService = new AccountService();
    private Account account = new Account();
    private RestTemplate restTemplate = new RestTemplate();
    private Transaction transaction = new Transaction();

    public static void main(String[] args) {
    	App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL));
    	app.run();
    }

    public App(ConsoleService console, AuthenticationService authenticationService) {
		this.console = console;
		this.authenticationService = authenticationService;
	}

	public void run() {
		System.out.println("*********************");
		System.out.println("* Welcome to TEnmo! *");
		System.out.println("*********************");
		
		registerAndLogin();
		if (isAuthenticated()) {
			mainMenu();
		}
	}

	private void mainMenu() {
    	boolean running = true;
		while(running) {
			String choice = (String)console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if(MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
				viewCurrentBalance();
			} else if(MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
				viewTransferHistory();
			} else if(MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
				viewPendingRequests();
			} else if(MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
				sendBucks();
			} else if(MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
				requestBucks();
			} else if(MAIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else {
				running = false;
			}
		}
	}
	private void viewCurrentBalance() {
		// TODO Auto-generated method stub
		BigDecimal balance = null;
		HttpEntity<?> entity = accountService.getEntity(currentUser);
		try {
		balance = restTemplate.exchange(API_BASE_URL + "accounts/balance", HttpMethod.GET, entity, BigDecimal.class).getBody();
			System.out.println(balance);
		} catch (RestClientException e) {
			System.out.println("Could not get balance.");
		}
	}

	private void viewTransferHistory() {
		HttpEntity<?> entity = accountService.getEntity(currentUser);
		try {
			Transaction[] transactions = restTemplate.exchange(API_BASE_URL + "list", HttpMethod.GET, entity, Transaction[].class).getBody();
			if (transactions.length != 0) {
				for (Transaction transaction : transactions) {
					System.out.println(transaction.toString());
				}
			}
		} catch (RestClientException e) {
			System.out.println("Could not get transfers.");
		}
	}


	private void viewPendingRequests() {
		HttpEntity<?> entity = accountService.getEntity(currentUser);
		try {
			Transaction[] transactions = restTemplate.exchange(API_BASE_URL + "transfer/pending", HttpMethod.GET, entity, Transaction[].class).getBody();
			if (transactions.length != 0) {
				for (Transaction transaction : transactions) {
					System.out.println(transaction.toString());
				}
			}
		} catch (RestClientException e) {
			System.out.println("Could not get transfers.");
		}
	}

	private void sendBucks() {
		HttpEntity<?> entity = accountService.getEntity(currentUser);
		try {
			Account[] accounts = restTemplate.exchange(API_BASE_URL + "accounts", HttpMethod.GET, entity, Account[].class).getBody();
			Account account = (Account) console.getChoiceFromOptions(accounts);
			String amount = console.getUserInput("Enter amount to send: ");
			BigDecimal amountBD = new BigDecimal(amount);
			if (amountBD.compareTo(BigDecimal.ZERO) > 0) {
				Account accountFrom = restTemplate.exchange(API_BASE_URL + "accounts/user", HttpMethod.GET, entity, Account.class).getBody();
				transaction = new Transaction(accountFrom.getAccountId(), account.getAccountId(), amountBD, 2, 2);
				HttpEntity<?> entity2 = accountService.getEntity(currentUser, transaction);
				restTemplate.exchange(API_BASE_URL + "transfer", HttpMethod.POST, entity2, Transaction.class);
			} else {
				System.out.println("Enter a positive amount.");
			}
		} catch (RestClientException e) {
			System.out.println("Could not send TE Bucks.");
		}
	}

	private void requestBucks() {
		HttpEntity<?> entity = accountService.getEntity(currentUser);
		try {
			Account[] accounts = restTemplate.exchange(API_BASE_URL + "accounts", HttpMethod.GET, entity, Account[].class).getBody();
			Account account = (Account) console.getChoiceFromOptions(accounts);
			String amount = console.getUserInput("Requested amount: ");
			BigDecimal amountBD = new BigDecimal(amount);
			if (amountBD.compareTo(BigDecimal.ZERO) > 0) {
				Account accountFrom = restTemplate.exchange(API_BASE_URL + "accounts/user", HttpMethod.GET, entity, Account.class).getBody();
				transaction = new Transaction(account.getAccountId(), accountFrom.getAccountId(), amountBD, 1, 1);
				HttpEntity<?> entity2 = accountService.getEntity(currentUser, transaction);
				restTemplate.exchange(API_BASE_URL + "transfer/pending", HttpMethod.POST, entity2, Transaction.class);
			} else {
				System.out.println("Please enter a positive amount.");
			}
		} catch (RestClientException e) {
			System.out.println("Could not request TE Bucks.");
		}
	}


	private void registerAndLogin() {
		while(!isAuthenticated()) {
			String choice = (String)console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
			if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
				register();
			} else {
				break;
			}
		}
	}

	private boolean isAuthenticated() {
		return currentUser != null;
	}

	private void register() {
		System.out.println("Please register a new user account");
		boolean isRegistered = false;
        while (!isRegistered) //will keep looping until user is registered
        {
            UserCredentials credentials = collectUserCredentials();
            try {
            	authenticationService.register(credentials);
            	isRegistered = true;
            	System.out.println("Registration successful. You can now login.");
            } catch(AuthenticationServiceException e) {
            	System.out.println("REGISTRATION ERROR: "+e.getMessage());
				System.out.println("Please attempt to register again.");
            }
        }
	}

	private void login() {
		System.out.println("Please log in");
		currentUser = null;
		while (currentUser == null) //will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
		    try {
				currentUser = authenticationService.login(credentials);
			} catch (AuthenticationServiceException e) {
				System.out.println("LOGIN ERROR: "+e.getMessage());
				System.out.println("Please attempt to login again.");
			}
		}
	}
	
	private UserCredentials collectUserCredentials() {
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		return new UserCredentials(username, password);
	}
}

	public boolean makes10(int a, int b) {
		if (a  == 10) {
			return true;
		} else if (a + b == 10) {
			return true;
		} else {
			return false;
		}
	}
