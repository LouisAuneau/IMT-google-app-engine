package com.zenika.zencontact.resource;

import com.zenika.zencontact.domain.User;
import com.zenika.zencontact.persistence.objectify.UserDaoObjectify;
import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// With @WebServlet annotation the webapp/WEB-INF/web.xml is no longer required.
@WebServlet(name = "UserResource", value = "/api/v0/users")
public class UserResource extends HttpServlet {

  public static final MemcacheService memcache = MemcacheServiceFactory.getMemcacheService();
  public static final String CACHE_KEY = "contacts";

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json; charset=utf-8");

    // Get users from cache
    List<User> contacts = (List<User>) memcache.get(CACHE_KEY);
    if (contacts == null) {
      contacts = UserDaoObjectify.getInstance().getAll();
      memcache.put(CACHE_KEY, contacts, Expiration.byDeltaSeconds(240), MemcacheService.SetPolicy.ADD_ONLY_IF_NOT_PRESENT);
    }
   
    response.getWriter().println(new Gson().toJsonTree(contacts).getAsJsonArray());
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    User user = new Gson().fromJson(request.getReader(), User.class);
    user.id(UserDaoObjectify.getInstance().save(user));
    memcache.delete(CACHE_KEY);
    response.setContentType("application/json; charset=utf-8");
    response.setStatus(201);
    response.getWriter().println(new Gson().toJson(user.id));
  }
}
