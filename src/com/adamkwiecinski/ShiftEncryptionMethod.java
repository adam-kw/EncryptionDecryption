package com.adamkwiecinski;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class ShiftEncryptionMethod implements EncryptionMethod {

    @Override
    public String encrypt(int key, String input) {

        StringBuilder message = new StringBuilder(input);

        for (int i = 0; i < message.length(); i++) {

            if(!Character.toString(message.charAt(i)).matches("[a-zA-z]*\\s*")){
                continue;
            }

            int asc = message.charAt(i);

            //65-90 ASCII a-z
            //97-122 ASCII A-Z
            if(asc >= 65 && asc <= 90){
                int rev = asc + key;
                if (rev > 90) {
                    rev = 65 + (rev - 91);
                }
                message.setCharAt(i, (char) rev);
            }

            if(asc >= 97 && asc <= 122){
                int rev = asc + key;
                if (rev > 122) {
                    rev = 97 + (rev - 123);
                }
                message.setCharAt(i, (char) rev);
            }

        }

        return message.toString();
    }

    @Override
    public String encryptFromFile(String path, int key){

        File file = new File(path);
        String message = "";

        StringBuilder encryptedMessage = new StringBuilder(message);

        try (Scanner scanner = new Scanner(file)) {

            while (scanner.hasNext()) {
                message = this.encrypt(key,scanner.nextLine());
                encryptedMessage.append(message);
            }

        } catch (FileNotFoundException e) {
            System.out.println("No file found: " + path);
        }

        return encryptedMessage.toString();
    }

}
