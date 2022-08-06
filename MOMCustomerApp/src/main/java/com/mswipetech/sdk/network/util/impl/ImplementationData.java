
package com.mswipetech.sdk.network.util.impl;


import com.mswipetech.sdk.network.util.impl.data.MetaDataImpl;

public class ImplementationData {
    private static ImplementationData instance = new ImplementationData();
    
    /**
     * Allows third parties to replace the implementation factory
     */
    protected ImplementationData() {
    }
    
    /**
     * Returns the singleton instance of this class
     *
     * @return instanceof Implementation factory
     */
    public static ImplementationData getInstance() {
        return instance;
    }
    

    public static void setInstance(ImplementationData i) {
        instance = i;
    }
    
    /**
     * Factory method to create the implementation instance
     * 
     * @return a newly created implementation instance
     */
    public ImplData createImplementation(ImplData instance) {
        if(instance instanceof MetaDataImpl)
                {
                    return instance;
                
        }
        
        return null;
    }
}
