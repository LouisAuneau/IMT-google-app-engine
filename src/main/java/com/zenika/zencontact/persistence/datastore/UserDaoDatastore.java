package com.zenika.zencontact.persistence.datastore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.PropertyProjection;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.repackaged.com.google.datastore.v1.Projection;
import com.zenika.zencontact.domain.User;
import com.zenika.zencontact.persistence.UserDao;

public class UserDaoDatastore implements UserDao {

    private static UserDaoDatastore INSTANCE = new UserDaoDatastore();
    public DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();  

    public static UserDaoDatastore getInstance() {
        return INSTANCE;
    }

    @Override
    public long save(User contact) {
        Entity e = new Entity("User");
        if (contact.id != null) {
            Key k = KeyFactory.createKey("User", contact.id);
            
            try {
                e = datastore.get(k);
            } catch (EntityNotFoundException exception) {}
        }

        e.setProperty("firstName", contact.firstName);
        e.setProperty("lastName", contact.lastName);
        e.setProperty("notes", contact.notes);
        e.setProperty("email", contact.email);
        e.setProperty("birthdate", contact.birthdate);
        Key key = datastore.put(e);
        return e.getKey().getId();
    }

    @Override
    public void delete(Long id) {
        Key k = KeyFactory.createKey("User", id);
        datastore.delete(k);
    }

    @Override
    public User get(Long id) {
        Entity e;
        try {
            e = datastore.get(KeyFactory.createKey("User", id));
            return this.entityToUserConverter(e);
        } catch (EntityNotFoundException exception) {
            return null;
        }
    }

    @Override
	public List<User> getAll() {
        List<User> contacts = new ArrayList<>();
        Query q = new Query("User")
            .addProjection(new PropertyProjection("firstName", String.class))
            .addProjection(new PropertyProjection("notes", String.class))
            .addProjection(new PropertyProjection("birthdate", Date.class))
            .addProjection(new PropertyProjection("email", String.class))
            .addProjection(new PropertyProjection("lastName", String.class));
        
        PreparedQuery pq = datastore.prepare(q);

        for (Entity e : pq.asIterable()) {
            contacts.add(this.entityToUserConverter(e));
        }

        return contacts;
	}

    public User entityToUserConverter(Entity e) {
        return User.create()
            .id(e.getKey().getId())
            .firstName((String) e.getProperty("firstName"))
            .lastName((String) e.getProperty("lastName"))
            .notes((String) e.getProperty("notes"))
            .email((String) e.getProperty("email"))
            .birthdate((Date) e.getProperty("birthdate"));
    }
}