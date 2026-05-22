package server_2026_b.server.service;

import server_2026_b.server.controllers.AuthController;
import server_2026_b.server.entities.TaskEntity;
import server_2026_b.server.entities.UserEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import server_2026_b.server.entities.WorkerEntity;
import server_2026_b.server.responses.UserResponse;

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

    public List<UserEntity> getUserByUsernameAndPassword(String username, String password) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM UserEntity WHERE username = :username AND password = :password", UserEntity.class)
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
                .createQuery("SELECT u.workerEntity FROM UserEntity u WHERE u.token = :token", WorkerEntity.class)
                .setParameter("token", token)
                .setMaxResults(1)
                .uniqueResult();

    }
       //login query
     public UserResponse login(String username, String password) {
        if(username.isEmpty()) { return new UserResponse(false,1232,null);}

        UserEntity newuser=sessionFactory.getCurrentSession()
                .createQuery("FROM UserEntity WHERE username = :username", UserEntity.class)
                .setParameter("username", username)
                .setMaxResults(1)
                .uniqueResult();
        if(newuser!=null) {
            String token = AuthController.generateMD5(username, password + System.currentTimeMillis());
            newuser.setToken(token);
            save(newuser);
            return new UserResponse(true, 200, newuser);
        }
        return new UserResponse(false,1232,null);
     }
    public void registerUser(String username, String password) {
        UserEntity newUserEntity = new UserEntity();
        newUserEntity.setUsername(username);

        sessionFactory.getCurrentSession().save(newUserEntity);
    }

    public List<WorkerEntity> getWorkersByTeam (int teamId) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM WorkerEntity WHERE teamEntity.id = :id", WorkerEntity.class)
                .setParameter("id", teamId)
                .list();
    }

    public UserEntity getUserByWorker (WorkerEntity workerEntity) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM UserEntity u WHERE u.workerEntity.id = :id", UserEntity.class)
                .setParameter("id", workerEntity.getId())
                .setMaxResults(1)
                .uniqueResult();

    }
    public void addTask(TaskEntity taskEntity) {
        sessionFactory.getCurrentSession().save(taskEntity);

    }


}
