package project.package17;

import java.io.Serializable;
import java.util.Iterator;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

public class MyInterceptor extends EmptyInterceptor {

	public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		System.out.println("onDelete");
	}

	// This method is called when Employee object gets updated.
	public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types) {
		if (entity instanceof Employee) {
			System.out.println("onFlushDirty : Update Operation");
			return true;
		}
		return false;
	}

	public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		System.out.println("onLoad");
		return true;
	}

	// This method is called when Employee object gets created.
	public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		if (entity instanceof Employee) {
			System.out.println("onSave : Create Operation");
			return true;
		}
		return false;
	}

	// This method is called before commit into database.
	public void preFlush(Iterator iterator) {
		System.out.println("preFlush");
	}

	// This method is called after committed into database.
	public void postFlush(Iterator iterator) {
		System.out.println("postFlush");
	}

}
