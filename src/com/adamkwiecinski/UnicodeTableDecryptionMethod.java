package com.adamkwiecinski;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class UnicodeTableDecryptionMethod implements DecryptionMethod {

    @Override
    public String decrypt(int key, String input) {
        StringBuilder message = new StringBuilder(input);

        for (int i = 0; i < message.length(); i++) {

            int asc = message.charAt(i);
            int rev = asc - key;
            if (rev > 127) {
                rev = (rev - 128);
            }
            message.setCharAt(i, (char) rev);
        }

        return message.toString();
    }

    @Override
    public String decryptFromFile(String path, int key){

        File file = new File(path);
        String message = "";

        StringBuilder decryptedMessage = new StringBuilder(message);

        try (Scanner scanner = new Scanner(file)) {

            while (scanner.hasNext()) {
                message = this.decrypt(key,scanner.nextLine());
                decryptedMessage.append(message);
            }

        } catch (FileNotFoundException e) {
            System.out.println("No file found: " + path);
        }

        return decryptedMessage.toString();
    }
}
