package org.cvit.cabig.dmr.cmef.util;

import org.cvit.cabig.dmr.cmef.UserCredentials ;

public interface Provider<T> {

    T provide(UserCredentials credentials) throws ProviderException ;
}
