package challenge;

import challenge.messages.Message;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Message message = new Message();
        message.displayMainMenu();
    }
}

