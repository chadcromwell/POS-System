/********************************************************************************************************************
Author: Chad Cromwell
Date: December 15th, 2017
Assignment: 3
Program: Order.java
Description: A class that represents an order of items at Crema
Methods:
		tick() method - What is computed each frame
		orderComplete() method - Calculates how long the order took and marks the order as finished
********************************************************************************************************************/
import java.util.*;

public class Order {
	ArrayList<Item> items; //ArrayList that holds all the items of the order
	ArrayList<Item> drinks; //ArrayList that holds all the drinks of the order
	ArrayList<Item> food; //ArrayList that holds all the food of the order
	int orderNumber; //The order number
	int totalDrinks; //Total drinks in this order
	int totalFood; //Total food in this order
	int numNotDone; //Counts how many items are not finished, used to count for when to complete an order
	double subtotal; //Subtotal (before taxes)
	double taxes = Crema.TAXES; //Taxes
	double total; //Final total of order
	String customerName; //The customer's name
	String paymentMethod; //How the customer paid
	boolean beingMade; //Whether the order is being made or not
	boolean isDone; //Whether the order is done or not
	boolean paymentDone; //Whether the order is paid for
	Date dateStarted; //Date and time when the order started
	Date dateCompleted; //Date and time when the order was completed
	long dateHowLong; //Date and time for how long the order took to be made

	//Order(ArrayList<Item>) Constructor - Accepts an ArraryList of Item objects
	public Order() {
		Crema.totalOrders += 1; //Increase the total number of orders created
		orderNumber = Crema.totalOrders; //Assign the order number
		items = new ArrayList<Item>(); //Initialize items array list
		drinks = new ArrayList<Item>(); //Initialize drinks array list
		food = new ArrayList<Item>(); //Initialize food array list
		dateStarted = new Date(); //Initialize the date and time the order started
		beingMade = false; //The order is now being made
		isDone = false; //The order is not done yet
		paymentDone = false;
	}

	//tick() method - What is computed each frame
	public void tick() {
		subtotal = 0;
		total = 0;
		//For each item
		for(int i = 0; i < this.items.size(); i++) {
			//If the item is a drink
			if(this.items.get(i).isDrink) {
				totalDrinks += 1; //Increment the total number of drinks
				this.drinks.add(this.items.get(i)); //Add this item to the drinks ArrayList
			}
			//If the item is food
			if(!this.items.get(i).isDrink) {
				totalFood += 1; //Incremember the total number of food items
				this.food.add(this.items.get(i)); //Add this item to the food ArrayList
			}
			subtotal += this.items.get(i).price; //Increment subtotal amount by each item's price
		}
		total = subtotal*taxes; //Calculate final total

		if(!isDone && beingMade) {
			for(int i = 0; i < items.size(); i++) {
				//If any of the items are not done
				if(!items.get(i).isDone) {
					numNotDone++; //Increment numNotDone to indicate that there are items that are still not finished
				}
			}
			//If all items are done being made
			if(numNotDone == 0) {
				orderComplete(); //If isDone is == 0, all items are done being made
			}
			//Items still need to be made
			else {
				numNotDone = 0; //All items are not finished so we must iterate again
			}
		}
	}

	//orderComplete() method - Calculates how long the order took and marks the order as finished
	public void orderComplete() {
		beingMade = false; //The order is done being made
		isDone = true; //The order is done
		dateCompleted = new Date(); //Initialize date and time when the order is completed
		dateHowLong = dateCompleted.getTime()-dateStarted.getTime(); //Calculate how long the order took to make
	}
}