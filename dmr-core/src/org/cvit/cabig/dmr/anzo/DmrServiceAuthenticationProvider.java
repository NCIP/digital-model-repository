/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */



package org.cvit.cabig.dmr.anzo ;

import java.sql.SQLException;
import java.util.List;
import java.util.Properties ;
import java.util.concurrent.CopyOnWriteArraySet ;

import javax.management.MBeanServer ;
import javax.management.ObjectName ;

import org.openanzo.common.exceptions.AnzoException ;
import org.openanzo.common.exceptions.ExceptionConstants;
import org.openanzo.common.exceptions.NotificationException;
import org.openanzo.common.exceptions.ExceptionConstants.SERVER.CODES;
import org.openanzo.model.Constants;
import org.openanzo.serialization.SerializationConstants;
import org.openanzo.server.repository.rdb.RepositoryConnection ;
import org.openanzo.server.repository.rdb.sql.UserRdbWrapper;
import org.openanzo.server.security.IAuthenticationProvider ;
import org.openanzo.server.security.IUserUpdateListener ;
import org.openanzo.services.INotificationListener;
import org.openanzo.services.NotificationListenerAdapter;
import org.openanzo.services.impl.NotificationListenerConnection ;
import org.openrdf.model.URI ;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Authentication provider which will authenticate any passed in userId which has
 * a URI within Anzo, since user must have already been authenticated using their 
 * grid credentials.
 * 
 * Based on com.cambridgesemantics.cvit.security.AuthenticationProvider
 * 
 * @author rbradley
 */
public class DmrServiceAuthenticationProvider extends NotificationListenerConnection implements IAuthenticationProvider {
    private Logger logger = LoggerFactory.getLogger(this.getClass()) ;
    private CopyOnWriteArraySet<IUserUpdateListener> listeners = new CopyOnWriteArraySet<IUserUpdateListener>() ;

    public DmrServiceAuthenticationProvider(Properties properties) {
	super(properties) ;
	ns.registerNotificationListener(new NotificationAdapter()) ;
    }

    public URI authenticateUser(RepositoryConnection conn, String userId, String password) throws AnzoException {
	if (userId != null) {
	    Long user ;
	    try {
		user = UserRdbWrapper.getUserId(conn.getPreparedStatementCache(), userId) ;
	    } catch (SQLException e) {
		throw AnzoException.createException(CODES.AUTHENTICATION_ERROR,
		    ExceptionConstants.SERVER.SUBCODES.USER_ERROR, userId) ;
	    }
	    URI userUri = conn.getNodeLayout().getNodeURILayout().fetchValue(user) ;
	    if (userUri != null) {
		return userUri ;
	    }
	}
	throw AnzoException.createException(CODES.AUTHENTICATION_ERROR,
	    ExceptionConstants.SERVER.SUBCODES.USER_ERROR, userId) ;
    }

    public void registerIUserUpdateListener(IUserUpdateListener l) {
	if (l != null) {
	    listeners.add(l) ;
	}
    }

    public void unregisterIUserUpdateListener(IUserUpdateListener l) {
	if (l != null) {
	    listeners.remove(l) ;
	}
    }

    public void close() throws AnzoException {
	super.close() ;
    }

    public boolean getIsStarted() throws AnzoException {
	return super.isConnected() ;
    }

    public void registerMBean(MBeanServer mbs, ObjectName parentName) {
	try {
	    ObjectName name = new ObjectName(
		parentName.getDomain() + ":" + 
		parentName.getKeyPropertyListString() + 
		",Type=AuthenticationProvider") ;
	    if (!mbs.isRegistered(name)) {
		mbs.registerMBean(this, name) ;
	    }
	} catch (Exception e) {
	    logger.error("Exception while registering management bean.", e) ;
	}
    }

    public void start() throws AnzoException {
	super.connect() ;
    }

    public void stop() throws AnzoException {
	super.close() ;
    }

    private class NotificationAdapter extends NotificationListenerAdapter {

	public void connectionStateChanged(int state) {
	    if (state == INotificationListener.CONNECTED) {
		try {
		    String selector = "(" + SerializationConstants.metaUri + " = '" + Constants.defaultSystemMetaGraph
				      + "' AND " + SerializationConstants.type + " = '"
				      + SerializationConstants.userType + "') OR (" + SerializationConstants.type
				      + " = '" + SerializationConstants.reset + "')" ;
		    ns.registerSelector(selector) ;
		} catch (NotificationException nse) {}
	    }
	}

	public boolean handleUser(boolean addition, 
	                          URI user, 
	                          URI defaultRole, 
	                          URI defaultAclTemplate, 
	                          String userId,
				  String password) throws AnzoException {
	    notifyListeners(java.util.Arrays.asList(new String[] {user.toString(), userId})) ;
	    return true ;
	}

	public boolean handleReset() throws AnzoException {
	    notifyListenersReset() ;
	    return true ;
	}
    }
    
    private void notifyListeners(List<String> users) {
	for (IUserUpdateListener listener : listeners) {
	    for (String user : users) {
		listener.userUpdated(user) ;
	    }
	}
    }

    private void notifyListenersReset() {
	for (IUserUpdateListener listener : listeners) {
	    listener.reset() ;
	}
    }
}
