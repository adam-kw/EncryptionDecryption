package com.adamkwiecinski;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

interface EncryptionMethod {
    String encrypt(int key, String input);
    String encryptFromFile(String path, int key);
}

interface DecryptionMethod {
    String decrypt(int key, String input);
    String decryptFromFile(String path, int key);
}

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

class UnicodeTableEncryptionMethod implements EncryptionMethod {

    @Override
    public String encrypt(int key, String input) {
        StringBuilder message = new StringBuilder(input);

        for (int i = 0; i < message.length(); i++) {

            int asc = message.charAt(i);
            int rev = asc + key;
            if (rev > 127) {
                rev = (rev - 128);
            }
            message.setCharAt(i, (char) rev);
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

class ShiftDecryptionMethod implements DecryptionMethod {

    @Override
    public String decrypt(int key, String input) {

        StringBuilder message = new StringBuilder(input);

        for (int i = 0; i < message.length(); i++) {

            if(!Character.toString(message.charAt(i)).matches("[a-zA-z]*\\s*")){
                continue;
            }

            int asc = message.charAt(i);

            //65-90 ASCII a-z
            //97-122 ASCII A-Z
            if(asc >= 65 && asc <= 90){
                int rev = asc - key;
                if (rev < 65) {
                    rev = 91 - (65 - rev);
                }
                message.setCharAt(i, (char) rev);
            }

            if(asc >= 97 && asc <= 122){
                int rev = asc - key;
                if (rev < 97) {
                    rev = 123 - (97 - rev);
                }
                message.setCharAt(i, (char) rev);
            }
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


class EncryptionContext {
    private EncryptionMethod method;

    public void setMethod(EncryptionMethod method){
        this.method = method;
    }

    public String encrypt(int key, String input){
        return this.method.encrypt(key, input);
    }

    public String encryptFromFile(String path, int key){
        return this.method.encryptFromFile(path, key);
    }

}

class DecryptionContext {
    private DecryptionMethod method;

    public void setMethod(DecryptionMethod method){
        this.method = method;
    }

    public String decrypt(int key, String input){
        return this.method.decrypt(key, input);
    }

    public String decryptFromFile(String path, int key){
        return this.method.decryptFromFile(path, key);
    }

}




public class Main {

    public static void writeIntoFile(String path, String encryptedMessage){
        File file = new File(path);

        try (FileWriter writer = new FileWriter(file)) {

            writer.write(encryptedMessage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){

        /*
        Input example 1:
        java Main -mode enc -in road_to_treasure.txt -out protected.txt -key 5 -alg unicode

        Input example 2:
        java Main -key 5 -alg unicode -data "\jqhtrj%yt%m~ujwxpnqq&" -mode dec
         */

        String mode = "enc";
        String data = "";
        String out = "";
        String path;
        String alg = "unicode";
        int key = 0;

        try {
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("-data") || args[i].equals("-in")) {
                    data = args[i + 1];
                }
                if (args[i].equals("-mode")) {
                    mode = args[i + 1];
                }
                if (args[i].equals("-key")) {
                    key = Integer.parseInt(args[i + 1]);
                }
                if (args[i].equals("-out")) {
                    out = args[i + 1];
                }
                if (args[i].equals("-alg")) {
                    alg = args[i + 1];
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Missing option");
        }

        File file = new File(data);

        if (file.exists() && !file.isDirectory()) {
            path = data;

            switch (mode){
                case "enc":
                    EncryptionContext encryptionContext = new EncryptionContext();
                    if(alg.equals("shift")) {
                        encryptionContext.setMethod(new ShiftEncryptionMethod());
                    } else {
                        encryptionContext.setMethod(new UnicodeTableEncryptionMethod());
                    }
                    data = encryptionContext.encryptFromFile(path, key);
                    break;

                case "dec":
                    DecryptionContext decryptionContext = new DecryptionContext();
                    if(alg.equals("shift")) {
                        decryptionContext.setMethod(new ShiftDecryptionMethod());
                    } else {
                        decryptionContext.setMethod(new UnicodeTableDecryptionMethod());
                    }
                    data = decryptionContext.decryptFromFile(path,key);
                    break;
            }

        } else {
            switch (mode){
                case "enc":
                    EncryptionContext encryptionContext = new EncryptionContext();
                    if(alg.equals("shift")) {
                        encryptionContext.setMethod(new ShiftEncryptionMethod());
                    } else {
                        encryptionContext.setMethod(new UnicodeTableEncryptionMethod());
                    }
                    data = encryptionContext.encrypt(key, data);
                    break;

                case "dec":
                    DecryptionContext decryptionContext = new DecryptionContext();
                    if(alg.equals("shift")) {
                        decryptionContext.setMethod(new ShiftDecryptionMethod());
                    } else {
                        decryptionContext.setMethod(new UnicodeTableDecryptionMethod());
                    }
                    data = decryptionContext.decrypt(key, data);
                    break;
            }
        }

        if (out.isEmpty()) {
            System.out.println(data);
        } else {
            writeIntoFile(out, data);
        }

    }
}

