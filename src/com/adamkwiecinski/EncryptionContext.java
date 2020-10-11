package com.adamkwiecinski;

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
