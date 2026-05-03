package server_2026_b.server.service;

import server_2026_b.server.entities.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import server_2026_b.server.entities.WorkerEntity;
import server_2026_b.server.responses.UserResponse;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Transactional
@Component
@SuppressWarnings("unchecked")
public class Persist {

    private final SessionFactory sessionFactory;

    public Persist(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public <T> void saveAll(List<T> objects) {
        for (T object : objects) {
            sessionFactory.getCurrentSession().saveOrUpdate(object);
        }
    }

    public void remove(Object o) {
        sessionFactory.getCurrentSession().remove(o);
    }

    public Session getQuerySession() {
        return sessionFactory.getCurrentSession();
    }

    public void save(Object object) {
        sessionFactory.getCurrentSession().saveOrUpdate(object);
    }

    public <T> T loadObject(Class<T> clazz, int oid) {
        return getQuerySession().get(clazz, oid);
    }

    public <T> List<T> loadList(Class<T> clazz) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM " + clazz.getSimpleName()).list();
    }

    public List<User> getUserByUsernameAndPassword(String username, String password) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM User WHERE username = :username AND password = :password", User.class)
                .setParameter("username", username)
                .setParameter("password", password)
                .list();
    }

    public List<WorkerEntity> getWorkersByManager (int managerId) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM WorkerEntity WHERE managerEntity.id = :id", WorkerEntity.class)
                .setParameter("id", managerId)
                .list();
    }


    public WorkerEntity getWorkerByToken (String token) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM WorkerEntity WHERE token = :token", WorkerEntity.class)
                .setParameter("token", token)
                .setMaxResults(1)
                .uniqueResult();

    }
       //login query
     public UserResponse login(String username, String password) {
        if(username.isEmpty() || password.isEmpty()) { return new UserResponse(false,1232,null);}

        User newuser=sessionFactory.getCurrentSession()
                .createQuery("FROM User WHERE username = :username and password= :password", User.class)
                .setParameter("username", username)
                .setParameter("password", password)
                .setMaxResults(1)
                .uniqueResult();
        newuser.setPassword("");
        if(newuser!=null) {
            return new UserResponse(true, 200, newuser);
        }
        return new UserResponse(false,1232,null);
     }
    public void registerUser(String username, String password) {
        User newUser = new User();
        newUser.setUsername(username);

        sessionFactory.getCurrentSession().save(newUser);
    }



}
