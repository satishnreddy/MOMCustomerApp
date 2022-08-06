/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mswipetech.sdk.network.util.params;

import java.util.Random;

/**
 *
 * @author satish
 */
public class KeyGenerationParameters {
    
    private Random    random;
    private int       strength;
    private String    keyData="";

    /**
     * initialise the generator with a source of randomness
     * and a strength (in bits).
     *
     * @param random the random byte source.
     * @param strength the size, in bits, of the keys we want to produce.
     */
    public KeyGenerationParameters(
        Random    random,
        int             strength,String inData)
    {
        this.random = random;
        this.strength = strength;
        this.keyData = inData;
    }

    /**
     * return the random source associated with this
     * generator.
     *
     * @return the generators random source.
     */
    public Random getRandom()
    {
        return random;
    }

    /**
     * return the bit strength for keys produced by this generator,
     *
     * @return the strength of the keys this generator produces (in bits).
     */
    public int getStrength()
    {
        return strength;
    }
    
    public String getKeyData()
    {
        return keyData;
    }
}
