package com.adamkwiecinski;

interface EncryptionMethod {
    String encrypt(int key, String input);
    String encryptFromFile(String path, int key);
}
