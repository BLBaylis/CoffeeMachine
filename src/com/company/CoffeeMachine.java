package com.company;

import java.util.Scanner;

public class CoffeeMachine {
    private int waterLevelMl;
    private int milkLevelMl;
    private int coffeeBeansLeftGrams;
    private int disposableCups;
    private int balance;
    private final int[] espresso = {250, 0, 16, 4};
    private final int[] latte = {350, 75, 20, 7};
    private final int[] cappuccino = {200, 100, 12, 6};
    private CoffeeMachineStatus status;
    private RefillType refillType;
    private boolean on;

    public enum CoffeeMachineStatus {
        WAITING_FOR_ACTION,
        WAITING_FOR_COFFEE_TYPE,
        WAITING_FOR_REFILL_VALUE
    }

    public enum RefillType {
        WATER,
        MILK,
        COFFEE_BEANS,
        DISPOSABLE_CUPS
    }

    private CoffeeMachine(int waterLevelMl, int milkLevelMl, int coffeeBeansLeftGrams, int disposableCups, int balance) {
        this.waterLevelMl = waterLevelMl;
        this.milkLevelMl = milkLevelMl;
        this.coffeeBeansLeftGrams = coffeeBeansLeftGrams;
        this.disposableCups = disposableCups;
        this.balance = balance;
        this.status = CoffeeMachineStatus.WAITING_FOR_ACTION;
        this.refillType = RefillType.WATER;
        this.on = true;
    }

    private void makeCoffee(int[] coffeeTypeValues) {
        String[] supplyType = {"water", "milk", "coffee beans", "cups"};
        boolean[] supplyAmountCheck = {
                this.waterLevelMl - coffeeTypeValues[0] > 0,
                this.milkLevelMl - coffeeTypeValues[1] > 0,
                this.coffeeBeansLeftGrams - coffeeTypeValues[2] > 0,
                this.disposableCups - 1 > 0
        };
        String message = "I have enough supplies, making you a coffee!";
        for (int index = 0; index < supplyAmountCheck.length; index++) {
            if (!supplyAmountCheck[index] && index == 0) {
                message = "Sorry, not enough: water";
            } else if (!supplyAmountCheck[index]) {
                message += ", " + supplyType[index];
            }
        }
        if (message.equals("I have enough supplies, making you a coffee!")){
            this.waterLevelMl -= coffeeTypeValues[0];
            this.milkLevelMl -= coffeeTypeValues[1];
            this.coffeeBeansLeftGrams -= coffeeTypeValues[2];
            this.disposableCups -= 1;
            this.balance += coffeeTypeValues[3];
        }
        System.out.println();
        System.out.println(message);
        System.out.println();
        this.status = CoffeeMachineStatus.WAITING_FOR_ACTION;
    }

    private void withdrawMoney() {
        int prevBalance = this.balance;
        this.balance = 0;
        System.out.println("I gave you $" + prevBalance);
        System.out.println();
    }

    private void suppliesReport() {
        System.out.println();
        System.out.println("The coffee machine has: ");
        System.out.println(this.waterLevelMl + " of water");
        System.out.println(this.milkLevelMl + " of milk");
        System.out.println(this.coffeeBeansLeftGrams + " of coffee beans");
        System.out.println(this.disposableCups + " of disposable cups");
        System.out.println(this.balance + " of money");
        System.out.println();
    }

    private void refillMachine(int refillValue) {
        switch (this.refillType) {
            case WATER:
                this.waterLevelMl += refillValue;
                this.refillType = RefillType.MILK;
                refillPrompt();
                break;
            case MILK:
                this.milkLevelMl += refillValue;
                this.refillType = RefillType.COFFEE_BEANS;
                refillPrompt();
                break;
            case COFFEE_BEANS:
                this.coffeeBeansLeftGrams += refillValue;
                this.refillType = RefillType.DISPOSABLE_CUPS;
                refillPrompt();
                break;
            case DISPOSABLE_CUPS:
                this.disposableCups += refillValue;
                this.refillType = RefillType.WATER;
                this.status = CoffeeMachineStatus.WAITING_FOR_ACTION;
                break;
            default:
                System.out.println("Error, unknown refill type.");
                refillPrompt();
                break;
        }
    }

    private void buyCoffee(String coffeeType) {
        switch (coffeeType) {
            case "1":
                this.makeCoffee(espresso);
                break;
            case "2":
                this.makeCoffee(latte);
                break;
            case "3":
                this.makeCoffee(cappuccino);
                break;
            case "back":
                this.status = CoffeeMachineStatus.WAITING_FOR_ACTION;
                break;
            default:
                System.out.println("Invalid option.");
                this.coffeePrompt();
                break;
        }
    }

    private void handleActionInput(String actionType) {
        switch (actionType) {
            case "buy":
                this.coffeePrompt();
                this.status = CoffeeMachineStatus.WAITING_FOR_COFFEE_TYPE;
                break;
            case "fill":
                refillPrompt();
                this.status = CoffeeMachineStatus.WAITING_FOR_REFILL_VALUE;
                this.refillType = RefillType.WATER;
                break;
            case "take":
                this.withdrawMoney();
                break;
            case "remaining":
                this.suppliesReport();
                break;
            case "exit":
                this.on = false;
                break;
            default:
                System.out.println("Invalid option.");
                actionPrompt();
                break;
        }
    }

    private void run(String userInput) {
        switch (this.status) {
            case WAITING_FOR_ACTION:
                this.handleActionInput(userInput);
                break;
            case WAITING_FOR_COFFEE_TYPE:
                this.buyCoffee(userInput);
                break;
            case WAITING_FOR_REFILL_VALUE:
                try {
                    int refillValue = Integer.parseInt(userInput);
                    this.refillMachine(refillValue);
                } catch (Exception err) {
                    System.out.println("Please give a number.");
                    refillPrompt();
                }
                break;
            default:
                System.out.println("Error! Invalid Coffee Machine Status");
                break;
        }
    }

    private void coffeePrompt() {
        System.out.println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu: ");
    }

    private void actionPrompt() {
        System.out.println("Input an action (buy, fill, take, remaining, exit): ");
    }

    private void refillPrompt() {
        switch (this.refillType) {
            case WATER:
                System.out.println("How many ml of water would you like to add?");
                break;
            case MILK:
                System.out.println("How many ml of milk would you like to add?");
                break;
            case COFFEE_BEANS:
                System.out.println("How many grams of coffee beans would you like to add?");
                break;
            case DISPOSABLE_CUPS:
                System.out.println("How many disposable coffee cups would you like to add?");
                break;
            default:
                System.out.println("Error, unknown refill type.");
                break;
        }
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CoffeeMachine myMachine = new CoffeeMachine(400, 540, 120, 9, 550);
        System.out.println("Welcome to Brad's Coffee Machine!");
        while (myMachine.on) {
            if (myMachine.status.equals(CoffeeMachineStatus.WAITING_FOR_ACTION)) {
                myMachine.actionPrompt();
            }
            myMachine.run(scanner.next());
        }
    }
}

