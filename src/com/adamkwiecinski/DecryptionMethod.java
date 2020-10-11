package com.adamkwiecinski;

interface DecryptionMethod {
    String decrypt(int key, String input);
    String decryptFromFile(String path, int key);
}
