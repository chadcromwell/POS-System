/********************************************************************************************************************
Author: Chad Cromwell
Date: December 15th, 2017
Assignment: 3
Program: Item.java
Description: A class that represents an item sold at Crema
Constructors:
			Item(String type) - For food items, ex. Item("Crema Roll")
			Item(String size, String type, String style) - For drink items, ex. Item("L", "Mocha", "Hot")
			Accepted sizes: L, l, M, m, S, s
			Accepted types: Crema Roll, Cookie, Cheesecake, Affogato, Gelato
			Accepted Style: Hot, Iced, Blended
********************************************************************************************************************/
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.*;
import javax.imageio.*;

public class Item {
	public String size; //Size of item
	public String type; //Type of item. Ex. "Cappuccino"
	public String style; //Style of item. Ex. "Iced"
	public double price; //Price of item
	public static double smallCappuccinoPrice = 3.50; //Small Cappuccino price
	public static double mediumCappuccinoPrice = 4.00; //Medium Cappuccino price
	public static double largeCappuccinoPrice = 4.50; //Large Cappuccino price
	public static double smallLattePrice = 3.50; //Small Latte price
	public static double mediumLattePrice = 4.00; //Medium Latte price
	public static double largeLattePrice = 4.50; //Large Latte price
	public static double smallMochaPrice = 4.00; //Small Mocha price
	public static double mediumMochaPrice = 4.50; //Medium Mocha price
	public static double largeMochaPrice = 5.00; //Large Mocha price
	public static double smallEspressoPrice = 2.00; //Small Espresso price
	public static double largeEspressoPrice = 3.00; //Large Espresso price
	public static double teaPrice = 4.00; //Tea price
	public static double cremaRollPrice = 6.00; //Crema Roll price
	public static double cookiePrice = 2.00; //Cookie price
	public static double cheesecakePrice = 6.00; //Cheesecake price
	public static double affogatoPrice = 5.00; //Affogato price
	public static double gelatoPrice = 5.00; //Gelato price
	public boolean isDrink; //Whether it is a drink or food item
	public boolean isDone; //Whether the item is made or not
	public JButton button; //Item's button for new order screen
	public JButton buttonPay; //Item's button for pay screen
	public Image cappuccinoImage = Crema.cappuccinoImage; //Cappuccino image for button
	public Image latteImage = Crema.latteImage; //Latte image for button
	public Image mochaImage = Crema.mochaImage; //Mocha image for button
	public Image espressoImage = Crema.espressoImage; //Espresso image for button
	public Image teaImage = Crema.teaImage; //Tea image for button
	public Image cremaRollImage = Crema.cremaRollImage; //Crema Roll image for button
	public Image cookieImage = Crema.cookieImage; //Cookie image for button
	public Image cheesecakeImage = Crema.cheesecakeImage; //Cheesecake image for button
	public Image affogatoImage = Crema.affogatoImage; //Affogato image for button
	public Image gelatoImage = Crema.gelatoImage; //Gelato image for button
	DecimalFormat df = new DecimalFormat("0.00"); //Format used to convert numbers into dollar amounts
	public int itemNumber = Crema.totalOrders + Crema.itemNumber; //Unique item number

	//Item(String) Constructor - For food items, accepts String type. ex. "Crema Roll" or "Cheesecake"
	public Item(String type) {
		isDone = false; //Future implementation
		this.type = type; //Capture type
		if(type == "Crema Roll") { //If it's a Crema Roll
			price = cremaRollPrice; //Set the price
			button = new JButton("<html><center>" + type + "<br/>" + df.format(price) + "</center></html>"); //Add type and price to button for new order screen
			buttonPay = new JButton("<html><center>" + type + "<br/>" + df.format(price) + "</center></html>"); //Add type and price to button for pay screen
			isDrink = false; //It's not a drink
			try{
				button.setIcon(new ImageIcon(cremaRollImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH))); //Set image
				buttonPay.setIcon(new ImageIcon(cremaRollImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH))); //Set image
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(type == "Cookie") { //All logic below fallows that above
			price = cookiePrice;
			button = new JButton("<html><center>" + type + "<br/>" + df.format(price) + "</center></html>");
			buttonPay = new JButton("<html><center>" + type + "<br/>" + df.format(price) + "</center></html>");
			isDrink = false;
			try{
				button.setIcon(new ImageIcon(cookieImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
				buttonPay.setIcon(new ImageIcon(cookieImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(type == "Cheesecake") {
			price = cheesecakePrice;
			button = new JButton("<html><center>" + type + "<br/>" + df.format(price) + "</center></html>");
			buttonPay = new JButton("<html><center>" + type + "<br/>" + df.format(price) + "</center></html>");
			isDrink = false;
			try{
				button.setIcon(new ImageIcon(cheesecakeImage.getScaledInstance(90, 90, Image.SCALE_SMOOTH)));
				buttonPay.setIcon(new ImageIcon(cheesecakeImage.getScaledInstance(90, 90, Image.SCALE_SMOOTH)));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(type == "Affogato") {
			price = affogatoPrice;
			button = new JButton("<html><center>" + type + "<br/>" + df.format(price) + "</center></html>");
			buttonPay = new JButton("<html><center>" + type + "<br/>" + df.format(price) + "</center></html>");
			isDrink = false;
			try{
				button.setIcon(new ImageIcon(affogatoImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
				buttonPay.setIcon(new ImageIcon(affogatoImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(type == "Gelato") {
			price = gelatoPrice;
			button = new JButton("<html><center>" + type + "<br/>" + df.format(price) + "</center></html>");
			buttonPay = new JButton("<html><center>" + type + "<br/>" + df.format(price) + "</center></html>");
			isDrink = false;
			try{
				button.setIcon(new ImageIcon(gelatoImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
				buttonPay.setIcon(new ImageIcon(gelatoImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		button.setFocusPainted(false); //Don't have it highlighted initially
		buttonPay.setFocusPainted(false); //Don't have it highlighted initially
		Crema.itemNumber++; //Increment the itemNumber
	}

	//Item(String, String, String) Constructor - Accepts 3 Strings, size, type, and style, follows the same logic as Item()
	public Item(String size, String type, String style) {
		isDone = false;
		this.size = size;
		this.type = type;
		this.style = style;

		//If it's a Cappuccino
		if(type == "Cappuccino") {
			this.style = "Hot";
			if(size == "s" || size == "S") {
				price = smallCappuccinoPrice;
			}
			if(size == "m" || size == "M") {
				price = mediumCappuccinoPrice;
			}
			if(size == "l" || size == "L") {
				price = largeCappuccinoPrice;
			}
			isDrink = true;
			button = new JButton("<html><center>" + style + "<br/>" + type + "<br/>" + size + "<br/>$" + df.format(price) + "</center></html>");
			buttonPay = new JButton("<html><center>" + style + "<br/>" + type + "<br/>" + size + "<br/>$" + df.format(price) + "</center></html>");
			try{
				button.setIcon(new ImageIcon(cappuccinoImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
				buttonPay.setIcon(new ImageIcon(cappuccinoImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(type == "Latte") {
			if(size == "s" || size == "S") {
				price = smallLattePrice;
			}
			if(size == "m" || size == "M") {
				price = mediumLattePrice;
			}
			if(size == "l" || size == "L") {
				price = largeLattePrice;
			}
			isDrink = true;
			button = new JButton("<html><center>" + style + "<br/>" + type + "<br/>" + size + "<br/>$" + df.format(price) + "</center></html>");
			buttonPay = new JButton("<html><center>" + style + "<br/>" + type + "<br/>" + size + "<br/>$" + df.format(price) + "</center></html>");
			try{
				button.setIcon(new ImageIcon(latteImage.getScaledInstance(85, 85, Image.SCALE_SMOOTH)));
				buttonPay.setIcon(new ImageIcon(latteImage.getScaledInstance(85, 85, Image.SCALE_SMOOTH)));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(type == "Mocha") {
			if(size == "s" || size == "S") {
				price = smallMochaPrice;
			}
			if(size == "m" || size == "M") {
				price = mediumMochaPrice;
			}
			if(size == "l" || size == "L") {
				price = largeMochaPrice;
			}
			isDrink = true;
			button = new JButton("<html><center>" + style + "<br/>" + type + "<br/>" + size + "<br/>$" + df.format(price) + "</center></html>");
			buttonPay = new JButton("<html><center>" + style + "<br/>" + type + "<br/>" + size + "<br/>$" + df.format(price) + "</center></html>");
			try{
				button.setIcon(new ImageIcon(mochaImage.getScaledInstance(80, 80, Image.SCALE_SMOOTH)));
				buttonPay.setIcon(new ImageIcon(mochaImage.getScaledInstance(80, 80, Image.SCALE_SMOOTH)));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(type == "Espresso") {
			this.style = "Hot";
			if(size == "s" || size == "S") {
				price = smallEspressoPrice;
			}
			if(size == "l" || size == "L") {
				price = largeEspressoPrice;
			}
			isDrink = true;
			button = new JButton("<html><center>" + style + "<br/>" + type + "<br/>" + size + "<br/>$" + df.format(price) + "</center></html>");
			buttonPay = new JButton("<html><center>" + style + "<br/>" + type + "<br/>" + size + "<br/>$" + df.format(price) + "</center></html>");
			try{
				button.setIcon(new ImageIcon(espressoImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
				buttonPay.setIcon(new ImageIcon(espressoImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(type == "Tea") {
			price = teaPrice;
			isDrink = true;
			button = new JButton("<html><center>" + style + "<br/>" + type + "<br/>" + size + "<br/>$" + df.format(price) + "</center></html>");
			buttonPay = new JButton("<html><center>" + style + "<br/>" + type + "<br/>" + size + "<br/>$" + df.format(price) + "</center></html>");
			try{
				button.setIcon(new ImageIcon(teaImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
				buttonPay.setIcon(new ImageIcon(teaImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		button.setFocusPainted(false);
		buttonPay.setFocusPainted(false);
		Crema.itemNumber++;
	}
}