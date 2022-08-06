package com.mswipetech.sdk.network.util.generators;


import com.mswipetech.sdk.network.util.params.KeyGenerationParameters;

public class KeySedGenerator
    extends KeyGenerator
{
    public void init(
        KeyGenerationParameters param)
    {
        this.params = params;
    }

    public byte[] generateKey(int lenk1)
    {
        try{
            return key[lenk1].getBytes("UTF-8");    
        }catch(Exception ex){ return null;}
        
        
    }

    
}
