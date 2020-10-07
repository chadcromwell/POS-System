/********************************************************************************************************************
Author: Chad Cromwell
Date: December 15th, 2017
Assignment: 3
Program: Crema.java
Description: A GUI POS system for Crema
Methods:
		start() method - Starts the thread for the program if it isn't already running
		stop() method - Stops the thread for the program if it is running
		render() method - Renders each frame
		run() method - The program loop
		getImage(JButton, String) method - Accepts a JButton and filename, loads the filename image and assigns it to the button
		newOrdersPanel() method - Generates the New Orders panel
		payPanel() method - The same as newOrderPanel() method however for keeping the payment panel updated, follows the same logic
		buttonVisible(boolean) method - Accepts a boolean, if true it makes all buttons visible, if false it makes all buttons invisible
		highlightStyleButton(JButton) method - Accepts a style JButton, unhighlights other style buttons and highlights the passed one
		highlightSizebutton(JButton) method - Accepts a size JButton, unhighlights other size buttons and highlights the passed one
********************************************************************************************************************/

import java.awt.*;
import java.awt.event.*;
import java.math.*;
import javax.swing.*;
import javax.imageio.*;
import java.text.*;
import java.util.*;

public class Crema extends Frame implements Runnable {
	//Finals
	private static final int WIDTH = 1280; //Window width
	private static final double HEIGHT = (WIDTH*0.5625)+22; //Window height (+22 to account for MacOS window decorations)
	private static final String TITLE = "Crema POS"; //Window title
	public static final int BUTTONWIDTH = 200; //Window title
	public static final int BUTTONHEIGHT = 200; //Window title
	private static final Font font = new Font("Arial", Font.BOLD, 18); //Arial font, bold, 12pt
	private static final Font orderFont = new Font("Arial", Font.BOLD, 24); //Arial font, bold, 12pt
	public static final double TAXES = 1.13; //Local tax amount
	public static Canvas c = new Canvas(); //Used for font metrics
	FontMetrics orderFontMetrics = c.getFontMetrics(orderFont); //Used to estime size of JLabels before rendering for placement

	public static int totalOrders; //The total number of orders
	public static int itemNumber; //Used to apply unique item number to each item
	public static ArrayList<JButton> imageButtons; //ArrayList that holds the buttons for ease of parameter setting
	public static ArrayList<JButton> normalButtons; //ArrayList that holds the buttons for ease of parameter setting
	public static ArrayList<Order> currentOrders; //ArrayList that holds the current orders
	public static ArrayList<Order> pastOrders; //ArrayList that holds the past orders (for future implementation);
	public static ArrayList<JButton> styleButtons; //ArrayList that holds the style selection buttons
	public static ArrayList<JButton> sizeButtons; //ArraList that holds the size selection buttons

	public BigDecimal total; //Total for the order, used for payment processing
	BigDecimal amountPaidBD;
	BigDecimal tempOrderTotalBD;
	public String amountPaid; //Total paid by customer for the order, used for payment processing
	public static String screen = "Home"; //What screen to display, default "Home"
	public String type; //Holds the type of the selected item
	public String style; //Holds the style of the selected item
	public String size; //Holds the size of the selected item
	public String defaultName = "No name"; //Initialize customer name as "No name" in case name is not inputted
	public Order tempOrder; //The current order
	DecimalFormat df = new DecimalFormat("0.00"); //Formatter to display doubles in dollar format

	//Swing components-------------------//
	//Panels
	private static JFrame frame; //Frame
	private static JPanel homePanel; //Home panel that goes in frame
	private static JPanel newOrderPanel; //New order panel that goes in frame
	private static JPanel drinksPanel; //Drinks panel that goes in frame
	private static JPanel cappuccinoPanel; //Cappuccino panel that goes in frame
	private static JPanel lattePanel; //Latte panel that goes in frame
	private static JPanel mochaPanel; //Mocha panel that goes in frame
	private static JPanel espressoPanel; //Espresso panel that goes in frame
	private static JPanel teaPanel; //Tea panel that goes in frame
	private static JPanel cappuccinoPanelModify; //Cappuccino panel that goes in frame
	private static JPanel lattePanelModify; //Latte panel that goes in frame
	private static JPanel mochaPanelModify; //Mocha panel that goes in frame
	private static JPanel espressoPanelModify; //Espresso panel that goes in frame
	private static JPanel teaPanelModify; //Tea panel that goes in frame
	private static JPanel foodPanel; //Food panel that goes in frame
	private static JPanel foodPanelModify; //Food panel that goes in frame
	private static JPanel cremaRollPanel; //Crema roll panel that goes in frame
	private static JPanel cookiePanel; //Cookie panel that goes in frame
	private static JPanel cheesecakePanel; //Cheesecake panel that goes in frame
	private static JPanel affogatoPanel; //Affogato panel that goes in frame
	private static JPanel gelatoPanel; //Gelato panel that goes in frame
	private static JPanel payPanel; //Pay panel that goes in frame
	private static JPanel refundOrderPanel; //Refund Order panel that goes in frame
	private static JPanel cashPanel; //Cash panel that goes in frame
	private static JPanel creditPanel; //Credit panel that goes in frame
	private static JPanel giftCardPanel; //Gift card panel that goes in frame
	private static JPanel completePanel; //Complete panel that goes in frame

	//Buttons
	private static JButton newOrderButton; //New Order button
	private static JButton pastOrdersButton; //Past orders button
	private static JButton currentOrdersButton; //Past orders button
	private static JButton finishedOrdersButton; //Past orders button
	private static JButton drinksButton; // Drinks button
	private static JButton foodButton; //Food button
	private static JButton cappuccinoButton; //Cappuccino button
	private static JButton latteButton; //Latte button
	private static JButton mochaButton; //Mocha button
	private static JButton espressoButton; //Espresso button
	private static JButton teaButton; //Espresso button
	private static JButton cremaRollButton; //Cream roll button
	private static JButton cookieButton; //Cookie button
	private static JButton cheesecakeButton; //Cheesecake button
	private static JButton affogatoButton; //Affogato button
	private static JButton gelatoButton; //Gelato button
	private static JButton cremaRollButtonModify; //Cream roll button
	private static JButton cookieButtonModify; //Cookie button
	private static JButton cheesecakeButtonModify; //Cheesecake button
	private static JButton affogatoButtonModify; //Affogato button
	private static JButton gelatoButtonModify; //Gelato button
	private static JButton icedButtonModify; //Iced button
	private static JButton blendedButtonModify; //Blended button
	private static JButton addButton; //Gift card button
	private static JButton smallButtonCappuccino; //Small button
	private static JButton mediumButtonCappuccino; //Medium button
	private static JButton largeButtonCappuccino; //Large button
	private static JButton hotButtonCappuccino; //Hot button
	private static JButton backButtonCappuccino; //Back button
	private static JButton addButtonCappuccino; //Gift card button
	private static JButton smallButtonCappuccinoModify; //Small button
	private static JButton mediumButtonCappuccinoModify; //Medium button
	private static JButton largeButtonCappuccinoModify; //Large button
	private static JButton hotButtonCappuccinoModify; //Hot button
	private static JButton removeButtonCappuccino; //Back button
	private static JButton updateButtonCappuccinoModify; //Gift card button
	private static JButton smallButtonLatte; //Small button
	private static JButton mediumButtonLatte; //Medium button
	private static JButton largeButtonLatte; //Large button
	private static JButton hotButtonLatte; //Hot button
	private static JButton icedButtonLatte; //Iced button
	private static JButton blendedButtonLatte; //Blended button
	private static JButton backButtonLatte; //Back button
	private static JButton addButtonLatte; //Gift card button
	private static JButton smallButtonLatteModify; //Small button
	private static JButton mediumButtonLatteModify; //Medium button
	private static JButton largeButtonLatteModify; //Large button
	private static JButton hotButtonLatteModify; //Hot button
	private static JButton icedButtonLatteModify; //Iced button
	private static JButton blendedButtonLatteModify; //Blended button
	private static JButton removeButtonLatte; //Back button
	private static JButton updateButtonLatteModify; //Gift card button
	private static JButton smallButtonMocha; //Small button
	private static JButton mediumButtonMocha; //Medium button
	private static JButton largeButtonMocha; //Large button
	private static JButton hotButtonMocha; //Hot button
	private static JButton icedButtonMocha; //Iced button
	private static JButton blendedButtonMocha; //Blended button
	private static JButton backButtonMocha; //Back button
	private static JButton addButtonMocha; //Gift card button
	private static JButton smallButtonMochaModify; //Small button
	private static JButton mediumButtonMochaModify; //Medium button
	private static JButton largeButtonMochaModify; //Large button
	private static JButton hotButtonMochaModify; //Hot button
	private static JButton icedButtonMochaModify; //Iced button
	private static JButton blendedButtonMochaModify; //Blended button
	private static JButton removeButtonMocha; //Back button
	private static JButton updateButtonMochaModify; //Gift card button
	private static JButton smallButtonEspresso; //Small button
	private static JButton largeButtonEspresso; //Large button
	private static JButton hotButtonEspresso; //Hot button
	private static JButton backButtonEspresso; //Back button
	private static JButton addButtonEspresso; //Gift card button
	private static JButton smallButtonEspressoModify; //Small button
	private static JButton largeButtonEspressoModify; //Large button
	private static JButton hotButtonEspressoModify; //Hot button
	private static JButton removeButtonEspresso; //Back button
	private static JButton updateButtonEspressoModify; //Gift card button
	private static JButton smallButtonTea; //Small button
	private static JButton mediumButtonTea; //Medium button
	private static JButton largeButtonTea; //Large button
	private static JButton hotButtonTea; //Hot button
	private static JButton icedButtonTea; //Iced button
	private static JButton blendedButtonTea; //Blended button
	private static JButton backButtonTea; //Back button
	private static JButton addButtonTea; //Gift card button
	private static JButton smallButtonTeaModify; //Small button
	private static JButton mediumButtonTeaModify; //Medium button
	private static JButton largeButtonTeaModify; //Large button
	private static JButton hotButtonTeaModify; //Hot button
	private static JButton icedButtonTeaModify; //Iced button
	private static JButton blendedButtonTeaModify; //Blended button
	private static JButton removeButtonTea; //Back button
	private static JButton updateButtonTeaModify; //Gift card button
	private static JButton backButtonDrinks; //Back button
	private static JButton backButtonFood; //Back button
	private static JButton removeButtonFood; //Back button
	private static JButton backButtonNewOrder; //Back button
	private static JButton backButtonPay; //Back button
	private static JButton payButton; //Pay button
	private static JButton refundOrderButton; //Refund order button
	private static JButton cancelOrderButton; //Cancel order button
	private static JButton cancelOrderButtonPay; //Cancel order button
	private static JButton cashButton; //Cash button
	private static JButton creditButton; //Credit button
	private static JButton giftCardButton; //Gift card button
	private static JButton completeButton; //Gift card button
	
	//Images
	public static Image drinksImage; //Drinks button image
	public static Image cappuccinoImage; //Cappuccino button image
	public static Image latteImage; //Latte button image
	public static Image mochaImage; //Mocha button image
	public static Image espressoImage; //Espresso button image
	public static Image teaImage; //Tea button image
	public static Image foodImage; //Food button image
	public static Image cremaRollImage; //Crema roll button image
	public static Image cookieImage; //Cookie button image
	public static Image cheesecakeImage; //Cheesecake button image
	public static Image affogatoImage; //Affogato button image
	public static Image gelatoImage; //Gelato button image

	//Booleans
	private boolean isRunning = false; //Whether program is running or not
	private boolean removed = false; //Whether or not an item has been removed yet
	private boolean customerNameGiven = false; //Whether or not the customer's name has been entered

	//Objects
	private Thread thread; //Thread

	//Program constructor
	public Crema(){
		//Try to load button images
		try {
			drinksImage = ImageIO.read(getClass().getResource("img/drinksButton.png"));
				cappuccinoImage = ImageIO.read(getClass().getResource("img/cappuccinoButton.png"));
				latteImage = ImageIO.read(getClass().getResource("img/latteButton.png"));
				mochaImage = ImageIO.read(getClass().getResource("img/mochaButton.png"));
				espressoImage = ImageIO.read(getClass().getResource("img/espressoButton.png"));
				teaImage = ImageIO.read(getClass().getResource("img/teaButton.png"));
			foodImage = ImageIO.read(getClass().getResource("img/foodButton.png"));
				cremaRollImage = ImageIO.read(getClass().getResource("img/cremaRollButton.png"));
				cookieImage = ImageIO.read(getClass().getResource("img/cookieButton.png"));
				cheesecakeImage = ImageIO.read(getClass().getResource("img/cheesecakeButton.png"));
				affogatoImage = ImageIO.read(getClass().getResource("img/affogatoButton.png"));
				gelatoImage = ImageIO.read(getClass().getResource("img/gelatoButton.png"));
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		//Initialization-------------------------------------//
		imageButtons = new ArrayList<JButton>(); //Initialize the button ArrayList
		normalButtons = new ArrayList<JButton>(); //Initialize the button ArrayList
		styleButtons = new ArrayList<JButton>(); //Initialize the styleButtons ArrayList
		sizeButtons = new ArrayList<JButton>(); //Initialize the sizeButtons ArrayList
		currentOrders = new ArrayList<Order>(); //Initialize the currentOrders ArrayList
		pastOrders = new ArrayList<Order>(); //Initialize the pastOrders ArrayList
		tempOrder = new Order();  //Initialize the tempOrder ArrayList
		totalOrders = 0; //Initialize the total orders
		itemNumber = 0; //Initialize the item number used for unique ID of each item

		frame = new JFrame(); //Initialize frame
		frame.setSize(new Dimension(WIDTH, (int)HEIGHT)); //Set minimum size of the frame, can't get smaller than the content of the window
		homePanel = new JPanel(); //Initialize panel
		homePanel.setLayout(null); //Use null layout
		newOrderPanel = new JPanel(); //Initialize panel
		newOrderPanel.setLayout(null); //Use null layout
		drinksPanel = new JPanel(); //Initialize panel
		drinksPanel.setLayout(null); //Use null layout
		cappuccinoPanel = new JPanel(); //Initialize panel
		cappuccinoPanel.setLayout(null); //Use null layout
		lattePanel = new JPanel(); //Initialize panel
		lattePanel.setLayout(null); //Use null layout
		mochaPanel = new JPanel(); //Initialize panel
		mochaPanel.setLayout(null); //Use null layout
		espressoPanel = new JPanel(); //Initialize panel
		espressoPanel.setLayout(null); //Use null layout
		teaPanel = new JPanel(); //Initialize panel
		teaPanel.setLayout(null); //Use null layout
		cappuccinoPanelModify = new JPanel(); //Initialize panel
		cappuccinoPanelModify.setLayout(null); //Use null layout
		lattePanelModify = new JPanel(); //Initialize panel
		lattePanelModify.setLayout(null); //Use null layout
		mochaPanelModify = new JPanel(); //Initialize panel
		mochaPanelModify.setLayout(null); //Use null layout
		espressoPanelModify = new JPanel(); //Initialize panel
		espressoPanelModify.setLayout(null); //Use null layout
		teaPanelModify = new JPanel(); //Initialize panel
		teaPanelModify.setLayout(null); //Use null layout
		foodPanel = new JPanel(); //Initialize panel
		foodPanel.setLayout(null); //Use null layout
		foodPanelModify = new JPanel(); //Initialize panel
		foodPanelModify.setLayout(null); //Use null layout
		cremaRollPanel = new JPanel(); //Initialize panel
		cremaRollPanel.setLayout(null); //Use null layout
		cookiePanel = new JPanel(); //Initialize panel
		cookiePanel.setLayout(null); //Use null layout
		cheesecakePanel = new JPanel(); //Initialize panel
		cheesecakePanel.setLayout(null); //Use null layout
		affogatoPanel = new JPanel(); //Initialize panel
		affogatoPanel.setLayout(null); //Use null layout
		gelatoPanel = new JPanel(); //Initialize panel
		gelatoPanel.setLayout(null); //Use null layout
		payPanel = new JPanel(); //Initialize panel
		payPanel.setLayout(null); //Use null layout
		refundOrderPanel = new JPanel(); //Initialize panel
		refundOrderPanel.setLayout(null); //Use null layout
		cashPanel = new JPanel(); //Initialize panel
		cashPanel.setLayout(null); //Use null layout
		creditPanel = new JPanel(); //Initialize panel
		creditPanel.setLayout(null); //Use null layout
		giftCardPanel = new JPanel(); //Initialize panel
		giftCardPanel.setLayout(null); //Use null layout
		completePanel = new JPanel(); //Initialize panel
		completePanel.setLayout(null); //Use null layout


		//Buttons
		backButtonCappuccino = new JButton("Back"); //Initialize back button
		normalButtons.add(backButtonCappuccino); //Add the button to the button ArrayList
		backButtonCappuccino.addActionListener(new ActionListener(){
		
			@Override
			public void actionPerformed(ActionEvent e) {
				SoundEffect.CLICK.play(); //Play click sound
				screen = "Drinks"; //Switch to Drinks screen
				removed = false; //Item not yet removed
				render();
			}
		});

		backButtonLatte = new JButton("Back"); //Initialize back button
		normalButtons.add(backButtonLatte); //Add the button to the button ArrayList
		backButtonLatte.addActionListener(new ActionListener(){
		
			@Override
			public void actionPerformed(ActionEvent e) {
				SoundEffect.CLICK.play(); //Play click sound
				screen = "Drinks"; //Switch to Drinks screen
				removed = false; //Item not yet removed
				render();
			}
		});

		backButtonMocha = new JButton("Back"); //Initialize back button
		normalButtons.add(backButtonMocha); //Add the button to the button ArrayList
		backButtonMocha.addActionListener(new ActionListener(){
		
			@Override
			public void actionPerformed(ActionEvent e) {
				SoundEffect.CLICK.play(); //Play click sound
				screen = "Drinks"; //Switch to Drinks screen
				removed = false; //Item not yet removed
				render();
			}
		});

		backButtonEspresso = new JButton("Back"); //Initialize back button
		normalButtons.add(backButtonEspresso); //Add the button to the button ArrayList
		backButtonEspresso.addActionListener(new ActionListener(){
		
			@Override
			public void actionPerformed(ActionEvent e) {
				SoundEffect.CLICK.play(); //Play click sound
				screen = "Drinks"; //Switch to Drinks screen
				removed = false; //Item not yet removed
				render();
			}
		});

		backButtonTea = new JButton("Back"); //Initialize back button
		normalButtons.add(backButtonTea); //Add the button to the button ArrayList
		backButtonTea.addActionListener(new ActionListener(){
		
			@Override
			public void actionPerformed(ActionEvent e) {
				SoundEffect.CLICK.play(); //Play click sound
				screen = "Drinks"; //Switch to Drinks screen
				removed = false; //Item not yet removed
				render();
			}
		});
		
		removeButtonCappuccino = new JButton("Remove"); //Initialize back button
		normalButtons.add(removeButtonCappuccino); //Add the button to the button ArrayList
		removeButtonCappuccino.addActionListener(new ActionListener(){
		
			@Override
			public void actionPerformed(ActionEvent e) {
				SoundEffect.CLICK.play(); //Play click sound
				screen = "New Order"; //Switch to New Order screen
				removed = false; //Item not yet removed
				render();
			}
		});

		removeButtonLatte = new JButton("Remove"); //Initialize back button
		normalButtons.add(removeButtonLatte); //Add the button to the button ArrayList
		removeButtonLatte.addActionListener(new ActionListener(){
		
			@Override
			public void actionPerformed(ActionEvent e) {
				SoundEffect.CLICK.play(); //Play click sound
				screen = "New Order"; //Switch to New Order screen
				removed = false; //Item not yet removed
				render();
			}
		});

		removeButtonMocha = new JButton("Remove"); //Initialize back button
		normalButtons.add(removeButtonMocha); //Add the button to the button ArrayList
		removeButtonMocha.addActionListener(new ActionListener(){
		
			@Override
			public void actionPerformed(ActionEvent e) {
				SoundEffect.CLICK.play(); //Play click sound
				screen = "New Order"; //Switch to New Order screen
				removed = false; //Item not yet removed
				render();
			}
		});

		removeButtonEspresso = new JButton("Remove"); //Initialize back button
		normalButtons.add(removeButtonEspresso); //Add the button to the button ArrayList
		removeButtonEspresso.addActionListener(new ActionListener(){
		
			@Override
			public void actionPerformed(ActionEvent e) {
				SoundEffect.CLICK.play(); //Play click sound
				screen = "New Order"; //Switch to New Order screen
				removed = false; //Item not yet removed
				render();
			}
		});

		removeButtonTea= new JButton("Remove"); //Initialize back button
		normalButtons.add(removeButtonTea); //Add the button to the button ArrayList
		removeButtonTea.addActionListener(new ActionListener(){
		
			@Override
			public void actionPerformed(ActionEvent e) {
				SoundEffect.CLICK.play(); //Play click sound
				screen = "New Order"; //Switch to New Order screen
				removed = false; //Item not yet removed
				render();
			}
		});

		backButtonDrinks = new JButton("Back"); //Initialize back button
		normalButtons.add(backButtonDrinks); //Add the button to the button ArrayList
		backButtonDrinks.addActionListener(new ActionListener(){
		
			@Override
			public void actionPerformed(ActionEvent e) {
				SoundEffect.CLICK.play(); //Play click sound
				screen = "New Order"; //Switch to New Order screen
				render();
			}
		});

		backButtonFood = new JButton("Back"); //Initialize back button
		normalButtons.add(backButtonFood); //Add the button to the button ArrayList
		backButtonFood.addActionListener(new ActionListener(){
		
			@Override
			public void actionPerformed(ActionEvent e) {
				SoundEffect.CLICK.play(); //Play click sound
				screen = "New Order"; //Switch to New Order screen
				removed = false; //Item not yet removed
				render();
			}
		});

		removeButtonFood = new JButton("Remove"); //Initialize back button
		normalButtons.add(removeButtonFood); //Add the button to the button ArrayList
		removeButtonFood.addActionListener(new ActionListener(){
		
			@Override
			public void actionPerformed(ActionEvent e) {
				SoundEffect.CLICK.play(); //Play click sound
				screen = "New Order"; //Switch to New Order screen
				removed = false; //Item not yet removed
				render();
			}
		});

		backButtonNewOrder = new JButton("Back"); //Initialize back button
		normalButtons.add(backButtonNewOrder); //Add the button to the button ArrayList
		backButtonNewOrder.addActionListener(new ActionListener(){
		
			@Override
			public void actionPerformed(ActionEvent e) {
				SoundEffect.CLICK.play(); //Play click sound
				screen = "Home";
				render();
			}
		});

		backButtonPay = new JButton("Back"); //Initialize back button
		normalButtons.add(backButtonPay); //Add the button to the button ArrayList
		backButtonPay.addActionListener(new ActionListener(){
		
			@Override
			public void actionPerformed(ActionEvent e) {
				SoundEffect.CLICK.play(); //Play click sound
				screen = "New Order"; //Switch to New Order screen
				render();
			}
		});

		newOrderButton = new JButton("New Order"); //Initialize orders button
		normalButtons.add(newOrderButton); //Add the button to the button ArrayList
		newOrderButton.addActionListener(new ActionListener(){
		
			@Override
			public void actionPerformed(ActionEvent e) {
				SoundEffect.CLICK.play(); //Play click sound
				screen = "New Order"; //Switch to New Order screen
				tempOrder = null; //Clear tempOrder, prevents a bug where tempOrder isn't garbage collected
				tempOrder = new Order();
				render(); //Render the new screen
			}
		});

		//Not implemented yet
		pastOrdersButton = new JButton("Past Orders"); //Initialize past orders button
		normalButtons.add(pastOrdersButton); //Add the button to the button ArrayList
		pastOrdersButton.addActionListener(new ActionListener(){
		
			@Override
			public void actionPerformed(ActionEvent e) {
				//SoundEffect.CLICK.play(); //Play click sound
				//screen = "Past Orders"; //Switch to Past Orders screen
				//render();
			}
		});
			
		//Not implemented yet
			currentOrdersButton = new JButton("Current Orders"); //Initialize current orders button
			normalButtons.add(currentOrdersButton); //Add the button to the button ArrayList
			currentOrdersButton.addActionListener(new ActionListener(){
			
				@Override
				public void actionPerformed(ActionEvent e) {
					//SoundEffect.CLICK.play(); //Play click sound
					//screen = "Current Orders";
					//render();
				}
			});
			
			//Not implemented yet
			finishedOrdersButton = new JButton("Finished Orders"); //Initialize finished orders button
			normalButtons.add(finishedOrdersButton); //Add the button to the button ArrayList
			finishedOrdersButton.addActionListener(new ActionListener(){
			
				@Override
				public void actionPerformed(ActionEvent e) {
					//SoundEffect.CLICK.play(); //Play click sound
					//screen = "Finished Orders";
					//render();
				}
			});

		drinksButton = new JButton("Drinks"); //Initialize drinks button
		imageButtons.add(drinksButton); //Add the button to the button ArrayList
		setButtonImage(drinksButton, drinksImage); //Add the icon to the button
		drinksButton.addActionListener(new ActionListener(){
		
			@Override
			public void actionPerformed(ActionEvent e) {
				SoundEffect.CLICK.play(); //Play click sound
				screen = "Drinks"; //Switch to Drinks screen
				render();
			}
		});

			smallButtonCappuccino = new JButton("<html><center>Small<br>" + Item.smallCappuccinoPrice + "0</center></html>"); //Initialize small button
			normalButtons.add(smallButtonCappuccino); //Add the button to the button ArrayList
			sizeButtons.add(smallButtonCappuccino);
			smallButtonCappuccino.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					size = "S"; //Set size to small
					highlightSizeButton(smallButtonCappuccino); //Hightlight small button
				}
			});
			
			smallButtonCappuccinoModify = new JButton("<html><center>Small<br>" + Item.smallCappuccinoPrice + "0</center></html>"); //Initialize small button
			normalButtons.add(smallButtonCappuccinoModify); //Add the button to the button ArrayList
			sizeButtons.add(smallButtonCappuccinoModify);
			smallButtonCappuccinoModify.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					size = "S"; //Set size to small
					highlightSizeButton(smallButtonCappuccinoModify); //Hightlight small button
				}
			});

			smallButtonLatte = new JButton("<html><center>Small<br>" + Item.smallLattePrice + "0</center></html>"); //Initialize small button
			normalButtons.add(smallButtonLatte); //Add the button to the button ArrayList
			sizeButtons.add(smallButtonLatte);
			smallButtonLatte.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					size = "S"; //Set size to small
					highlightSizeButton(smallButtonLatte); //Hightlight small button
				}
			});
			
			smallButtonLatteModify = new JButton("<html><center>Small<br>" + Item.smallLattePrice + "0</center></html>"); //Initialize small button
			normalButtons.add(smallButtonLatteModify); //Add the button to the button ArrayList
			sizeButtons.add(smallButtonLatteModify);
			smallButtonLatteModify.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					size = "S"; //Set size to small
					highlightSizeButton(smallButtonLatteModify); //Hightlight small button
				}
			});

			smallButtonMocha = new JButton("<html><center>Small<br>" + Item.smallMochaPrice + "0</center></html>"); //Initialize small button
			normalButtons.add(smallButtonMocha); //Add the button to the button ArrayList
			sizeButtons.add(smallButtonMocha);
			smallButtonMocha.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					size = "S"; //Set size to small
					highlightSizeButton(smallButtonMocha); //Hightlight small button
				}
			});
			
			smallButtonMochaModify = new JButton("<html><center>Small<br>" + Item.smallMochaPrice + "0</center></html>"); //Initialize small button
			normalButtons.add(smallButtonMochaModify); //Add the button to the button ArrayList
			sizeButtons.add(smallButtonMochaModify);
			smallButtonMochaModify.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					size = "S"; //Set size to small
					highlightSizeButton(smallButtonMochaModify); //Hightlight small button
				}
			});

			smallButtonEspresso = new JButton("<html><center>Small<br>" + Item.smallEspressoPrice + "0</center></html>"); //Initialize small button
			normalButtons.add(smallButtonEspresso); //Add the button to the button ArrayList
			sizeButtons.add(smallButtonEspresso);
			smallButtonEspresso.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					size = "S"; //Set size to small
					highlightSizeButton(smallButtonEspresso); //Hightlight small button
				}
			});
			
			smallButtonEspressoModify = new JButton("<html><center>Small<br>" + Item.smallEspressoPrice + "0</center></html>"); //Initialize small button
			normalButtons.add(smallButtonEspressoModify); //Add the button to the button ArrayList
			sizeButtons.add(smallButtonEspressoModify);
			smallButtonEspressoModify.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					size = "S"; //Set size to small
					highlightSizeButton(smallButtonEspressoModify); //Hightlight small button
				}
			});

			smallButtonTea = new JButton("<html><center>Small<br>" + Item.teaPrice + "0</center></html>"); //Initialize small button
			normalButtons.add(smallButtonTea); //Add the button to the button ArrayList
			sizeButtons.add(smallButtonTea);
			smallButtonTea.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					size = "S"; //Set size to small
					highlightSizeButton(smallButtonTea); //Hightlight small button
				}
			});
			
			smallButtonTeaModify = new JButton("<html><center>Small<br>" + Item.teaPrice + "0</center></html>"); //Initialize small button
			normalButtons.add(smallButtonTeaModify); //Add the button to the button ArrayList
			sizeButtons.add(smallButtonTeaModify);
			smallButtonTeaModify.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					size = "S"; //Set size to small
					highlightSizeButton(smallButtonTeaModify); //Hightlight small button
				}
			});

			mediumButtonCappuccino = new JButton("<html><center>Medium<br>" + Item.mediumCappuccinoPrice + "0</center></html>"); //Initialize medium button
			normalButtons.add(mediumButtonCappuccino); //Add the button to the button ArrayList
			sizeButtons.add(mediumButtonCappuccino);
			mediumButtonCappuccino.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					size = "M";
					highlightSizeButton(mediumButtonCappuccino); //Hightlight medium button
				}
			});
			
			mediumButtonCappuccinoModify = new JButton("<html><center>Medium<br>" + Item.mediumCappuccinoPrice + "0</center></html>"); //Initialize medium button
			normalButtons.add(mediumButtonCappuccinoModify); //Add the button to the button ArrayList
			sizeButtons.add(mediumButtonCappuccinoModify);
			mediumButtonCappuccinoModify.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					size = "M";
					highlightSizeButton(mediumButtonCappuccinoModify); //Hightlight medium button
				}
			});

			mediumButtonLatte = new JButton("<html><center>Medium<br>" + Item.mediumLattePrice + "0</center></html>"); //Initialize medium button
			normalButtons.add(mediumButtonLatte); //Add the button to the button ArrayList
			sizeButtons.add(mediumButtonLatte);
			mediumButtonLatte.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					size = "M";
					highlightSizeButton(mediumButtonLatte); //Hightlight medium button
				}
			});
			
			mediumButtonLatteModify = new JButton("<html><center>Medium<br>" + Item.mediumLattePrice + "0</center></html>"); //Initialize medium button
			normalButtons.add(mediumButtonLatteModify); //Add the button to the button ArrayList
			sizeButtons.add(mediumButtonLatteModify);
			mediumButtonLatteModify.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					size = "M";
					highlightSizeButton(mediumButtonLatteModify); //Hightlight medium button
				}
			});

			mediumButtonMocha = new JButton("<html><center>Medium<br>" + Item.mediumMochaPrice + "0</center></html>"); //Initialize medium button
			normalButtons.add(mediumButtonMocha); //Add the button to the button ArrayList
			sizeButtons.add(mediumButtonMocha);
			mediumButtonMocha.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					size = "M";
					highlightSizeButton(mediumButtonMocha); //Hightlight medium button
				}
			});
			
			mediumButtonMochaModify = new JButton("<html><center>Medium<br>" + Item.mediumMochaPrice + "0</center></html>"); //Initialize medium button
			normalButtons.add(mediumButtonMochaModify); //Add the button to the button ArrayList
			sizeButtons.add(mediumButtonMochaModify);
			mediumButtonMochaModify.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					size = "M";
					highlightSizeButton(mediumButtonMochaModify); //Hightlight medium button
				}
			});

			mediumButtonTea = new JButton("<html><center>Medium<br>" + Item.teaPrice + "0</center></html>"); //Initialize medium button
			normalButtons.add(mediumButtonTea); //Add the button to the button ArrayList
			sizeButtons.add(mediumButtonTea);
			mediumButtonTea.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					size = "M";
					highlightSizeButton(mediumButtonTea); //Hightlight medium button
				}
			});
			
			mediumButtonTeaModify = new JButton("<html><center>Medium<br>" + Item.teaPrice + "0</center></html>"); //Initialize medium button
			normalButtons.add(mediumButtonTeaModify); //Add the button to the button ArrayList
			sizeButtons.add(mediumButtonTeaModify);
			mediumButtonTeaModify.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					size = "M";
					highlightSizeButton(mediumButtonTeaModify); //Hightlight medium button
				}
			});

			largeButtonCappuccino = new JButton("<html><center>Large<br>" + Item.largeCappuccinoPrice + "0</center></html>"); //Initialize large button
			normalButtons.add(largeButtonCappuccino); //Add the button to the button ArrayList
			sizeButtons.add(largeButtonCappuccino);
			largeButtonCappuccino.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					size = "L";
					highlightSizeButton(largeButtonCappuccino); //Hightlight large button
				}
			});
			
			largeButtonCappuccinoModify = new JButton("<html><center>Large<br>" + Item.largeCappuccinoPrice + "0</center></html>"); //Initialize large button
			normalButtons.add(largeButtonCappuccinoModify); //Add the button to the button ArrayList
			sizeButtons.add(largeButtonCappuccinoModify);
			largeButtonCappuccinoModify.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					size = "L";
					highlightSizeButton(largeButtonCappuccinoModify); //Hightlight large button
				}
			});

			largeButtonLatte = new JButton("<html><center>Large<br>" + Item.largeLattePrice + "0</center></html>"); //Initialize large button
			normalButtons.add(largeButtonLatte); //Add the button to the button ArrayList
			sizeButtons.add(largeButtonLatte);
			largeButtonLatte.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					size = "L";
					highlightSizeButton(largeButtonLatte); //Hightlight large button
				}
			});
			
			largeButtonLatteModify = new JButton("<html><center>Large<br>" + Item.largeLattePrice + "0</center></html>"); //Initialize large button
			normalButtons.add(largeButtonLatteModify); //Add the button to the button ArrayList
			sizeButtons.add(largeButtonLatteModify);
			largeButtonLatteModify.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					size = "L";
					highlightSizeButton(largeButtonLatteModify); //Hightlight large button
				}
			});

			largeButtonMocha = new JButton("<html><center>Large<br>" + Item.largeMochaPrice + "0</center></html>"); //Initialize large button
			normalButtons.add(largeButtonMocha); //Add the button to the button ArrayList
			sizeButtons.add(largeButtonMocha);
			largeButtonMocha.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					size = "L";
					highlightSizeButton(largeButtonMocha); //Hightlight large button
				}
			});
			
			largeButtonMochaModify = new JButton("<html><center>Large<br>" + Item.largeMochaPrice + "0</center></html>"); //Initialize large button
			normalButtons.add(largeButtonMochaModify); //Add the button to the button ArrayList
			sizeButtons.add(largeButtonMochaModify);
			largeButtonMochaModify.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					size = "L";
					highlightSizeButton(largeButtonMochaModify); //Hightlight large button
				}
			});

			largeButtonEspresso = new JButton("<html><center>Large<br>" + Item.largeEspressoPrice + "0</center></html>"); //Initialize large button
			normalButtons.add(largeButtonEspresso); //Add the button to the button ArrayList
			sizeButtons.add(largeButtonEspresso);
			largeButtonEspresso.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					size = "L";
					highlightSizeButton(largeButtonEspresso); //Hightlight large button
				}
			});
			
			largeButtonEspressoModify = new JButton("<html><center>Large<br>" + Item.largeEspressoPrice + "0</center></html>"); //Initialize large button
			normalButtons.add(largeButtonEspressoModify); //Add the button to the button ArrayList
			sizeButtons.add(largeButtonEspressoModify);
			largeButtonEspressoModify.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					size = "L";
					highlightSizeButton(largeButtonEspressoModify); //Hightlight large button
				}
			});
		
			largeButtonTea = new JButton("<html><center>Large<br>" + Item.teaPrice + "0</center></html>"); //Initialize large button
			normalButtons.add(largeButtonTea); //Add the button to the button ArrayList
			sizeButtons.add(largeButtonTea);
			largeButtonTea.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					size = "L";
					highlightSizeButton(largeButtonTea); //Hightlight large button
				}
			});
			
			largeButtonTeaModify = new JButton("<html><center>Large<br>" + Item.teaPrice + "0</center></html>"); //Initialize large button
			normalButtons.add(largeButtonTeaModify); //Add the button to the button ArrayList
			sizeButtons.add(largeButtonTeaModify);
			largeButtonTeaModify.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					size = "L";
					highlightSizeButton(largeButtonTeaModify); //Hightlight large button
				}
			});

			hotButtonCappuccino = new JButton("Hot"); //Initialize hot button
			normalButtons.add(hotButtonCappuccino); //Add the button to the button ArrayList
			styleButtons.add(hotButtonCappuccino);
			hotButtonCappuccino.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					style = "Hot";
					highlightStyleButton(hotButtonCappuccino); //Hightlight hot button
				}
			});

			hotButtonCappuccinoModify = new JButton("Hot"); //Initialize hot button
			normalButtons.add(hotButtonCappuccinoModify); //Add the button to the button ArrayList
			styleButtons.add(hotButtonCappuccinoModify);
			hotButtonCappuccinoModify.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					style = "Hot";
					highlightStyleButton(hotButtonCappuccinoModify); //Hightlight hot button
				}
			});

			hotButtonLatte = new JButton("Hot"); //Initialize hot button
			normalButtons.add(hotButtonLatte); //Add the button to the button ArrayList
			styleButtons.add(hotButtonLatte);
			hotButtonLatte.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					style = "Hot";
					highlightStyleButton(hotButtonLatte); //Hightlight hot button
				}
			});
			
			hotButtonLatteModify = new JButton("Hot"); //Initialize hot button
			normalButtons.add(hotButtonLatteModify); //Add the button to the button ArrayList
			styleButtons.add(hotButtonLatteModify);
			hotButtonLatteModify.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					style = "Hot";
					highlightStyleButton(hotButtonLatteModify); //Hightlight hot button
				}
			});

			hotButtonMocha = new JButton("Hot"); //Initialize hot button
			normalButtons.add(hotButtonMocha); //Add the button to the button ArrayList
			styleButtons.add(hotButtonMocha);
			hotButtonMocha.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					style = "Hot";
					highlightStyleButton(hotButtonMocha); //Hightlight hot button
				}
			});
			
			hotButtonMochaModify = new JButton("Hot"); //Initialize hot button
			normalButtons.add(hotButtonMochaModify); //Add the button to the button ArrayList
			styleButtons.add(hotButtonMochaModify);
			hotButtonMochaModify.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					style = "Hot";
					highlightStyleButton(hotButtonMochaModify); //Hightlight hot button
				}
			});

			hotButtonEspresso = new JButton("Hot"); //Initialize hot button
			normalButtons.add(hotButtonEspresso); //Add the button to the button ArrayList
			styleButtons.add(hotButtonEspresso);
			hotButtonEspresso.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					style = "Hot";
					highlightStyleButton(hotButtonEspresso); //Hightlight hot button
				}
			});
			
			hotButtonEspressoModify = new JButton("Hot"); //Initialize hot button
			normalButtons.add(hotButtonEspressoModify); //Add the button to the button ArrayList
			styleButtons.add(hotButtonEspressoModify);
			hotButtonEspressoModify.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					style = "Hot";
					highlightStyleButton(hotButtonEspressoModify); //Hightlight hot button
				}
			});

			hotButtonTea = new JButton("Hot"); //Initialize hot button
			normalButtons.add(hotButtonTea); //Add the button to the button ArrayList
			styleButtons.add(hotButtonTea);
			hotButtonTea.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					style = "Hot";
					highlightStyleButton(hotButtonTea); //Hightlight hot button
				}
			});
			
			hotButtonTeaModify = new JButton("Hot"); //Initialize hot button
			normalButtons.add(hotButtonTeaModify); //Add the button to the button ArrayList
			styleButtons.add(hotButtonTeaModify);
			hotButtonTeaModify.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					style = "Hot";
					highlightStyleButton(hotButtonTeaModify); //Hightlight hot button
				}
			});
			
			icedButtonModify = new JButton("Iced"); //Initialize iced button
			normalButtons.add(icedButtonModify); //Add the button to the button ArrayList
			styleButtons.add(icedButtonModify);
			icedButtonModify.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					style = "Iced";
					highlightStyleButton(icedButtonModify); //Hightlight iced button
				}
			});

			icedButtonLatte = new JButton("Iced"); //Initialize iced button
			normalButtons.add(icedButtonLatte); //Add the button to the button ArrayList
			styleButtons.add(icedButtonLatte);
			icedButtonLatte.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					style = "Iced";
					highlightStyleButton(icedButtonLatte); //Hightlight iced button
				}
			});
			
			icedButtonLatteModify = new JButton("Iced"); //Initialize iced button
			normalButtons.add(icedButtonLatteModify); //Add the button to the button ArrayList
			styleButtons.add(icedButtonLatteModify);
			icedButtonLatteModify.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					style = "Iced";
					highlightStyleButton(icedButtonLatteModify); //Hightlight iced button
				}
			});

			icedButtonMocha = new JButton("Iced"); //Initialize iced button
			normalButtons.add(icedButtonMocha); //Add the button to the button ArrayList
			styleButtons.add(icedButtonMocha);
			icedButtonMocha.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					style = "Iced";
					highlightStyleButton(icedButtonMocha); //Hightlight iced button
				}
			});
			
			icedButtonMochaModify = new JButton("Iced"); //Initialize iced button
			normalButtons.add(icedButtonMochaModify); //Add the button to the button ArrayList
			styleButtons.add(icedButtonMochaModify);
			icedButtonMochaModify.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					style = "Iced";
					highlightStyleButton(icedButtonMochaModify); //Hightlight iced button
				}
			});

			icedButtonTea = new JButton("Iced"); //Initialize iced button
			normalButtons.add(icedButtonTea); //Add the button to the button ArrayList
			styleButtons.add(icedButtonTea);
			icedButtonTea.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					style = "Iced";
					highlightStyleButton(icedButtonTea); //Hightlight iced button
				}
			});
			
			icedButtonTeaModify = new JButton("Iced"); //Initialize iced button
			normalButtons.add(icedButtonTeaModify); //Add the button to the button ArrayList
			styleButtons.add(icedButtonTeaModify);
			icedButtonTeaModify.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					style = "Iced";
					highlightStyleButton(icedButtonTeaModify); //Hightlight iced button
				}
			});
			
			blendedButtonModify = new JButton("Blended"); //Initialize blended button
			normalButtons.add(blendedButtonModify); //Add the button to the button ArrayList
			styleButtons.add(blendedButtonModify);
			blendedButtonModify.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					style = "Blended";
					highlightStyleButton(blendedButtonModify); //Hightlight blended button
				}
			});

			blendedButtonLatte = new JButton("Blended"); //Initialize blended button
			normalButtons.add(blendedButtonLatte); //Add the button to the button ArrayList
			styleButtons.add(blendedButtonLatte);
			blendedButtonLatte.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					style = "Blended";
					highlightStyleButton(blendedButtonLatte); //Hightlight blended button
				}
			});
			
			blendedButtonLatteModify = new JButton("Blended"); //Initialize blended button
			normalButtons.add(blendedButtonLatteModify); //Add the button to the button ArrayList
			styleButtons.add(blendedButtonLatteModify);
			blendedButtonLatteModify.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					style = "Blended";
					highlightStyleButton(blendedButtonLatteModify); //Hightlight blended button
				}
			});

			blendedButtonMocha = new JButton("Blended"); //Initialize blended button
			normalButtons.add(blendedButtonMocha); //Add the button to the button ArrayList
			styleButtons.add(blendedButtonMocha);
			blendedButtonMocha.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					style = "Blended";
					highlightStyleButton(blendedButtonMocha); //Hightlight blended button
				}
			});
			
			blendedButtonMochaModify = new JButton("Blended"); //Initialize blended button
			normalButtons.add(blendedButtonMochaModify); //Add the button to the button ArrayList
			styleButtons.add(blendedButtonMochaModify);
			blendedButtonMochaModify.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					style = "Blended";
					highlightStyleButton(blendedButtonMochaModify); //Hightlight blended button
				}
			});

			blendedButtonTea = new JButton("Blended"); //Initialize blended button
			normalButtons.add(blendedButtonTea); //Add the button to the button ArrayList
			styleButtons.add(blendedButtonTea);
			blendedButtonTea.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					style = "Blended";
					highlightStyleButton(blendedButtonTea); //Hightlight blended button
				}
			});
			
			blendedButtonTeaModify = new JButton("Blended"); //Initialize blended button
			normalButtons.add(blendedButtonTeaModify); //Add the button to the button ArrayList
			styleButtons.add(blendedButtonTeaModify);
			blendedButtonTeaModify.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					style = "Blended";
					highlightStyleButton(blendedButtonTeaModify); //Hightlight blended button
				}
			});

			cappuccinoButton = new JButton("Cappuccino"); //Initialize cappuccino button
			imageButtons.add(cappuccinoButton); //Add the button to the button ArrayList
			setButtonImage(cappuccinoButton, cappuccinoImage); //Add the icon to the button
			cappuccinoButton.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					screen = "Cappuccino";
					type = "Cappuccino";
					style = "Hot";
					highlightStyleButton(hotButtonCappuccino); //Hightlight hot button
					render();
				}
			});

			latteButton = new JButton("Latte"); //Initialize latte button
			imageButtons.add(latteButton); //Add the button to the button ArrayList
			setButtonImage(latteButton, latteImage); //Add the icon to the button
			latteButton.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					screen = "Latte";
					type = "Latte";
					render();
				}
			});

			mochaButton = new JButton("Mocha"); //Initialize mocha button
			imageButtons.add(mochaButton); //Add the button to the button ArrayList
			setButtonImage(mochaButton, mochaImage); //Add the icon to the button
			mochaButton.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					screen = "Mocha";
					type = "Mocha";
					render();
				}
			});

			espressoButton = new JButton("Espresso"); //Initialize espresso button
			imageButtons.add(espressoButton); //Add the button to the button ArrayList
			setButtonImage(espressoButton, espressoImage); //Add the icon to the button
			espressoButton.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					screen = "Espresso";
					type = "Espresso";
					style = "Hot";
					highlightStyleButton(hotButtonEspresso);  //Hightlight hot button
					render();
				}
			});

			teaButton = new JButton("Tea"); //Initialize tea button
			imageButtons.add(teaButton); //Add the button to the button ArrayList
			setButtonImage(teaButton, teaImage); //Add the icon to the button
			teaButton.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					screen = "Tea";
					type = "Tea";
					render();
				}
			});

		foodButton = new JButton("Food"); //Initialize food button
		imageButtons.add(foodButton); //Add the button to the button ArrayList
		setButtonImage(foodButton, foodImage); //Add the icon to the button
		foodButton.addActionListener(new ActionListener(){
		
			@Override
			public void actionPerformed(ActionEvent e) {
				SoundEffect.CLICK.play(); //Play click sound
				screen = "Food";
				render();
			}
		});

			cremaRollButton = new JButton("<html><center>Crema Roll<br>" + Item.cremaRollPrice + "0</center></html>"); //Initialize crema roll button
			imageButtons.add(cremaRollButton); //Add the button to the button ArrayList
			setButtonImage(cremaRollButton, cremaRollImage); //Add the icon to the button
			cremaRollButton.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					tempOrder.items.add(new Item("Crema Roll"));
					screen = "New Order"; //Switch to New Order screen
					removed = false; //Item not yet removed
					render();
				}
			});
			
			cremaRollButtonModify = new JButton("<html><center>Crema Roll<br>" + Item.cremaRollPrice + "0</center></html>"); //Initialize crema roll button
			imageButtons.add(cremaRollButtonModify); //Add the button to the button ArrayList
			setButtonImage(cremaRollButtonModify, cremaRollImage); //Add the icon to the button
			cremaRollButtonModify.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					tempOrder.items.add(new Item("Crema Roll"));
					screen = "New Order"; //Switch to New Order screen
					removed = false; //Item not yet removed
					render();
				}
			});

			cookieButton = new JButton("<html><center>Cookie<br>" + Item.cookiePrice + "0</center></html>"); //Initialize cookie button
			imageButtons.add(cookieButton); //Add the button to the button ArrayList
			setButtonImage(cookieButton, cookieImage); //Add the icon to the button
			cookieButton.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					tempOrder.items.add(new Item("Cookie"));
					screen = "New Order"; //Switch to New Order screen
					removed = false; //Item not yet removed
					render();
				}
			});

			cookieButtonModify = new JButton("<html><center>Cookie<br>" + Item.cookiePrice + "0</center></html>"); //Initialize cookie button
			imageButtons.add(cookieButtonModify); //Add the button to the button ArrayList
			setButtonImage(cookieButtonModify, cookieImage); //Add the icon to the button
			cookieButtonModify.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					tempOrder.items.add(new Item("Cookie"));
					screen = "New Order"; //Switch to New Order screen
					removed = false; //Item not yet removed
					render();
				}
			});

			cheesecakeButton = new JButton("<html><center>Cheesecake<br>" + Item.cheesecakePrice + "0</center></html>"); //Initialize cheesecake button
			imageButtons.add(cheesecakeButton); //Add the button to the button ArrayList
			setButtonImage(cheesecakeButton, cheesecakeImage); //Add the icon to the button
			cheesecakeButton.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					tempOrder.items.add(new Item("Cheesecake"));
					screen = "New Order"; //Switch to New Order screen
					removed = false; //Item not yet removed
					render();
				}
			});
			
			cheesecakeButtonModify = new JButton("<html><center>Cheesecake<br>" + Item.cheesecakePrice + "0</center></html>"); //Initialize cheesecake button
			imageButtons.add(cheesecakeButtonModify); //Add the button to the button ArrayList
			setButtonImage(cheesecakeButtonModify, cheesecakeImage); //Add the icon to the button
			cheesecakeButtonModify.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					tempOrder.items.add(new Item("Cheesecake"));
					screen = "New Order"; //Switch to New Order screen
					removed = false; //Item not yet removed
					render();
				}
			});

			affogatoButton = new JButton("<html><center>Affogato<br>" + Item.affogatoPrice + "0</center></html>"); //Initialize affogato button
			imageButtons.add(affogatoButton); //Add the button to the button ArrayList
			setButtonImage(affogatoButton, affogatoImage); //Add the icon to the button
			affogatoButton.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					tempOrder.items.add(new Item("Affogato"));
					screen = "New Order"; //Switch to New Order screen
					removed = false; //Item not yet removed
					render();
				}
			});
			
			affogatoButtonModify = new JButton("<html><center>Affogato<br>" + Item.affogatoPrice + "0</center></html>"); //Initialize affogato button
			imageButtons.add(affogatoButtonModify); //Add the button to the button ArrayList
			setButtonImage(affogatoButtonModify, affogatoImage); //Add the icon to the button
			affogatoButtonModify.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					tempOrder.items.add(new Item("Affogato"));
					screen = "New Order"; //Switch to New Order screen
					removed = false; //Item not yet removed
					render();
				}
			});
			
			gelatoButton = new JButton("<html><center>Gelato<br>" + Item.gelatoPrice + "0</center></html>"); //Initialize gelato button
			imageButtons.add(gelatoButton); //Add the button to the button ArrayList
			setButtonImage(gelatoButton, gelatoImage); //Add the icon to the button
			gelatoButton.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					tempOrder.items.add(new Item("Gelato"));
					screen = "New Order"; //Switch to New Order screen
					removed = false; //Item not yet removed
					render();
				}
			});
			
			gelatoButtonModify = new JButton("<html><center>Gelato<br>" + Item.gelatoPrice + "0</center></html>"); //Initialize gelato button
			imageButtons.add(gelatoButtonModify); //Add the button to the button ArrayList
			setButtonImage(gelatoButtonModify, gelatoImage); //Add the icon to the button
			gelatoButtonModify.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					tempOrder.items.add(new Item("Gelato"));
					screen = "New Order"; //Switch to New Order screen
					removed = false; //Item not yet removed
					render();
				}
			});

		payButton = new JButton("Pay"); //Initialize pay button
		normalButtons.add(payButton); //Add the button to the button ArrayList
		payButton.addActionListener(new ActionListener(){
		
			@Override
			public void actionPerformed(ActionEvent e) {
				SoundEffect.CLICK.play(); //Play click sound
				screen = "Pay"; //Switch to Pay screen
				render();
			}
		});

		//Not implemented
		refundOrderButton = new JButton("Refund\nOrder"); //Initialize refund order button
		normalButtons.add(refundOrderButton); //Add the button to the button ArrayList
		refundOrderButton.addActionListener(new ActionListener(){
		
			@Override
			public void actionPerformed(ActionEvent e) {
				SoundEffect.CLICK.play(); //Play click sound
				//screen = "Refund Order"; //Switch to Refund Order Screen
				//render();
			}
		});

		cancelOrderButton = new JButton("Cancel\nOrder"); //Initialize cancel order button
		normalButtons.add(cancelOrderButton); //Add the button to the button ArrayList
		cancelOrderButton.addActionListener(new ActionListener(){
		
			@Override
			public void actionPerformed(ActionEvent e) {
				SoundEffect.CLICK.play(); //Play click sound
				JOptionPane optionPane = new JOptionPane("Are you sure you want to cancel the order?", JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION); //Warn user
				JDialog dialog = optionPane.createDialog(frame, "Cancel order"); //Set up dialog
				dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE); //Don't let the user x out of the dialog
				dialog.setVisible(true); //Set visible
				int value = ((Integer)optionPane.getValue()).intValue(); //Get the users choice
				if(value == JOptionPane.NO_OPTION) { //If they click no
					dialog.setVisible(false); //Remove dialog
				}
				else if (value == JOptionPane.YES_OPTION) { //If they click yes
					screen = "Home"; //Switch to home screen
					totalOrders--; //Decrement totalOrders because we aren't going to use this order
					tempOrder.items.clear(); //Erase the order
					render();
				}
			}
		});

		cancelOrderButtonPay = new JButton("Cancel\nOrder"); //Initialize cancel order button
		normalButtons.add(cancelOrderButtonPay); //Add the button to the button ArrayList
		cancelOrderButtonPay.addActionListener(new ActionListener(){
		
			@Override
			public void actionPerformed(ActionEvent e) {
				SoundEffect.CLICK.play(); //Play click sound
				JOptionPane optionPane = new JOptionPane("Are you sure you want to cancel the order?", JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION); //Warn user
				JDialog dialog = optionPane.createDialog(frame, "Cancel order"); //Set up dialog
				dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE); //Don't let the user x out of the dialog
				dialog.setVisible(true); //Set visible
				int value = ((Integer)optionPane.getValue()).intValue(); //Get the users choice
				if(value == JOptionPane.NO_OPTION) { //If they click no
					dialog.setVisible(false); //Remove dialog
				}
				else if (value == JOptionPane.YES_OPTION) {
					screen = "Home"; //Switch to home screen
					totalOrders--; //Decrement totalOrders because we aren't going to use this order
					tempOrder.items.clear(); //Erase the order
					render();
				}
			}
		});

		cashButton = new JButton("Cash"); //Initialize cash button
		normalButtons.add(cashButton); //Add the button to the button ArrayList
		cashButton.addActionListener(new ActionListener(){
		
			@Override
			public void actionPerformed(ActionEvent e) {
				SoundEffect.CLICK.play(); //Play click sound
				customerNameGiven = false; //The customer name isn't entered yet
				if(!customerNameGiven) {
					String customerName = JOptionPane.showInputDialog(frame, "Please enter the customer's name"); //Ask user for customer name
					if(customerName != null || customerName != "") { //If the input is not null or blank
						tempOrder.customerName = customerName; //Assign name
					}
					if(customerName == null || customerName == "") { //If the input is null or blank
						tempOrder.customerName = defaultName; //Assign the default name
					}
					customerNameGiven = true; //Name has been given
				}
				//While loop to handle user input to make sure integers are entered
				while(true) {
					amountPaid = JOptionPane.showInputDialog(frame, "Amount due: $" + df.format(tempOrder.total) + "\nEnter amount tendered:"); //Ask for payment amount
					try {
						amountPaidBD = new BigDecimal(Float.toString(Float.parseFloat(amountPaid))); //Calculate amount paid in BD
						amountPaidBD = amountPaidBD.setScale(2, BigDecimal.ROUND_HALF_UP); //Round it to 2 decimal places
						tempOrderTotalBD = new BigDecimal(Double.toString(tempOrder.total)); //Calculate the tempOrder total in BD
						tempOrderTotalBD = tempOrderTotalBD.setScale(2, BigDecimal.ROUND_HALF_UP); //Round it to 2 decimal places
						total = tempOrderTotalBD.subtract(amountPaidBD, new MathContext(2));//Calculate their payment for remainder
						break; //If it's an integer break out of the while loop
					}
					catch (NumberFormatException ex) {
						JOptionPane.showMessageDialog(frame, "Please only enter numbers"); //Tell the user to only enter numbers
					}
					catch (NullPointerException npe) {
						//Cancel is disabled
					}
				}
				while(!tempOrder.paymentDone) { //Loop until payment is done
					System.out.println("payment not done");
					//If the customer has paid exact change
					if(total.compareTo(BigDecimal.ZERO) == 0.00) {
						JOptionPane submittedMessage = new JOptionPane("Order has been submitted", JOptionPane.INFORMATION_MESSAGE); //Tell user the order has been submitted
						JDialog orderCompleteDialog = submittedMessage.createDialog(frame, "Order Complete"); //Create dialog
						//Spin a new thread that will close the dialog after 2 seconds so the user does not need to press OK
						new Thread(new Runnable() {
							public void run() {
								try {
									Thread.sleep(2000); //Wait 2 seconds
									orderCompleteDialog.dispose(); //Disposed of the dialog
									screen = "Home"; //Go to home screen
									pastOrders.add(tempOrder); //Add the order to past orders ArrayList - future implementation
									tempOrder.items.clear(); //Clear the tempOrder
									render();
								}
								catch (Throwable e) {
									e.printStackTrace();
								}
							}
						}).start(); //Start the thread
						orderCompleteDialog.setVisible(true); //Show the dialog saying that the order is complete
						tempOrder.paymentDone = true; //Payment is finished
						render();
					}
					//If customer has over paid
					if(total.compareTo(BigDecimal.ZERO) < 0) {
						JOptionPane.showMessageDialog(frame, "Total change due: $" + df.format(total.negate())); //Tell user how much change is due
						JOptionPane submittedMessage = new JOptionPane("Order has been submitted", JOptionPane.INFORMATION_MESSAGE); //Tell user the order has been submitted
						JDialog orderCompleteDialog = submittedMessage.createDialog(frame, "Order Complete"); //Create dialog
						//Spin a new thread that will close the dialog after 2 seconds so the user does not need to press OK
						new Thread(new Runnable() {
							public void run() {
								try {
									Thread.sleep(2000); //Wait 2 seconds
									orderCompleteDialog.dispose(); //Disposed of the dialog
									screen = "Home"; //Go to home screen
									pastOrders.add(tempOrder); //Add the order to past orders ArrayList - future implementation
									tempOrder.items.clear(); //Clear the tempOrder
									render();
								}
								catch (Throwable e) {
									e.printStackTrace();
								}
							}
						}).start(); //Start the thread
						orderCompleteDialog.setVisible(true); //Show the dialog saying that the order is complete
						tempOrder.paymentDone = true; //Payment is finished
						render();
					}
					//If the customer hasn't paid enough
					if(total.compareTo(BigDecimal.ZERO) > 0) {
						while(true) {
							amountPaid = JOptionPane.showInputDialog(frame, "$" + df.format(total) + " still owed.\nEnter amount tendered:"); //Show how much is left to pay, ask for next payment
							try {
								amountPaidBD = new BigDecimal(Float.toString(Float.parseFloat(amountPaid))); //Calculate amount paid in BD
								amountPaidBD = amountPaidBD.setScale(2, BigDecimal.ROUND_HALF_UP); //Round it to 2 decimal places
								total = total.subtract(amountPaidBD, new MathContext(2));//Calculate their payment for remainder
								break; //If it's an integer break out of the while loop
							}
							catch (NumberFormatException ex) {
								JOptionPane.showMessageDialog(frame, "Please only enter numbers"); //Tell the user to only enter numbers
							}
							catch (NullPointerException npe) {
								//Cancel is disabled
							}
						}
					}
				}
			}
		});

		creditButton = new JButton("Credit"); //Initialize credit button
		normalButtons.add(creditButton); //Add the button to the button ArrayList
		creditButton.addActionListener(new ActionListener(){
		
			@Override
			public void actionPerformed(ActionEvent e) {
				SoundEffect.CLICK.play(); //Play click sound
				customerNameGiven = false; //The customer name isn't entered yet
				if(!customerNameGiven) {
					String customerName = JOptionPane.showInputDialog(frame, "Please enter the customer's name"); //Ask user for customer name
					if(customerName != null || customerName != "") { //If the input is not null or blank
						tempOrder.customerName = customerName; //Assign name
					}
					if(customerName == null || customerName == "") { //If the input is null or blank
						tempOrder.customerName = defaultName; //Assign the default name
					}
					customerNameGiven = true; //Name has been given
				}
				
				JOptionPane waitForCreditMessage = new JOptionPane("Waiting for " + tempOrder.customerName + "'s credit card payment", JOptionPane.INFORMATION_MESSAGE); //Tell user we're waiting for customer to pay
				JDialog waitingForCreditDialog = waitForCreditMessage.createDialog(frame, "Waiting on customer"); //Create dialog
				JOptionPane processingCreditMessage = new JOptionPane("Processing " + tempOrder.customerName + "'s credit card", JOptionPane.INFORMATION_MESSAGE); //Tell user we're waiting for payment to process
				JDialog processingCreditDialog = processingCreditMessage.createDialog(frame, "Proccessing"); //Create dialog
				JOptionPane doneCreditMessage = new JOptionPane(tempOrder.customerName + "'s payment complete\nOrder submitted", JOptionPane.INFORMATION_MESSAGE); //Tell user payment is done
				JDialog doneCreditDialog = doneCreditMessage.createDialog(frame, "Payment complete"); //Create dialog

				//Spin a new thread that will close the waiting after 2 seconds so the user does not need to press OK
				new Thread(new Runnable() {
					public void run() {
						try {
							Thread.sleep(2000); //Wait 2 seconds
							waitingForCreditDialog.dispose(); //Dispose dialog
						}
						catch (Throwable e) {
							e.printStackTrace();
						}
					}
				}).start();
				waitingForCreditDialog.setVisible(true); //Show the dialog saying that the order is complete
				
				new Thread(new Runnable() {
					public void run() {
						try {
							Thread.sleep(2000); //Wait 2 seconds
							processingCreditDialog.dispose(); //Dispose dialog
						}
						catch (Throwable e) {
							e.printStackTrace();
						}
					}
				}).start(); //Start the thread
				processingCreditDialog.setVisible(true); //Show the dialog saying that the order is complete
				
				new Thread(new Runnable() {
					public void run() {
						try {
							Thread.sleep(2000); //Wait 2 seconds
							doneCreditDialog.dispose(); //Dispose dialog
							screen = "Home"; //Switch to Home screen
							pastOrders.add(tempOrder); //Add tempOrder to pastOrders - Future implementation
							tempOrder.items.clear(); //Clear the tempOrder
							render();
						}
						catch (Throwable e) {
							e.printStackTrace();
						}
					}
				}).start(); //Start the thread
				doneCreditDialog.setVisible(true); //Show the dialog saying that the order is complete
			}
		});
		giftCardButton = new JButton("Gift\nCard"); //Initialize gift card button
		normalButtons.add(giftCardButton); //Add the button to the button ArrayList
		giftCardButton.addActionListener(new ActionListener(){
		
			@Override
			public void actionPerformed(ActionEvent e) {
				SoundEffect.CLICK.play(); //Play click sound
				customerNameGiven = false; //The customer name isn't entered yet
				if(!customerNameGiven) {
					String customerName = JOptionPane.showInputDialog(frame, "Please enter the customer's name"); //Ask user for customer name
					if(customerName != null || customerName != "") { //If the input is not null or blank
						tempOrder.customerName = customerName; //Assign name
					}
					if(customerName == null || customerName == "") { //If the input is null or blank
						tempOrder.customerName = defaultName; //Assign the default name
					}
					customerNameGiven = true; //Name has been given
				}
				
				JOptionPane waitForGiftCardMessage = new JOptionPane("Waiting for " + tempOrder.customerName + "'s gift card payment", JOptionPane.INFORMATION_MESSAGE);
				JDialog waitingForGiftCardDialog = waitForGiftCardMessage.createDialog(frame, "Waiting on customer");
				JOptionPane processingGiftCardMessage = new JOptionPane("Processing " + tempOrder.customerName + "'s gift card", JOptionPane.INFORMATION_MESSAGE);
				JDialog processingGiftCardDialog = processingGiftCardMessage.createDialog(frame, "Proccessing");
				JOptionPane doneGiftCardMessage = new JOptionPane(tempOrder.customerName + "'s payment complete\nOrder submitted", JOptionPane.INFORMATION_MESSAGE);
				JDialog doneGiftCardDialog = doneGiftCardMessage.createDialog(frame, "Payment complete");

				//Spin a new thread that will close the waiting after 2 seconds so the user does not need to press OK
				new Thread(new Runnable() {
					public void run() {
						try {
							Thread.sleep(2000);
							waitingForGiftCardDialog.dispose();
						}
						catch (Throwable e) {
							e.printStackTrace();
						}
					}
				}).start();
				waitingForGiftCardDialog.setVisible(true); //Show the dialog saying that the order is complete
				
				new Thread(new Runnable() {
					public void run() {
						try {
							Thread.sleep(2000);
							processingGiftCardDialog.dispose();
						}
						catch (Throwable e) {
							e.printStackTrace();
						}
					}
				}).start();
				processingGiftCardDialog.setVisible(true); //Show the dialog saying that the order is complete
				
				new Thread(new Runnable() {
					public void run() {
						try {
							Thread.sleep(2000);
							doneGiftCardDialog.dispose();
							screen = "Home";
							pastOrders.add(tempOrder);
							tempOrder.items.clear();
							render();
						}
						catch (Throwable e) {
							e.printStackTrace();
						}
					}
				}).start();
				doneGiftCardDialog.setVisible(true); //Show the dialog saying that the order is complete
			}
		});

		addButton = new JButton("Add");
		normalButtons.add(addButton);
		addButton.addActionListener(new ActionListener(){
		
			@Override
			public void actionPerformed(ActionEvent e) {
				SoundEffect.CLICK.play(); //Play click sound
				tempOrder.items.add(new Item(size, type, style));
				screen = "New Order"; //Switch to New Order screen
				removed = false; //Item not yet removed
				render();
			}
		});

		addButtonCappuccino = new JButton("Add");
		normalButtons.add(addButtonCappuccino);
		addButtonCappuccino.addActionListener(new ActionListener(){
		
			@Override
			public void actionPerformed(ActionEvent e) {
				SoundEffect.CLICK.play(); //Play click sound
				tempOrder.items.add(new Item(size, type, style));
				screen = "New Order"; //Switch to New Order screen
				removed = false; //Item not yet removed
				render();
			}
		});

		updateButtonCappuccinoModify = new JButton("Update");
		normalButtons.add(updateButtonCappuccinoModify);
		updateButtonCappuccinoModify.addActionListener(new ActionListener(){
		
			@Override
			public void actionPerformed(ActionEvent e) {
				SoundEffect.CLICK.play(); //Play click sound
				tempOrder.items.add(new Item(size, type, style));
				screen = "New Order"; //Switch to New Order screen
				removed = false; //Item not yet removed
				render();
			}
		});

		addButtonLatte = new JButton("Add");
		normalButtons.add(addButtonLatte);
		addButtonLatte.addActionListener(new ActionListener(){
		
			@Override
			public void actionPerformed(ActionEvent e) {
				SoundEffect.CLICK.play(); //Play click sound
				tempOrder.items.add(new Item(size, type, style));
				screen = "New Order"; //Switch to New Order screen
				removed = false; //Item not yet removed
				render();
			}
		});

		updateButtonLatteModify = new JButton("Update");
		normalButtons.add(updateButtonLatteModify);
		updateButtonLatteModify.addActionListener(new ActionListener(){
		
			@Override
			public void actionPerformed(ActionEvent e) {
				SoundEffect.CLICK.play(); //Play click sound
				tempOrder.items.add(new Item(size, type, style));
				screen = "New Order"; //Switch to New Order screen
				removed = false; //Item not yet removed
				render();
			}
		});

		addButtonMocha = new JButton("Add");
		normalButtons.add(addButtonMocha);
		addButtonMocha.addActionListener(new ActionListener(){
		
			@Override
			public void actionPerformed(ActionEvent e) {
				SoundEffect.CLICK.play(); //Play click sound
				tempOrder.items.add(new Item(size, type, style));
				screen = "New Order"; //Switch to New Order screen
				removed = false; //Item not yet removed
				render();
			}
		});
		
		updateButtonMochaModify = new JButton("Update");
		normalButtons.add(updateButtonMochaModify);
		updateButtonMochaModify.addActionListener(new ActionListener(){
		
			@Override
			public void actionPerformed(ActionEvent e) {
				SoundEffect.CLICK.play(); //Play click sound
				tempOrder.items.add(new Item(size, type, style));
				screen = "New Order"; //Switch to New Order screen
				removed = false; //Item not yet removed
				render();
			}
		});

		addButtonEspresso = new JButton("Add");
		normalButtons.add(addButtonEspresso);
		addButtonEspresso.addActionListener(new ActionListener(){
		
			@Override
			public void actionPerformed(ActionEvent e) {
				SoundEffect.CLICK.play(); //Play click sound
				tempOrder.items.add(new Item(size, type, style));
				screen = "New Order"; //Switch to New Order screen
				removed = false; //Item not yet removed
				render();
			}
		});

		updateButtonEspressoModify = new JButton("Update");
		normalButtons.add(updateButtonEspressoModify);
		updateButtonEspressoModify.addActionListener(new ActionListener(){
		
			@Override
			public void actionPerformed(ActionEvent e) {
				SoundEffect.CLICK.play(); //Play click sound
				tempOrder.items.add(new Item(size, type, style));
				screen = "New Order"; //Switch to New Order screen
				removed = false; //Item not yet removed
				render();
			}
		});

		addButtonTea = new JButton("Add");
		normalButtons.add(addButtonTea);
		addButtonTea.addActionListener(new ActionListener(){
		
			@Override
			public void actionPerformed(ActionEvent e) {
				SoundEffect.CLICK.play(); //Play click sound
				tempOrder.items.add(new Item(size, type, style));
				screen = "New Order"; //Switch to New Order screen
				removed = false; //Item not yet removed
				render();
			}
		});
		
		updateButtonTeaModify = new JButton("Update");
		normalButtons.add(updateButtonTeaModify);
		updateButtonTeaModify.addActionListener(new ActionListener(){
		
			@Override
			public void actionPerformed(ActionEvent e) {
				SoundEffect.CLICK.play(); //Play click sound
				tempOrder.items.add(new Item(size, type, style));
				screen = "New Order"; //Switch to New Order screen
				removed = false; //Item not yet removed
				render();
			}
		});
		
		completeButton = new JButton("Complete");
		normalButtons.add(completeButton);
		completeButton.addActionListener(new ActionListener(){
		
			@Override
			public void actionPerformed(ActionEvent e) {
				SoundEffect.CLICK.play(); //Play click sound
				currentOrders.add(tempOrder); //Add tempOrder to currentOrders - Future implementation
			}
		});

		//Set defaults for each image button, text is centered and at bottom
		for(int i = 0; i < imageButtons.size(); i++) {
			imageButtons.get(i).setPreferredSize(new Dimension(BUTTONWIDTH, BUTTONHEIGHT)); //Set the button size
			imageButtons.get(i).setVerticalTextPosition(SwingConstants.BOTTOM); //Set the button vertical text position
			imageButtons.get(i).setHorizontalTextPosition(SwingConstants.CENTER); //Set the button horizontal text position
			imageButtons.get(i).setFont(font); //Set the button font
			imageButtons.get(i).setFocusPainted(false); //Prevent button from being highlighted around the edges
			imageButtons.get(i).setVisible(true); //Set visible
		}
		//Set defaults for each non-image button, text is centered and half button height
		for(int i = 0; i < normalButtons.size(); i++) {
			normalButtons.get(i).setPreferredSize(new Dimension(BUTTONWIDTH, BUTTONHEIGHT/2)); //Set the button size
			normalButtons.get(i).setFont(font); //Set the button font
			normalButtons.get(i).setFocusPainted(false); //Prevent button from being highlighted around the edges
			normalButtons.get(i).setVisible(true); //Set visible
		}

		//Panel Bounds------------------------------------------//
		homePanel.setBounds(0, 0, WIDTH, (int)HEIGHT); //Set panel bounds
		newOrderPanel.setBounds(0, 0, WIDTH, (int)HEIGHT); //New order panel bounds
		drinksPanel.setBounds(0, 0, WIDTH, (int)HEIGHT); //Drinks panel bounds
		cappuccinoPanel.setBounds(0, 0, WIDTH, (int)HEIGHT); //Cappuccino panel bounds
		lattePanel.setBounds(0, 0, WIDTH, (int)HEIGHT); //Latte panel bounds
		mochaPanel.setBounds(0, 0, WIDTH, (int)HEIGHT); //Mocha panel bounds
		espressoPanel.setBounds(0, 0, WIDTH, (int)HEIGHT); //Espresso panel bounds
		teaPanel.setBounds(0, 0, WIDTH, (int)HEIGHT); //Tea panel bounds
		cappuccinoPanelModify.setBounds(0, 0, WIDTH, (int)HEIGHT); //Cappuccino panel bounds
		lattePanelModify.setBounds(0, 0, WIDTH, (int)HEIGHT); //Latte panel bounds
		mochaPanelModify.setBounds(0, 0, WIDTH, (int)HEIGHT); //Mocha panel bounds
		espressoPanelModify.setBounds(0, 0, WIDTH, (int)HEIGHT); //Espresso panel bounds
		teaPanelModify.setBounds(0, 0, WIDTH, (int)HEIGHT); //Tea panel bounds
		foodPanel.setBounds(0, 0, WIDTH, (int)HEIGHT); //Food panel bounds
		foodPanelModify.setBounds(0, 0, WIDTH, (int)HEIGHT); //Food panel bounds
		cremaRollPanel.setBounds(0, 0, WIDTH, (int)HEIGHT); //Crema roll panel bounds
		cookiePanel.setBounds(0, 0, WIDTH, (int)HEIGHT); //Cookie panel bounds
		cheesecakePanel.setBounds(0, 0, WIDTH, (int)HEIGHT); //Cheesecake panel bounds
		affogatoPanel.setBounds(0, 0, WIDTH, (int)HEIGHT); //Affogato panel bounds
		gelatoPanel.setBounds(0, 0, WIDTH, (int)HEIGHT); //Gelato panel bounds
		payPanel.setBounds(0, 0, WIDTH, (int)HEIGHT); //Pay panel bounds
		refundOrderPanel.setBounds(0, 0, WIDTH, (int)HEIGHT); //Refund Order panel bounds
		cashPanel.setBounds(0, 0, WIDTH, (int)HEIGHT); //Cash panel bounds
		creditPanel.setBounds(0, 0, WIDTH, (int)HEIGHT); //Credit panel bounds
		giftCardPanel.setBounds(0, 0, WIDTH, (int)HEIGHT); //Gift card panel bounds
		completePanel.setBounds(0, 0, WIDTH, (int)HEIGHT); //Complete panel bounds
		
		resetPanels();

		//Home Panel------------------------------------------//

			newOrderButton.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH), (int)(HEIGHT/2)-(int)(BUTTONHEIGHT/2), BUTTONWIDTH, (int)BUTTONHEIGHT/2);
			pastOrdersButton.setBounds((int)(WIDTH/2), (int)(HEIGHT/2)-(int)(BUTTONHEIGHT/2), BUTTONWIDTH, (int)BUTTONHEIGHT/2);
			homePanel.add(newOrderButton);
			homePanel.add(pastOrdersButton);
			frame.add(homePanel); //Add the panel to the frame

		//New Order Panel------------------------------------------//
			newOrderPanel();	

		//Drinks Panel------------------------------------------//
			int drinksPanelLabelWidth = orderFontMetrics.stringWidth("Drinks"); //Label width
			int drinksPanelLabelHeight = orderFontMetrics.getHeight(); //Label height
			JLabel drinksPanelLabel = new JLabel("Drinks"); //Label
			drinksPanelLabel.setFont(orderFont);
			drinksPanelLabel.setBounds((int)(WIDTH/2)-(int)(drinksPanelLabelWidth*.5), (int)(HEIGHT/2)-(int)(BUTTONHEIGHT*1.5), drinksPanelLabelWidth, drinksPanelLabelHeight);
			drinksPanel.add(drinksPanelLabel);

			cappuccinoButton.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH*2.5), (int)(HEIGHT/2)-(int)(BUTTONHEIGHT/2), BUTTONWIDTH, BUTTONHEIGHT);
			latteButton.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH*1.5), (int)(HEIGHT/2)-(int)(BUTTONHEIGHT/2), BUTTONWIDTH, BUTTONHEIGHT);
			mochaButton.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH*.5), (int)(HEIGHT/2)-(int)(BUTTONHEIGHT/2), BUTTONWIDTH, BUTTONHEIGHT);
			espressoButton.setBounds((int)(WIDTH/2)+(int)(BUTTONWIDTH*.5), (int)(HEIGHT/2)-(int)(BUTTONHEIGHT/2), BUTTONWIDTH, BUTTONHEIGHT);
			teaButton.setBounds((int)(WIDTH/2)+(int)(BUTTONWIDTH*1.5), (int)(HEIGHT/2)-(int)(BUTTONHEIGHT/2), BUTTONWIDTH, BUTTONHEIGHT);
			backButtonDrinks.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH*.5), (int)(HEIGHT/2)+(int)(BUTTONHEIGHT/2), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			drinksPanel.add(cappuccinoButton);
			drinksPanel.add(latteButton);
			drinksPanel.add(mochaButton);
			drinksPanel.add(espressoButton);
			drinksPanel.add(teaButton);
			drinksPanel.add(backButtonDrinks);
			frame.add(drinksPanel); //Add the panel to the frame

		//Food Panel
			int foodPanelLabelWidth = orderFontMetrics.stringWidth("Food"); //Label width
			int foodPanelLabelHeight = orderFontMetrics.getHeight(); //Label height
			JLabel foodPanelLabel = new JLabel("Food"); //Label
			foodPanelLabel.setFont(orderFont);
			foodPanelLabel.setBounds((int)(WIDTH/2)-(int)(foodPanelLabelWidth*.5), (int)(HEIGHT/2)-(int)(BUTTONHEIGHT*1.5), foodPanelLabelWidth, foodPanelLabelHeight);
			foodPanel.add(foodPanelLabel);

			cremaRollButton.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH*2.5), (int)(HEIGHT/2)-(int)(BUTTONHEIGHT/2), BUTTONWIDTH, BUTTONHEIGHT);
			cookieButton.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH*1.5), (int)(HEIGHT/2)-(int)(BUTTONHEIGHT/2), BUTTONWIDTH, BUTTONHEIGHT);
			cheesecakeButton.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH*.5), (int)(HEIGHT/2)-(int)(BUTTONHEIGHT/2), BUTTONWIDTH, BUTTONHEIGHT);
			affogatoButton.setBounds((int)(WIDTH/2)+(int)(BUTTONWIDTH*.5), (int)(HEIGHT/2)-(int)(BUTTONHEIGHT/2), BUTTONWIDTH, BUTTONHEIGHT);
			gelatoButton.setBounds((int)(WIDTH/2)+(int)(BUTTONWIDTH*1.5), (int)(HEIGHT/2)-(int)(BUTTONHEIGHT/2), BUTTONWIDTH, BUTTONHEIGHT);
			backButtonFood.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH*.5), (int)(HEIGHT/2)+(int)(BUTTONHEIGHT/2), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			foodPanel.add(cremaRollButton);
			foodPanel.add(cookieButton);
			foodPanel.add(cheesecakeButton);
			foodPanel.add(affogatoButton);
			foodPanel.add(gelatoButton);
			foodPanel.add(backButtonFood);
			frame.add(foodPanel); //Add the panel to the frame
		
		//Food Panel Modify
			int foodPanelModifyLabelWidth = orderFontMetrics.stringWidth("Food"); //Label width
			int foodPanelModifyLabelHeight = orderFontMetrics.getHeight(); //Label height
			JLabel foodPanelModifyLabel = new JLabel("Food"); //Label
			foodPanelModifyLabel.setFont(orderFont);
			foodPanelModifyLabel.setBounds((int)(WIDTH/2)-(int)(foodPanelModifyLabelWidth*.5), (int)(HEIGHT/2)-(int)(BUTTONHEIGHT*1.5), foodPanelModifyLabelWidth, foodPanelModifyLabelHeight);
			foodPanelModify.add(foodPanelModifyLabel);

			cremaRollButtonModify.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH*2.5), (int)(HEIGHT/2)-(int)(BUTTONHEIGHT/2), BUTTONWIDTH, BUTTONHEIGHT);
			cookieButtonModify.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH*1.5), (int)(HEIGHT/2)-(int)(BUTTONHEIGHT/2), BUTTONWIDTH, BUTTONHEIGHT);
			cheesecakeButtonModify.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH*.5), (int)(HEIGHT/2)-(int)(BUTTONHEIGHT/2), BUTTONWIDTH, BUTTONHEIGHT);
			affogatoButtonModify.setBounds((int)(WIDTH/2)+(int)(BUTTONWIDTH*.5), (int)(HEIGHT/2)-(int)(BUTTONHEIGHT/2), BUTTONWIDTH, BUTTONHEIGHT);
			gelatoButtonModify.setBounds((int)(WIDTH/2)+(int)(BUTTONWIDTH*1.5), (int)(HEIGHT/2)-(int)(BUTTONHEIGHT/2), BUTTONWIDTH, BUTTONHEIGHT);
			removeButtonFood.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH*.5), (int)(HEIGHT/2)+(int)(BUTTONHEIGHT/2), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			foodPanelModify.add(cremaRollButtonModify);
			foodPanelModify.add(cookieButtonModify);
			foodPanelModify.add(cheesecakeButtonModify);
			foodPanelModify.add(affogatoButtonModify);
			foodPanelModify.add(gelatoButtonModify);
			foodPanelModify.add(removeButtonFood);
			frame.add(foodPanelModify); //Add the panel to the frame

		//Cappuccino Panel
			int cappuccinoLabelWidth = orderFontMetrics.stringWidth("Cappuccino"); //Label width
			int cappuccinoLabelHeight = orderFontMetrics.getHeight(); //Label height
			JLabel cappuccinoLabel = new JLabel("Cappuccino"); //Label
			cappuccinoLabel.setFont(orderFont);
			cappuccinoLabel.setBounds((int)(WIDTH/2)-(int)(cappuccinoLabelWidth*.5), (int)(HEIGHT/2)-(int)(BUTTONHEIGHT*1.5), cappuccinoLabelWidth, cappuccinoLabelHeight);
			cappuccinoPanel.add(cappuccinoLabel);

			JLabel cappuccinoIcon = new JLabel(new ImageIcon(cappuccinoImage));
			cappuccinoIcon.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH*.5)+12, (int)(HEIGHT/2)-(int)(BUTTONHEIGHT), BUTTONWIDTH, BUTTONHEIGHT);
			cappuccinoPanel.add(cappuccinoIcon);

			hotButtonCappuccino.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH*.5), (int)(HEIGHT/2), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			smallButtonCappuccino.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH*1.5), (int)(HEIGHT/2)+(int)(BUTTONHEIGHT*.5), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			mediumButtonCappuccino.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH*.5), (int)(HEIGHT/2)+(int)(BUTTONHEIGHT*.5), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			largeButtonCappuccino.setBounds((int)(WIDTH/2)+(int)(BUTTONWIDTH*.5), (int)(HEIGHT/2)+(int)(BUTTONHEIGHT*.5), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			backButtonCappuccino.setBounds((int)(WIDTH/2)-BUTTONWIDTH, (int)(HEIGHT/2)+(int)(BUTTONHEIGHT), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			addButtonCappuccino.setBounds((int)(WIDTH/2), (int)(HEIGHT/2)+(int)(BUTTONHEIGHT), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			cappuccinoPanel.add(hotButtonCappuccino);
			cappuccinoPanel.add(smallButtonCappuccino);
			cappuccinoPanel.add(mediumButtonCappuccino);
			cappuccinoPanel.add(largeButtonCappuccino);
			cappuccinoPanel.add(backButtonCappuccino);
			cappuccinoPanel.add(addButtonCappuccino);
			frame.add(cappuccinoPanel);
		
		//Cappuccino Panel Modify
			int cappuccinoLabelModifyWidth = orderFontMetrics.stringWidth("Cappuccino"); //Label width
			int cappuccinoLabelModifyHeight = orderFontMetrics.getHeight(); //Label height
			JLabel cappuccinoModifyLabel = new JLabel("Cappuccino"); //Label
			cappuccinoModifyLabel.setFont(orderFont);
			cappuccinoModifyLabel.setBounds((int)(WIDTH/2)-(int)(cappuccinoLabelModifyWidth*.5), (int)(HEIGHT/2)-(int)(BUTTONHEIGHT*1.5), cappuccinoLabelModifyWidth, cappuccinoLabelModifyHeight);
			cappuccinoPanelModify.add(cappuccinoModifyLabel);

			JLabel cappuccinoModifyIcon = new JLabel(new ImageIcon(cappuccinoImage));
			cappuccinoModifyIcon.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH*.5)+12, (int)(HEIGHT/2)-(int)(BUTTONHEIGHT), BUTTONWIDTH, BUTTONHEIGHT);
			cappuccinoPanelModify.add(cappuccinoModifyIcon);

			hotButtonCappuccinoModify.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH*.5), (int)(HEIGHT/2), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			smallButtonCappuccinoModify.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH*1.5), (int)(HEIGHT/2)+(int)(BUTTONHEIGHT*.5), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			mediumButtonCappuccinoModify.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH*.5), (int)(HEIGHT/2)+(int)(BUTTONHEIGHT*.5), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			largeButtonCappuccinoModify.setBounds((int)(WIDTH/2)+(int)(BUTTONWIDTH*.5), (int)(HEIGHT/2)+(int)(BUTTONHEIGHT*.5), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			removeButtonCappuccino.setBounds((int)(WIDTH/2)-BUTTONWIDTH, (int)(HEIGHT/2)+(int)(BUTTONHEIGHT), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			updateButtonCappuccinoModify.setBounds((int)(WIDTH/2), (int)(HEIGHT/2)+(int)(BUTTONHEIGHT), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			cappuccinoPanelModify.add(hotButtonCappuccinoModify);
			cappuccinoPanelModify.add(smallButtonCappuccinoModify);
			cappuccinoPanelModify.add(mediumButtonCappuccinoModify);
			cappuccinoPanelModify.add(largeButtonCappuccinoModify);
			cappuccinoPanelModify.add(removeButtonCappuccino);
			cappuccinoPanelModify.add(updateButtonCappuccinoModify);
			frame.add(cappuccinoPanelModify);

		//Latte Panel
			int latteLabelWidth = orderFontMetrics.stringWidth("Latte"); //Label width
			int latteLabelHeight = orderFontMetrics.getHeight(); //Label height
			JLabel latteLabel = new JLabel("Latte"); //Label
			latteLabel.setFont(orderFont);
			latteLabel.setBounds((int)(WIDTH/2)-(int)(latteLabelWidth*.5), (int)(HEIGHT/2)-(int)(BUTTONHEIGHT*1.5), latteLabelWidth, latteLabelHeight);
			lattePanel.add(latteLabel);

			JLabel latteIcon = new JLabel(new ImageIcon(latteImage));
			latteIcon.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH*.5)+12, (int)(HEIGHT/2)-(int)(BUTTONHEIGHT), BUTTONWIDTH, BUTTONHEIGHT);
			lattePanel.add(latteIcon);

			hotButtonLatte.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH*1.5), (int)(HEIGHT/2), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			icedButtonLatte.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH*.5), (int)(HEIGHT/2), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			blendedButtonLatte.setBounds((int)(WIDTH/2)+(int)(BUTTONWIDTH*.5), (int)(HEIGHT/2), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			smallButtonLatte.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH*1.5), (int)(HEIGHT/2)+(int)(BUTTONHEIGHT*.5), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			mediumButtonLatte.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH*.5), (int)(HEIGHT/2)+(int)(BUTTONHEIGHT*.5), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			largeButtonLatte.setBounds((int)(WIDTH/2)+(int)(BUTTONWIDTH*.5), (int)(HEIGHT/2)+(int)(BUTTONHEIGHT*.5), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			backButtonLatte.setBounds((int)(WIDTH/2)-BUTTONWIDTH, (int)(HEIGHT/2)+(int)(BUTTONHEIGHT), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			addButtonLatte.setBounds((int)(WIDTH/2), (int)(HEIGHT/2)+(int)(BUTTONHEIGHT), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			lattePanel.add(hotButtonLatte);
			lattePanel.add(icedButtonLatte);
			lattePanel.add(blendedButtonLatte);
			lattePanel.add(smallButtonLatte);
			lattePanel.add(mediumButtonLatte);
			lattePanel.add(largeButtonLatte);
			lattePanel.add(backButtonLatte);
			lattePanel.add(addButtonLatte);
			frame.add(lattePanel);

		//Latte Panel Modify
			int latteModifyLabelWidth = orderFontMetrics.stringWidth("Latte"); //Label width
			int latteModifyLabelHeight = orderFontMetrics.getHeight(); //Label height
			JLabel latteModifyLabel = new JLabel("Latte"); //Label
			latteModifyLabel.setFont(orderFont);
			latteModifyLabel.setBounds((int)(WIDTH/2)-(int)(latteModifyLabelWidth*.5), (int)(HEIGHT/2)-(int)(BUTTONHEIGHT*1.5), latteModifyLabelWidth, latteModifyLabelHeight);
			lattePanelModify.add(latteModifyLabel);

			JLabel latteModifyIcon = new JLabel(new ImageIcon(latteImage));
			latteModifyIcon.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH*.5)+12, (int)(HEIGHT/2)-(int)(BUTTONHEIGHT), BUTTONWIDTH, BUTTONHEIGHT);
			lattePanelModify.add(latteModifyIcon);

			hotButtonLatteModify.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH*1.5), (int)(HEIGHT/2), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			icedButtonLatteModify.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH*.5), (int)(HEIGHT/2), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			blendedButtonLatteModify.setBounds((int)(WIDTH/2)+(int)(BUTTONWIDTH*.5), (int)(HEIGHT/2), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			smallButtonLatteModify.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH*1.5), (int)(HEIGHT/2)+(int)(BUTTONHEIGHT*.5), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			mediumButtonLatteModify.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH*.5), (int)(HEIGHT/2)+(int)(BUTTONHEIGHT*.5), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			largeButtonLatteModify.setBounds((int)(WIDTH/2)+(int)(BUTTONWIDTH*.5), (int)(HEIGHT/2)+(int)(BUTTONHEIGHT*.5), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			removeButtonLatte.setBounds((int)(WIDTH/2)-BUTTONWIDTH, (int)(HEIGHT/2)+(int)(BUTTONHEIGHT), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			updateButtonLatteModify.setBounds((int)(WIDTH/2), (int)(HEIGHT/2)+(int)(BUTTONHEIGHT), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			lattePanelModify.add(hotButtonLatteModify);
			lattePanelModify.add(icedButtonLatteModify);
			lattePanelModify.add(blendedButtonLatteModify);
			lattePanelModify.add(smallButtonLatteModify);
			lattePanelModify.add(mediumButtonLatteModify);
			lattePanelModify.add(largeButtonLatteModify);
			lattePanelModify.add(removeButtonLatte);
			lattePanelModify.add(updateButtonLatteModify);
			frame.add(lattePanelModify);
		
		//Mocha Panel
			int mochaLabelWidth = orderFontMetrics.stringWidth("Mocha"); //Label width
			int mochaLabelHeight = orderFontMetrics.getHeight(); //Label height
			JLabel mochaLabel = new JLabel("Mocha"); //Label
			mochaLabel.setFont(orderFont);
			mochaLabel.setBounds((int)(WIDTH/2)-(int)(mochaLabelWidth*.5), (int)(HEIGHT/2)-(int)(BUTTONHEIGHT*1.5), mochaLabelWidth, mochaLabelHeight);
			mochaPanel.add(mochaLabel);

			JLabel mochaIcon = new JLabel(new ImageIcon(mochaImage));
			mochaIcon.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH*.5)+12, (int)(HEIGHT/2)-(int)(BUTTONHEIGHT), BUTTONWIDTH, BUTTONHEIGHT);
			mochaPanel.add(mochaIcon);

			hotButtonMocha.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH*1.5), (int)(HEIGHT/2), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			icedButtonMocha.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH*.5), (int)(HEIGHT/2), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			blendedButtonMocha.setBounds((int)(WIDTH/2)+(int)(BUTTONWIDTH*.5), (int)(HEIGHT/2), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			smallButtonMocha.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH*1.5), (int)(HEIGHT/2)+(int)(BUTTONHEIGHT*.5), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			mediumButtonMocha.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH*.5), (int)(HEIGHT/2)+(int)(BUTTONHEIGHT*.5), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			largeButtonMocha.setBounds((int)(WIDTH/2)+(int)(BUTTONWIDTH*.5), (int)(HEIGHT/2)+(int)(BUTTONHEIGHT*.5), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			backButtonMocha.setBounds((int)(WIDTH/2)-BUTTONWIDTH, (int)(HEIGHT/2)+(int)(BUTTONHEIGHT), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			addButtonMocha.setBounds((int)(WIDTH/2), (int)(HEIGHT/2)+(int)(BUTTONHEIGHT), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			mochaPanel.add(hotButtonMocha);
			mochaPanel.add(icedButtonMocha);
			mochaPanel.add(blendedButtonMocha);
			mochaPanel.add(smallButtonMocha);
			mochaPanel.add(mediumButtonMocha);
			mochaPanel.add(largeButtonMocha);
			mochaPanel.add(backButtonMocha);
			mochaPanel.add(addButtonMocha);
			frame.add(mochaPanel);
		
		//Mocha Panel
			int mochaModifyLabelWidth = orderFontMetrics.stringWidth("Mocha"); //Label width
			int mochaModifyLabelHeight = orderFontMetrics.getHeight(); //Label height
			JLabel mochaModifyLabel = new JLabel("Mocha"); //Label
			mochaModifyLabel.setFont(orderFont);
			mochaModifyLabel.setBounds((int)(WIDTH/2)-(int)(mochaModifyLabelWidth*.5), (int)(HEIGHT/2)-(int)(BUTTONHEIGHT*1.5), mochaModifyLabelWidth, mochaModifyLabelHeight);
			mochaPanelModify.add(mochaModifyLabel);

			JLabel mochaModifyIcon = new JLabel(new ImageIcon(mochaImage));
			mochaModifyIcon.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH*.5)+12, (int)(HEIGHT/2)-(int)(BUTTONHEIGHT), BUTTONWIDTH, BUTTONHEIGHT);
			mochaPanelModify.add(mochaModifyIcon);

			hotButtonMochaModify.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH*1.5), (int)(HEIGHT/2), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			icedButtonMochaModify.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH*.5), (int)(HEIGHT/2), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			blendedButtonMochaModify.setBounds((int)(WIDTH/2)+(int)(BUTTONWIDTH*.5), (int)(HEIGHT/2), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			smallButtonMochaModify.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH*1.5), (int)(HEIGHT/2)+(int)(BUTTONHEIGHT*.5), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			mediumButtonMochaModify.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH*.5), (int)(HEIGHT/2)+(int)(BUTTONHEIGHT*.5), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			largeButtonMochaModify.setBounds((int)(WIDTH/2)+(int)(BUTTONWIDTH*.5), (int)(HEIGHT/2)+(int)(BUTTONHEIGHT*.5), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			removeButtonMocha.setBounds((int)(WIDTH/2)-BUTTONWIDTH, (int)(HEIGHT/2)+(int)(BUTTONHEIGHT), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			updateButtonMochaModify.setBounds((int)(WIDTH/2), (int)(HEIGHT/2)+(int)(BUTTONHEIGHT), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			mochaPanelModify.add(hotButtonMochaModify);
			mochaPanelModify.add(icedButtonMochaModify);
			mochaPanelModify.add(blendedButtonMochaModify);
			mochaPanelModify.add(smallButtonMochaModify);
			mochaPanelModify.add(mediumButtonMochaModify);
			mochaPanelModify.add(largeButtonMochaModify);
			mochaPanelModify.add(removeButtonMocha);
			mochaPanelModify.add(updateButtonMochaModify);
			frame.add(mochaPanelModify);

		//Espresso Panel
			int espressoLabelWidth = orderFontMetrics.stringWidth("Espresso"); //Label width
			int espressoLabelHeight = orderFontMetrics.getHeight(); //Label height
			JLabel espressoLabel = new JLabel("Espresso"); //Label
			espressoLabel.setFont(orderFont);
			espressoLabel.setBounds((int)(WIDTH/2)-(int)(espressoLabelWidth*.5), (int)(HEIGHT/2)-(int)(BUTTONHEIGHT*1.5), espressoLabelWidth, espressoLabelHeight);
			espressoPanel.add(espressoLabel);

			JLabel espressoIcon = new JLabel(new ImageIcon(espressoImage));
			espressoIcon.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH*.5)+12, (int)(HEIGHT/2)-(int)(BUTTONHEIGHT), BUTTONWIDTH, BUTTONHEIGHT);
			espressoPanel.add(espressoIcon);

			hotButtonEspresso.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH*.5), (int)(HEIGHT/2), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			smallButtonEspresso.setBounds((int)(WIDTH/2)-BUTTONWIDTH, (int)(HEIGHT/2)+(int)(BUTTONHEIGHT*.5), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			largeButtonEspresso.setBounds((int)(WIDTH/2), (int)(HEIGHT/2)+(int)(BUTTONHEIGHT*.5), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			backButtonEspresso.setBounds((int)(WIDTH/2)-BUTTONWIDTH, (int)(HEIGHT/2)+(int)(BUTTONHEIGHT), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			addButtonEspresso.setBounds((int)(WIDTH/2), (int)(HEIGHT/2)+(int)(BUTTONHEIGHT), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			espressoPanel.add(hotButtonEspresso);
			espressoPanel.add(smallButtonEspresso);
			espressoPanel.add(largeButtonEspresso);
			espressoPanel.add(backButtonEspresso);
			espressoPanel.add(addButtonEspresso);
			frame.add(espressoPanel);
		
		//Espresso Panel
			int espressoModifyLabelWidth = orderFontMetrics.stringWidth("Espresso"); //Label width
			int espressoModifyLabelHeight = orderFontMetrics.getHeight(); //Label height
			JLabel espressoModifyLabel = new JLabel("Espresso"); //Label
			espressoModifyLabel.setFont(orderFont);
			espressoModifyLabel.setBounds((int)(WIDTH/2)-(int)(espressoModifyLabelWidth*.5), (int)(HEIGHT/2)-(int)(BUTTONHEIGHT*1.5), espressoModifyLabelWidth, espressoModifyLabelHeight);
			espressoPanelModify.add(espressoModifyLabel);

			JLabel espressoModifyIcon = new JLabel(new ImageIcon(espressoImage));
			espressoModifyIcon.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH*.5)+12, (int)(HEIGHT/2)-(int)(BUTTONHEIGHT), BUTTONWIDTH, BUTTONHEIGHT);
			espressoPanelModify.add(espressoModifyIcon);

			hotButtonEspressoModify.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH*.5), (int)(HEIGHT/2), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			smallButtonEspressoModify.setBounds((int)(WIDTH/2)-BUTTONWIDTH, (int)(HEIGHT/2)+(int)(BUTTONHEIGHT*.5), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			largeButtonEspressoModify.setBounds((int)(WIDTH/2), (int)(HEIGHT/2)+(int)(BUTTONHEIGHT*.5), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			removeButtonEspresso.setBounds((int)(WIDTH/2)-BUTTONWIDTH, (int)(HEIGHT/2)+(int)(BUTTONHEIGHT), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			updateButtonEspressoModify.setBounds((int)(WIDTH/2), (int)(HEIGHT/2)+(int)(BUTTONHEIGHT), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			espressoPanelModify.add(hotButtonEspressoModify);
			espressoPanelModify.add(smallButtonEspressoModify);
			espressoPanelModify.add(largeButtonEspressoModify);
			espressoPanelModify.add(removeButtonEspresso);
			espressoPanelModify.add(updateButtonEspressoModify);
			frame.add(espressoPanelModify);
		
		//Tea Panel
			int teaLabelWidth = orderFontMetrics.stringWidth("Tea"); //Label width
			int teaLabelHeight = orderFontMetrics.getHeight(); //Label height
			JLabel teaLabel = new JLabel("Tea"); //Label
			teaLabel.setFont(orderFont);
			teaLabel.setBounds((int)(WIDTH/2)-(int)(teaLabelWidth*.5), (int)(HEIGHT/2)-(int)(BUTTONHEIGHT*1.5), teaLabelWidth, teaLabelHeight);
			teaPanel.add(teaLabel);

			JLabel teaIcon = new JLabel(new ImageIcon(teaImage));
			teaIcon.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH*.5)+12, (int)(HEIGHT/2)-(int)(BUTTONHEIGHT), BUTTONWIDTH, BUTTONHEIGHT);
			teaPanel.add(teaIcon);

			hotButtonTea.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH*1.5), (int)(HEIGHT/2), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			icedButtonTea.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH*.5), (int)(HEIGHT/2), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			blendedButtonTea.setBounds((int)(WIDTH/2)+(int)(BUTTONWIDTH*.5), (int)(HEIGHT/2), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			smallButtonTea.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH*1.5), (int)(HEIGHT/2)+(int)(BUTTONHEIGHT*.5), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			mediumButtonTea.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH*.5), (int)(HEIGHT/2)+(int)(BUTTONHEIGHT*.5), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			largeButtonTea.setBounds((int)(WIDTH/2)+(int)(BUTTONWIDTH*.5), (int)(HEIGHT/2)+(int)(BUTTONHEIGHT*.5), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			backButtonTea.setBounds((int)(WIDTH/2)-BUTTONWIDTH, (int)(HEIGHT/2)+(int)(BUTTONHEIGHT), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			addButtonTea.setBounds((int)(WIDTH/2), (int)(HEIGHT/2)+(int)(BUTTONHEIGHT), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			teaPanel.add(hotButtonTea);
			teaPanel.add(icedButtonTea);
			teaPanel.add(blendedButtonTea);
			teaPanel.add(smallButtonTea);
			teaPanel.add(mediumButtonTea);
			teaPanel.add(largeButtonTea);
			teaPanel.add(backButtonTea);
			teaPanel.add(addButtonTea);
			frame.add(teaPanel);
		
		//Tea Panel
			int teaModifyLabelWidth = orderFontMetrics.stringWidth("Tea"); //Label width
			int teaModifyLabelHeight = orderFontMetrics.getHeight(); //Label height
			JLabel teaModifyLabel = new JLabel("Tea"); //Label
			teaModifyLabel.setFont(orderFont);
			teaModifyLabel.setBounds((int)(WIDTH/2)-(int)(teaModifyLabelWidth*.5), (int)(HEIGHT/2)-(int)(BUTTONHEIGHT*1.5), teaModifyLabelWidth, teaModifyLabelHeight);
			teaPanelModify.add(teaModifyLabel);

			JLabel teaModifyIcon = new JLabel(new ImageIcon(teaImage));
			teaModifyIcon.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH*.5)+12, (int)(HEIGHT/2)-(int)(BUTTONHEIGHT), BUTTONWIDTH, BUTTONHEIGHT);
			teaPanelModify.add(teaModifyIcon);

			hotButtonTeaModify.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH*1.5), (int)(HEIGHT/2), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			icedButtonTeaModify.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH*.5), (int)(HEIGHT/2), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			blendedButtonTeaModify.setBounds((int)(WIDTH/2)+(int)(BUTTONWIDTH*.5), (int)(HEIGHT/2), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			smallButtonTeaModify.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH*1.5), (int)(HEIGHT/2)+(int)(BUTTONHEIGHT*.5), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			mediumButtonTeaModify.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH*.5), (int)(HEIGHT/2)+(int)(BUTTONHEIGHT*.5), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			largeButtonTeaModify.setBounds((int)(WIDTH/2)+(int)(BUTTONWIDTH*.5), (int)(HEIGHT/2)+(int)(BUTTONHEIGHT*.5), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			removeButtonTea.setBounds((int)(WIDTH/2)-BUTTONWIDTH, (int)(HEIGHT/2)+(int)(BUTTONHEIGHT), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			updateButtonTeaModify.setBounds((int)(WIDTH/2), (int)(HEIGHT/2)+(int)(BUTTONHEIGHT), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			teaPanelModify.add(hotButtonTeaModify);
			teaPanelModify.add(icedButtonTeaModify);
			teaPanelModify.add(blendedButtonTeaModify);
			teaPanelModify.add(smallButtonTeaModify);
			teaPanelModify.add(mediumButtonTeaModify);
			teaPanelModify.add(largeButtonTeaModify);
			teaPanelModify.add(removeButtonTea);
			teaPanelModify.add(updateButtonTeaModify);
			frame.add(teaPanelModify);

		//Pay Panel
		payPanel();

		//Past Orders Panel

		frame.setTitle(TITLE); //Add the title to the frame
		frame.setResizable(false); //Window cannot be resized
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Exit the program when the window is closed
		frame.setLocationRelativeTo(null); //Open the window in the middle
		frame.setVisible(true); //Set visible //Make the window visible
	}

	//getImage(JButton, String) method - Accepts a JButton and filename, loads the filename image and assigns it to the button
	public void setButtonImage(JButton button, Image image) {
		try {
			button.setIcon(new ImageIcon(image.getScaledInstance(BUTTONWIDTH-50, BUTTONHEIGHT-50, Image.SCALE_SMOOTH)));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void resetPanels() {
		//Set all panels to not visible
		homePanel.setVisible(false);
		newOrderPanel.setVisible(false);
		drinksPanel.setVisible(false);
		cappuccinoPanel.setVisible(false);
		lattePanel.setVisible(false);
		mochaPanel.setVisible(false);
		espressoPanel.setVisible(false);
		teaPanel.setVisible(false);
		cappuccinoPanelModify.setVisible(false);
		lattePanelModify.setVisible(false);
		mochaPanelModify.setVisible(false);
		espressoPanelModify.setVisible(false);
		teaPanelModify.setVisible(false);
		foodPanel.setVisible(false);
		foodPanelModify.setVisible(false);
		cremaRollPanel.setVisible(false);
		cookiePanel.setVisible(false);
		cheesecakePanel.setVisible(false);
		affogatoPanel.setVisible(false);
		gelatoPanel.setVisible(false);
		payPanel.setVisible(false);
		refundOrderPanel.setVisible(false);
		cashPanel.setVisible(false);
		creditPanel.setVisible(false);
		giftCardPanel.setVisible(false);
		completePanel.setVisible(false);
	}

	//newOrdersPanel() method - Generates the New Orders panel
	public void newOrderPanel() {
		newOrderPanel.removeAll(); //Clear the components
		newOrderPanel.updateUI(); //Update the panel

		//If theres no items
		if(tempOrder.items.size() == 0) {
			//Set up the title and place it
			int newOrderPanelLabelWidth = orderFontMetrics.stringWidth("Order #" + tempOrder.orderNumber);
			int newOrderPanelLabelHeight = orderFontMetrics.getHeight();
			JLabel newOrderPanelLabel = new JLabel("Order #" + tempOrder.orderNumber);
			newOrderPanelLabel.setFont(orderFont);
			newOrderPanelLabel.setBounds((int)(WIDTH/2)-(int)(newOrderPanelLabelWidth*.5), (int)(HEIGHT/2)-(int)(BUTTONHEIGHT*1.5), newOrderPanelLabelWidth, newOrderPanelLabelHeight);
			newOrderPanel.add(newOrderPanelLabel);

			//Place buttons
			drinksButton.setBounds((int)(WIDTH/2)-BUTTONWIDTH, (int)(HEIGHT/2)-BUTTONHEIGHT, BUTTONWIDTH, (int)(BUTTONHEIGHT));
			foodButton.setBounds((int)(WIDTH/2), (int)(HEIGHT/2)-BUTTONHEIGHT, BUTTONWIDTH, (int)(BUTTONHEIGHT));
			payButton.setBounds((int)(WIDTH/2)-BUTTONWIDTH, (int)(HEIGHT/2), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			cancelOrderButton.setBounds((int)(WIDTH/2), (int)(HEIGHT/2), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));

			payButton.setEnabled(false); //Because there is nothing to pay for, disable the button

			newOrderPanel.add(drinksButton);
			newOrderPanel.add(foodButton);
			newOrderPanel.add(payButton);
			newOrderPanel.add(cancelOrderButton);
		}

		if(tempOrder.items.size() > 0) {
			//Set up the title and place it
			int newOrderPanelLabelWidth = orderFontMetrics.stringWidth("Order #" + totalOrders);
			int newOrderPanelLabelHeight = orderFontMetrics.getHeight();
			JLabel newOrderPanelLabel = new JLabel("Order #" + totalOrders);
			newOrderPanelLabel.setFont(orderFont);
			newOrderPanelLabel.setBounds((int)(WIDTH/2)-(int)(newOrderPanelLabelWidth*.5), (int)(HEIGHT/2)-(int)(BUTTONHEIGHT*1.5), newOrderPanelLabelWidth, newOrderPanelLabelHeight);
			newOrderPanel.add(newOrderPanelLabel);

			//Place buttons
			int totalPriceWidth = orderFontMetrics.stringWidth("Total: $" + df.format(tempOrder.total));
			int totalPriceHeight = orderFontMetrics.getHeight();
			JLabel totalPriceLabel = new JLabel("Total: $" + df.format(tempOrder.total));
			totalPriceLabel.setFont(orderFont);
			totalPriceLabel.setBounds((int)(WIDTH/2)-(int)(totalPriceWidth*.5), (int)(HEIGHT/2)+110, totalPriceWidth, totalPriceHeight);
			newOrderPanel.add(totalPriceLabel);

			drinksButton.setBounds((int)(WIDTH/2)-BUTTONWIDTH, (int)(HEIGHT/2)-BUTTONHEIGHT, BUTTONWIDTH, (int)(BUTTONHEIGHT));
			foodButton.setBounds((int)(WIDTH/2), (int)(HEIGHT/2)-BUTTONHEIGHT, BUTTONWIDTH, (int)(BUTTONHEIGHT));
			payButton.setBounds((int)(WIDTH/2)-BUTTONWIDTH, (int)(HEIGHT/2)+150, BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			cancelOrderButton.setBounds((int)(WIDTH/2), (int)(HEIGHT/2)+150, BUTTONWIDTH, (int)(BUTTONHEIGHT/2));

			payButton.setEnabled(true); //There is something to pay for, enable the button

			newOrderPanel.add(drinksButton);
			newOrderPanel.add(foodButton);
			newOrderPanel.add(payButton);
			newOrderPanel.add(cancelOrderButton);

			int tempOrderSize = tempOrder.items.size(); //Assign the size of the order
			int track = tempOrderSize/2; //Middle point used for placing buttons aligned center below
			
			//If there's 1 item
			if(tempOrderSize == 1) {
				tempOrder.items.get(0).button.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH/2), (int)(HEIGHT/2), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
				newOrderPanel.add(tempOrder.items.get(0).button);
			}

			//If there are an even number of items in the order
			if(tempOrderSize % 2 == 0 && tempOrderSize > 1) {
				track = tempOrderSize/2;
				//For first half of items (left)
				for(int i = (tempOrderSize/2)-1; i >= 0; i--) {
					tempOrder.items.get(i).button.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH*(i+1)), (int)(HEIGHT/2), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
					newOrderPanel.add(tempOrder.items.get(i).button);
				}
				//For second half of items (right)
				for(int i = 0; i < tempOrderSize/2; i++) {
					tempOrder.items.get(track).button.setBounds((int)(WIDTH/2)+(int)(BUTTONWIDTH*(i)), (int)(HEIGHT/2), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
					newOrderPanel.add(tempOrder.items.get(track).button);
					track += 1;
				}
			}
			//If there are an odd number of items in the order
			if(tempOrderSize % 2 != 0 && tempOrderSize > 1) {
				track = tempOrderSize/2;
				//For first half of items (left), not including middle item
				for(int i = (tempOrderSize/2)-1; i >= 0; i--) {
					tempOrder.items.get(i).button.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH*(i+1.5)), (int)(HEIGHT/2), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
					newOrderPanel.add(tempOrder.items.get(i).button);
				}
				tempOrder.items.get(track).button.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH/2), (int)(HEIGHT/2), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
				newOrderPanel.add(tempOrder.items.get(track).button);
				track += 1;
				//For second half of items (right), not including middle item
				for(int i = 0; i < (tempOrderSize/2); i++) {
					tempOrder.items.get(track).button.setBounds((int)(WIDTH/2)+(int)(BUTTONWIDTH*(i+.5)), (int)(HEIGHT/2), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
					newOrderPanel.add(tempOrder.items.get(track).button);
					track += 1;
				}
			}
		}
		//Remove item buttons if the user removes them
		for(int i = 0; i < tempOrder.items.size(); i++) {
			int j = i; //Used for adding action listener as like-final is needed
			//Apply action listener to all buttons of items in the order
			tempOrder.items.get(j).button.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					if(tempOrder.items.size() == 1 && !removed) {
						removed = true; //Item is removed
						String t = tempOrder.items.get(0).type; //Get the type of the button
						String styleCheck = tempOrder.items.get(0).style; //Get the style of the button
						String sizeCheck = tempOrder.items.get(0).size; //Get the size of the button
						tempOrder.items.remove(0); //Remove the item
						itemNumber--; //Decrement itemNumber because the item was removed
						if(t == "Cappuccino") { //If type is Cappuccino
							screen = "Cappuccino Modify"; //Switch to capp modify screen
							style = "Hot"; //Set tyle
							if(styleCheck == "Hot") {
								highlightStyleButton(hotButtonCappuccinoModify); //Highlight button
							}
							if(sizeCheck == "S") {
								highlightSizeButton(smallButtonCappuccinoModify); //Highlight button
							}
							if(sizeCheck == "M") {
								highlightSizeButton(mediumButtonCappuccinoModify); //Highlight button
							}
							if(sizeCheck == "L") {
								highlightSizeButton(largeButtonCappuccinoModify); //Highlight button
							}
							render();
						}
						if(t == "Latte") { //Same comments as above
							screen = "Latte Modify";
							if(styleCheck == "Hot") {
								highlightStyleButton(hotButtonLatteModify);
							}
							if(styleCheck == "Iced") {
								highlightStyleButton(icedButtonLatteModify);
							}
							if(styleCheck == "Blended") {
								highlightStyleButton(blendedButtonLatteModify);
							}
							if(sizeCheck == "S") {
								highlightSizeButton(smallButtonLatteModify);
							}
							if(sizeCheck == "M") {
								highlightSizeButton(mediumButtonLatteModify);
							}
							if(sizeCheck == "L") {
								highlightSizeButton(largeButtonLatteModify);
							}
							render();
						}
						if(t == "Mocha") { //Same comments as above
							screen = "Mocha Modify";
							if(styleCheck == "Hot") {
								highlightStyleButton(hotButtonMochaModify);
							}
							if(styleCheck == "Iced") {
								highlightStyleButton(icedButtonMochaModify);
							}
							if(styleCheck == "Blended") {
								highlightStyleButton(blendedButtonMochaModify);
							}
							if(sizeCheck == "S") {
								highlightSizeButton(smallButtonMochaModify);
							}
							if(sizeCheck == "M") {
								highlightSizeButton(mediumButtonMochaModify);
							}
							if(sizeCheck == "L") {
								highlightSizeButton(largeButtonMochaModify);
							}
							render();
						}
						if(t == "Espresso") { //Same comments as above
							screen = "Espresso Modify";
							style = "Hot";
							if(styleCheck == "Hot") {
								highlightStyleButton(hotButtonEspressoModify);
							}
							if(sizeCheck == "S") {
								highlightSizeButton(smallButtonEspressoModify);
							}
							if(sizeCheck == "L") {
								highlightSizeButton(largeButtonEspressoModify);
							}
							render();
						}
						if(t == "Tea") { //Same comments as above
							screen = "Tea Modify";
							if(styleCheck == "Hot") {
								highlightStyleButton(hotButtonTeaModify);
							}
							if(styleCheck == "Iced") {
								highlightStyleButton(icedButtonTeaModify);
							}
							if(styleCheck == "Blended") {
								highlightStyleButton(blendedButtonTeaModify);
							}
							if(sizeCheck == "S") {
								highlightSizeButton(smallButtonTeaModify);
							}
							if(sizeCheck == "M") {
								highlightSizeButton(mediumButtonTeaModify);
							}
							if(sizeCheck == "L") {
								highlightSizeButton(largeButtonTeaModify);
							}
							render();
						}
						if(t == "Crema Roll") { //Same comments as above
							screen = "Food Modify";
							render();
						}
						if(t == "Cookie") { //Same comments as above
							screen = "Food Modify";
							render();
						}
						if(t == "Cheesecake") { //Same comments as above
							screen = "Food Modify";
							render();
						}
						if(t == "Affogato") { //Same comments as above
							screen = "Food Modify";
							render();
						}
						if(t == "Gelato") { //Same comments as above
							screen = "Food Modify";
							render();
						}
					}
					if(tempOrder.items.size() > 1 && !removed) {
						//Same logic as above
						String t = tempOrder.items.get(j).type;
						String styleCheck = tempOrder.items.get(j).style;
						String sizeCheck = tempOrder.items.get(j).size;
						if(t == "Cappuccino" && !removed) {
							screen = "Cappuccino Modify";
							style = "Hot";
							if(styleCheck == "Hot") {
								highlightStyleButton(hotButtonCappuccinoModify);
							}
							if(sizeCheck == "S") {
								highlightSizeButton(smallButtonCappuccinoModify);
							}
							if(sizeCheck == "M") {
								highlightSizeButton(mediumButtonCappuccinoModify);
							}
							if(sizeCheck == "L") {
								highlightSizeButton(largeButtonCappuccinoModify);
							}
							for(int k = 0; k <tempOrder.items.size(); k++) {
								if(tempOrder.items.get(k).itemNumber == tempOrder.items.get(j).itemNumber) {
									tempOrder.items.remove(k);
									itemNumber--;
									removed = true;
									break;
								}
							}
							render();
						}
						if(t == "Latte" && !removed) {
							screen = "Latte Modify";
							if(styleCheck == "Hot") {
								highlightStyleButton(hotButtonLatteModify);
							}
							if(styleCheck == "Iced") {
								highlightStyleButton(icedButtonLatteModify);
							}
							if(styleCheck == "Blended") {
								highlightStyleButton(blendedButtonLatteModify);
							}
							if(sizeCheck == "S") {
								highlightSizeButton(smallButtonLatteModify);
							}
							if(sizeCheck == "M") {
								highlightSizeButton(mediumButtonLatteModify);
							}
							if(sizeCheck == "L") {
								highlightSizeButton(largeButtonLatteModify);
							}
							for(int k = 0; k <tempOrder.items.size(); k++) {
								if(tempOrder.items.get(k).itemNumber == tempOrder.items.get(j).itemNumber) {
									tempOrder.items.remove(k);
									itemNumber--;
									removed = true;
									break;
								}
							}
							render();
						}
						if(t == "Mocha" && !removed) {
							screen = "Mocha Modify";
							if(styleCheck == "Hot") {
								highlightStyleButton(hotButtonMochaModify);
							}
							if(styleCheck == "Iced") {
								highlightStyleButton(icedButtonMochaModify);
							}
							if(styleCheck == "Blended") {
								highlightStyleButton(blendedButtonMochaModify);
							}
							if(sizeCheck == "S") {
								highlightSizeButton(smallButtonMochaModify);
							}
							if(sizeCheck == "M") {
								highlightSizeButton(mediumButtonMochaModify);
							}
							if(sizeCheck == "L") {
								highlightSizeButton(largeButtonMochaModify);
							}
							for(int k = 0; k <tempOrder.items.size(); k++) {
								if(tempOrder.items.get(k).itemNumber == tempOrder.items.get(j).itemNumber) {
									tempOrder.items.remove(k);
									itemNumber--;
									removed = true;
									break;
								}
							}
							render();
						}
						if(t == "Espresso" && !removed) {
							screen = "Espresso Modify";
							style = "Hot";
							if(styleCheck == "Hot") {
								highlightStyleButton(hotButtonEspressoModify);
							}
							if(sizeCheck == "S") {
								highlightSizeButton(smallButtonEspressoModify);
							}
							if(sizeCheck == "L") {
								highlightSizeButton(largeButtonEspressoModify);
							}
							for(int k = 0; k <tempOrder.items.size(); k++) {
								if(tempOrder.items.get(k).itemNumber == tempOrder.items.get(j).itemNumber) {
									tempOrder.items.remove(k);
									itemNumber--;
									removed = true;
									break;
								}
							}
							render();
						}
						if(t == "Tea" && !removed) {
							screen = "Tea Modify";
							if(styleCheck == "Hot") {
								highlightStyleButton(hotButtonTeaModify);
							}
							if(styleCheck == "Iced") {
								highlightStyleButton(icedButtonTeaModify);
							}
							if(styleCheck == "Blended") {
								highlightStyleButton(blendedButtonTeaModify);
							}
							if(sizeCheck == "S") {
								highlightSizeButton(smallButtonTeaModify);
							}
							if(sizeCheck == "M") {
								highlightSizeButton(mediumButtonTeaModify);
							}
							if(sizeCheck == "L") {
								highlightSizeButton(largeButtonTeaModify);
							}
							for(int k = 0; k <tempOrder.items.size(); k++) {
								if(tempOrder.items.get(k).itemNumber == tempOrder.items.get(j).itemNumber) {
									tempOrder.items.remove(k);
									itemNumber--;
									removed = true;
									break;
								}
							}
							render();
						}
						if(t == "Crema Roll" && !removed) {
							screen = "Food Modify";
							for(int k = 0; k <tempOrder.items.size(); k++) {
								if(tempOrder.items.get(k).itemNumber == tempOrder.items.get(j).itemNumber) {
									tempOrder.items.remove(k);
									itemNumber--;
									removed = true;
									break;
								}
							}
							render();
						}
						if(t == "Cookie" && !removed) {
							screen = "Food Modify";
							for(int k = 0; k <tempOrder.items.size(); k++) {
								if(tempOrder.items.get(k).itemNumber == tempOrder.items.get(j).itemNumber) {
									tempOrder.items.remove(k);
									itemNumber--;
									removed = true;
									break;
								}
							}
							render();
						}
						if(t == "Cheesecake" && !removed) {
							screen = "Food Modify";
							for(int k = 0; k <tempOrder.items.size(); k++) {
								if(tempOrder.items.get(k).itemNumber == tempOrder.items.get(j).itemNumber) {
									tempOrder.items.remove(k);
									itemNumber--;
									removed = true;
									break;
								}
							}
							render();
						}
						if(t == "Affogato" && !removed) {
							screen = "Food Modify";
							for(int k = 0; k <tempOrder.items.size(); k++) {
								if(tempOrder.items.get(k).itemNumber == tempOrder.items.get(j).itemNumber) {
									tempOrder.items.remove(k);
									itemNumber--;
									removed = true;
									break;
								}
							}
							render();
						}
						if(t == "Gelato" && !removed) {
							screen = "Food Modify";
							for(int k = 0; k <tempOrder.items.size(); k++) {
								if(tempOrder.items.get(k).itemNumber == tempOrder.items.get(j).itemNumber) {
									tempOrder.items.remove(k);
									itemNumber--;
									removed = true;
									break;
								}
							}
							render();
						}
					}
				}
			});
		}
		frame.add(newOrderPanel); //Add the panel to the frame
	}

	//payPanel() method - The same as newOrderPanel() method however for keeping the payment panel updated, follows the same logic
	public void payPanel() {
		payPanel.removeAll();
		payPanel.updateUI();

		int payPanelLabelWidth = orderFontMetrics.stringWidth("Pay");
		int payPanelLabelHeight = orderFontMetrics.getHeight();
		JLabel payPanelLabel = new JLabel("Pay");
		payPanelLabel.setFont(orderFont);
		payPanelLabel.setBounds((int)(WIDTH/2)-(int)(payPanelLabelWidth*.5), (int)(HEIGHT/2)-(int)(BUTTONHEIGHT*1.5), payPanelLabelWidth, payPanelLabelHeight);
		payPanel.add(payPanelLabel);

		int payPanelLabel2Width = orderFontMetrics.stringWidth("Order #" + tempOrder.orderNumber);
		int payPanelLabel2Height = orderFontMetrics.getHeight();
		JLabel payPanelLabel2 = new JLabel("Order #" + tempOrder.orderNumber);
		payPanelLabel2.setFont(orderFont);
		payPanelLabel2.setBounds((int)(WIDTH/2)-(int)(payPanelLabel2Width*.5), (int)(HEIGHT/2)-(int)(BUTTONHEIGHT*1.5)+payPanelLabel2Height, payPanelLabel2Width, payPanelLabel2Height);
		payPanel.add(payPanelLabel2);

		int totalPriceWidth = orderFontMetrics.stringWidth("Total: $" + df.format(tempOrder.total));
		int totalPriceHeight = orderFontMetrics.getHeight();
		JLabel totalPriceLabel = new JLabel("Total: $" + df.format(tempOrder.total));
		totalPriceLabel.setFont(orderFont);
		totalPriceLabel.setBounds((int)(WIDTH/2)-(int)(totalPriceWidth*.5), (int)(HEIGHT/2)+10, totalPriceWidth, totalPriceHeight);
		payPanel.add(totalPriceLabel);

		cashButton.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH*1.5), (int)(HEIGHT/2)+50, BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
		creditButton.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH*.5), (int)(HEIGHT/2)+50, BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
		giftCardButton.setBounds((int)(WIDTH/2)+(int)(BUTTONWIDTH*.5), (int)(HEIGHT/2)+50, BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
		backButtonPay.setBounds((int)(WIDTH/2)-BUTTONWIDTH, (int)(HEIGHT/2)+150, BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
		cancelOrderButtonPay.setBounds((int)(WIDTH/2), (int)(HEIGHT/2)+150, BUTTONWIDTH, (int)(BUTTONHEIGHT/2));

		payButton.setEnabled(true); //There is something to pay for, enable the button

		payPanel.add(cashButton);
		payPanel.add(creditButton);
		payPanel.add(giftCardButton);
		payPanel.add(backButtonPay);
		payPanel.add(cancelOrderButtonPay);

		int tempOrderSize = tempOrder.items.size();
		int track = tempOrderSize/2;
		
		//If there's 1 item in the order
		if(tempOrderSize == 1) {
			tempOrder.items.get(0).buttonPay.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH/2), (int)(HEIGHT/2)-(int)(BUTTONHEIGHT/2), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			payPanel.add(tempOrder.items.get(0).buttonPay);
		}

		//If there's an even number of items in the order
		if(tempOrderSize % 2 == 0) {
			track = tempOrderSize/2;
			//For first half of items (left)
			for(int i = (tempOrderSize/2)-1; i >= 0; i--) {
				tempOrder.items.get(i).buttonPay.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH*(i+1)), (int)(HEIGHT/2)-(int)(BUTTONHEIGHT/2), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
				payPanel.add(tempOrder.items.get(i).buttonPay);
			}
			//For second half of items (right)
			for(int i = 0; i < tempOrderSize/2; i++) {
				tempOrder.items.get(track).buttonPay.setBounds((int)(WIDTH/2)+(int)(BUTTONWIDTH*(i)), (int)(HEIGHT/2)-(int)(BUTTONHEIGHT/2), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
				payPanel.add(tempOrder.items.get(track).buttonPay);
				track += 1;
			}
		}

		//If there are an odd number of items in the order
		if(tempOrderSize % 2 != 0 && tempOrderSize > 1) {
			track = tempOrderSize/2;
			//For first half of items (left), not including middle item
			for(int i = (tempOrderSize/2)-1; i >= 0; i--) {
				tempOrder.items.get(i).buttonPay.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH*(i+1.5)), (int)(HEIGHT/2)-(int)(BUTTONHEIGHT/2), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
				payPanel.add(tempOrder.items.get(i).buttonPay);
			}
			tempOrder.items.get(track).buttonPay.setBounds((int)(WIDTH/2)-(int)(BUTTONWIDTH/2), (int)(HEIGHT/2)-(int)(BUTTONHEIGHT/2), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
			payPanel.add(tempOrder.items.get(track).buttonPay);
			track += 1;
			//For second half of items (right), not including middle item
			for(int i = 0; i < (tempOrderSize/2); i++) {
				tempOrder.items.get(track).buttonPay.setBounds((int)(WIDTH/2)+(int)(BUTTONWIDTH*(i+.5)), (int)(HEIGHT/2)-(int)(BUTTONHEIGHT/2), BUTTONWIDTH, (int)(BUTTONHEIGHT/2));
				payPanel.add(tempOrder.items.get(track).buttonPay);
				track += 1;
			}
		}
		for(int i = 0; i < tempOrder.items.size(); i++) {
			int j = i;
			tempOrder.items.get(j).buttonPay.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					SoundEffect.CLICK.play(); //Play click sound
					if(tempOrder.items.size() == 1 && !removed) {
						removed = true;
						String t = tempOrder.items.get(0).type;
						String styleCheck = tempOrder.items.get(0).style;
						String sizeCheck = tempOrder.items.get(0).size;
						tempOrder.items.remove(0);
						itemNumber--;
						if(t == "Cappuccino") {
							screen = "Cappuccino Modify";
							style = "Hot";
							if(styleCheck == "Hot") {
								highlightStyleButton(hotButtonCappuccinoModify);
							}
							if(sizeCheck == "S") {
								highlightSizeButton(smallButtonCappuccinoModify);
							}
							if(sizeCheck == "M") {
								highlightSizeButton(mediumButtonCappuccinoModify);
							}
							if(sizeCheck == "L") {
								highlightSizeButton(largeButtonCappuccinoModify);
							}
							render();
						}
						if(t == "Latte") {
							screen = "Latte Modify";
							if(styleCheck == "Hot") {
								highlightStyleButton(hotButtonLatteModify);
							}
							if(styleCheck == "Iced") {
								highlightStyleButton(icedButtonLatteModify);
							}
							if(styleCheck == "Blended") {
								highlightStyleButton(blendedButtonLatteModify);
							}
							if(sizeCheck == "S") {
								highlightSizeButton(smallButtonLatteModify);
							}
							if(sizeCheck == "M") {
								highlightSizeButton(mediumButtonLatteModify);
							}
							if(sizeCheck == "L") {
								highlightSizeButton(largeButtonLatteModify);
							}
							render();
						}
						if(t == "Mocha") {
							screen = "Mocha Modify";
							if(styleCheck == "Hot") {
								highlightStyleButton(hotButtonMochaModify);
							}
							if(styleCheck == "Iced") {
								highlightStyleButton(icedButtonMochaModify);
							}
							if(styleCheck == "Blended") {
								highlightStyleButton(blendedButtonMochaModify);
							}
							if(sizeCheck == "S") {
								highlightSizeButton(smallButtonMochaModify);
							}
							if(sizeCheck == "M") {
								highlightSizeButton(mediumButtonMochaModify);
							}
							if(sizeCheck == "L") {
								highlightSizeButton(largeButtonMochaModify);
							}
							render();
						}
						if(t == "Espresso") {
							screen = "Espresso Modify";
							style = "Hot";
							if(styleCheck == "Hot") {
								highlightStyleButton(hotButtonEspressoModify);
							}
							if(sizeCheck == "S") {
								highlightSizeButton(smallButtonEspressoModify);
							}
							if(sizeCheck == "L") {
								highlightSizeButton(largeButtonEspressoModify);
							}
							render();
						}
						if(t == "Tea") {
							screen = "Tea Modify";
							if(styleCheck == "Hot") {
								highlightStyleButton(hotButtonTeaModify);
							}
							if(styleCheck == "Iced") {
								highlightStyleButton(icedButtonTeaModify);
							}
							if(styleCheck == "Blended") {
								highlightStyleButton(blendedButtonTeaModify);
							}
							if(sizeCheck == "S") {
								highlightSizeButton(smallButtonTeaModify);
							}
							if(sizeCheck == "M") {
								highlightSizeButton(mediumButtonTeaModify);
							}
							if(sizeCheck == "L") {
								highlightSizeButton(largeButtonTeaModify);
							}
							render();
						}
						if(t == "Crema Roll") {
							screen = "Food Modify";
							render();
						}
						if(t == "Cookie") {
							screen = "Food Modify";
							render();
						}
						if(t == "Cheesecake") {
							screen = "Food Modify";
							render();
						}
						if(t == "Affogato") {
							screen = "Food Modify";
							render();
						}
						if(t == "Gelato") {
							screen = "Food Modify";
							render();
						}
					}
					if(tempOrder.items.size() > 1 && !removed) {
						String t = tempOrder.items.get(j).type;
						String styleCheck = tempOrder.items.get(j).style;
						String sizeCheck = tempOrder.items.get(j).size;
						if(t == "Cappuccino" && !removed) {
							screen = "Cappuccino Modify";
							style = "Hot";
							if(styleCheck == "Hot") {
								highlightStyleButton(hotButtonCappuccinoModify);
							}
							if(sizeCheck == "S") {
								highlightSizeButton(smallButtonCappuccinoModify);
							}
							if(sizeCheck == "M") {
								highlightSizeButton(mediumButtonCappuccinoModify);
							}
							if(sizeCheck == "L") {
								highlightSizeButton(largeButtonCappuccinoModify);
							}
							for(int k = 0; k <tempOrder.items.size(); k++) {
								if(tempOrder.items.get(k).itemNumber == tempOrder.items.get(j).itemNumber) {
									tempOrder.items.remove(k);
									itemNumber--;
									removed = true;
									break;
								}
							}
							render();
						}
						if(t == "Latte" && !removed) {
							screen = "Latte Modify";
							if(styleCheck == "Hot") {
								highlightStyleButton(hotButtonLatteModify);
							}
							if(styleCheck == "Iced") {
								highlightStyleButton(icedButtonLatteModify);
							}
							if(styleCheck == "Blended") {
								highlightStyleButton(blendedButtonLatteModify);
							}
							if(sizeCheck == "S") {
								highlightSizeButton(smallButtonLatteModify);
							}
							if(sizeCheck == "M") {
								highlightSizeButton(mediumButtonLatteModify);
							}
							if(sizeCheck == "L") {
								highlightSizeButton(largeButtonLatteModify);
							}
							for(int k = 0; k <tempOrder.items.size(); k++) {
								if(tempOrder.items.get(k).itemNumber == tempOrder.items.get(j).itemNumber) {
									tempOrder.items.remove(k);
									itemNumber--;
									removed = true;
									break;
								}
							}
							render();
						}
						if(t == "Mocha" && !removed) {
							screen = "Mocha Modify";
							if(styleCheck == "Hot") {
								highlightStyleButton(hotButtonMochaModify);
							}
							if(styleCheck == "Iced") {
								highlightStyleButton(icedButtonMochaModify);
							}
							if(styleCheck == "Blended") {
								highlightStyleButton(blendedButtonMochaModify);
							}
							if(sizeCheck == "S") {
								highlightSizeButton(smallButtonMochaModify);
							}
							if(sizeCheck == "M") {
								highlightSizeButton(mediumButtonMochaModify);
							}
							if(sizeCheck == "L") {
								highlightSizeButton(largeButtonMochaModify);
							}
							for(int k = 0; k <tempOrder.items.size(); k++) {
								if(tempOrder.items.get(k).itemNumber == tempOrder.items.get(j).itemNumber) {
									tempOrder.items.remove(k);
									itemNumber--;
									removed = true;
									break;
								}
							}
							render();
						}
						if(t == "Espresso" && !removed) {
							screen = "Espresso Modify";
							style = "Hot";
							if(styleCheck == "Hot") {
								highlightStyleButton(hotButtonEspressoModify);
							}
							if(sizeCheck == "S") {
								highlightSizeButton(smallButtonEspressoModify);
							}
							if(sizeCheck == "L") {
								highlightSizeButton(largeButtonEspressoModify);
							}
							for(int k = 0; k <tempOrder.items.size(); k++) {
								if(tempOrder.items.get(k).itemNumber == tempOrder.items.get(j).itemNumber) {
									tempOrder.items.remove(k);
									itemNumber--;
									removed = true;
									break;
								}
							}
							render();
						}
						if(t == "Tea" && !removed) {
							screen = "Tea Modify";
							if(styleCheck == "Hot") {
								highlightStyleButton(hotButtonTeaModify);
							}
							if(styleCheck == "Iced") {
								highlightStyleButton(icedButtonTeaModify);
							}
							if(styleCheck == "Blended") {
								highlightStyleButton(blendedButtonTeaModify);
							}
							if(sizeCheck == "S") {
								highlightSizeButton(smallButtonTeaModify);
							}
							if(sizeCheck == "M") {
								highlightSizeButton(mediumButtonTeaModify);
							}
							if(sizeCheck == "L") {
								highlightSizeButton(largeButtonTeaModify);
							}
							for(int k = 0; k <tempOrder.items.size(); k++) {
								if(tempOrder.items.get(k).itemNumber == tempOrder.items.get(j).itemNumber) {
									tempOrder.items.remove(k);
									itemNumber--;
									removed = true;
									break;
								}
							}
							render();
						}
						if(t == "Crema Roll" && !removed) {
							screen = "Food Modify";
							for(int k = 0; k <tempOrder.items.size(); k++) {
								if(tempOrder.items.get(k).itemNumber == tempOrder.items.get(j).itemNumber) {
									tempOrder.items.remove(k);
									itemNumber--;
									removed = true;
									break;
								}
							}
							render();
						}
						if(t == "Cookie" && !removed) {
							screen = "Food Modify";
							for(int k = 0; k <tempOrder.items.size(); k++) {
								if(tempOrder.items.get(k).itemNumber == tempOrder.items.get(j).itemNumber) {
									tempOrder.items.remove(k);
									itemNumber--;
									removed = true;
									break;
								}
							}
							render();
						}
						if(t == "Cheesecake" && !removed) {
							screen = "Food Modify";
							for(int k = 0; k <tempOrder.items.size(); k++) {
								if(tempOrder.items.get(k).itemNumber == tempOrder.items.get(j).itemNumber) {
									tempOrder.items.remove(k);
									itemNumber--;
									removed = true;
									break;
								}
							}
							render();
						}
						if(t == "Affogato" && !removed) {
							screen = "Food Modify";
							for(int k = 0; k <tempOrder.items.size(); k++) {
								if(tempOrder.items.get(k).itemNumber == tempOrder.items.get(j).itemNumber) {
									tempOrder.items.remove(k);
									itemNumber--;
									removed = true;
									break;
								}
							}
							render();
						}
						if(t == "Gelato" && !removed) {
							screen = "Food Modify";
							for(int k = 0; k <tempOrder.items.size(); k++) {
								if(tempOrder.items.get(k).itemNumber == tempOrder.items.get(j).itemNumber) {
									tempOrder.items.remove(k);
									itemNumber--;
									removed = true;
									break;
								}
							}
							render();
						}
					}
				}
			});
		}
		frame.add(payPanel);
	}

	//start() method - Starts the thread for the program if it isn't already running
	public synchronized void start() {
		if(isRunning) return; //If the program is already running, exit method
		isRunning = true; //Set boolean to true to show that it is running
		thread = new Thread(this); //Create a new thread
		thread.start(); //Start the thread
	}

	//stop() method - Stops the thread for the program if it is running
	public synchronized void stop() {
		if(!isRunning) return; //If the program is stopped, exit method
		isRunning = false; //Set boolean to false to show that the program is no longer running
		//Attempt to join thread (close the threads, prevent memory leaks)
		try {
			thread.join();
		}
		//If there is an error, print the stack trace for debugging
		catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	//buttonVisible(boolean) method - Accepts a boolean, if true it makes all buttons visible, if false it makes all buttons invisible
	public void buttonsVisible(boolean b) {
			//For all image buttons
			for(int i = 0; i < imageButtons.size(); i++) {
				imageButtons.get(i).setVisible(b); //Set the button size
			}
			//For all normal buttons
			for(int i = 0; i < normalButtons.size(); i++) {
				normalButtons.get(i).setVisible(b); //Set the button size
			}
	}

	//highlightStyleButton(JButton) method - Accepts a style JButton, unhighlights other style buttons and highlights the passed one
	public void highlightStyleButton(JButton b) {
		//Set defaults for each image button, text is centered and at bottom
		for(int i = 0; i < styleButtons.size(); i++) {
			styleButtons.get(i).setOpaque(false);
		}
		b.setBackground(Color.BLACK);
		b.setOpaque(true);
		render();
	}

	//highlightSizebutton(JButton) method - Accepts a size JButton, unhighlights other size buttons and highlights the passed one
	public void highlightSizeButton(JButton b) {
		//Set defaults for each non-image button, text is centered and half button height
		for(int i = 0; i < sizeButtons.size(); i++) {
			sizeButtons.get(i).setOpaque(false);
		}
		b.setBackground(Color.BLACK);
		b.setOpaque(true);
		render();
	}

	//render() method - Renders each frame
	public void render() {
		tempOrder.tick(); //Call tempOrder.tick() to update the order
		newOrderPanel(); //Call newOrderPanel() to generate New Order panel
		payPanel(); //Call payPanel() to generate new Pay panel
		resetPanels(); //Set all panels to not visible
		if(tempOrder.items.size() == 0) { //If there are no items
			payButton.setEnabled(false); //You can't pay
		}
		if(tempOrder.items.size() > 0) { //If there's items
			payButton.setEnabled(true); //You can pay
		}
		if(screen == "Home") {
			homePanel.setVisible(true); //Set visible
		}
		if(screen == "New Order") {
			newOrderPanel.setVisible(true); //Set visible
		}
		if(screen == "Drinks") {
			drinksPanel.setVisible(true); //Set visible
		}
		if(screen == "Cappuccino") {
			cappuccinoPanel.setVisible(true); //Set visible
		}
		if(screen == "Latte") {
			lattePanel.setVisible(true); //Set visible
		}
		if(screen == "Mocha") {
			mochaPanel.setVisible(true); //Set visible
		}
		if(screen == "Espresso") {
			espressoPanel.setVisible(true); //Set visible
		}
		if(screen == "Tea") {
			teaPanel.setVisible(true); //Set visible
		}
		if(screen == "Cappuccino Modify") {
			cappuccinoPanelModify.setVisible(true); //Set visible
		}
		if(screen == "Latte Modify") {
			lattePanelModify.setVisible(true); //Set visible
		}
		if(screen == "Mocha Modify") {
			mochaPanelModify.setVisible(true); //Set visible
		}
		if(screen == "Espresso Modify") {
			espressoPanelModify.setVisible(true); //Set visible
		}
		if(screen == "Tea Modify") {
			teaPanelModify.setVisible(true); //Set visible
		}
		if(screen == "Food") {
			foodPanel.setVisible(true); //Set visible
		}
		if(screen == "Food Modify") {
			foodPanelModify.setVisible(true);
		}
		if(screen == "Pay") {
			payPanel.setVisible(true); //Set visible
		}
	}

	//run() method - The program loop
	@Override
	public void run() {
		requestFocus(); //So window is selected when it opens
		render();
		String screenChanged; //Holds the screen name
		while(isRunning) {
			screenChanged = screen; //Assign current screen name
			//If the current screen is not as the last screen name we need to render
			if(screenChanged != screen) {
				render(); //Render
			}
		}
		stop(); //Stop the program
	}

	//Main
	public static void main(String[] args) {
		Crema crema = new Crema(); //Create new Crema object
		crema.start(); //Call start method in program object, starts the program
	}
}