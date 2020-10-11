package com.adamkwiecinski;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


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

