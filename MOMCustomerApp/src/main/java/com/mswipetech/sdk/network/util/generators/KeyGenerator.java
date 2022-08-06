package com.mswipetech.sdk.network.util.generators;


import com.mswipetech.sdk.network.util.params.KeyGenerationParameters;

import java.util.Random;

public class KeyGenerator
    extends BasicKeyGenerator
{
    /**
     * initialise the key generator - if strength is set to zero
     * the key generated will be 192 bits in size, otherwise
     * strength can be 128 or 192 (or 112 or 168 if you don't count
     * parity bits), depending on whether you wish to do 2-key or 3-key
     * triple DES.
     *
     * @param param the parameters to be used for key generation
     */
    public void init(
        KeyGenerationParameters param)
    {
        this.params = params;
    }

    public byte[] generateKey()
    {
        byte[]  newKey = new byte[params.getStrength()];
        Random random = params.getRandom();
        
        random.nextBytes(newKey);

        return newKey;
    }

    
}
