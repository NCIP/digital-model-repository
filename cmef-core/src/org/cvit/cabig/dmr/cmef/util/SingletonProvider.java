package org.cvit.cabig.dmr.cmef.util;

import org.cvit.cabig.dmr.cmef.UserCredentials ;

public class SingletonProvider <T> implements Provider<T> {
    private T instance ;

    public SingletonProvider(T instance) {
	if (instance == null) {
	    throw new IllegalArgumentException("Singleton instance must not be null.") ;
	}
	this.instance = instance ;
    }
    
    @Override
    public T provide(UserCredentials credentials) throws ProviderException {
	return instance ;
    }

}
