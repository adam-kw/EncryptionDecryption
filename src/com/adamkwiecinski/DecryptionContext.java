package com.adamkwiecinski;

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
