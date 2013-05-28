/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */


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
