package reservation.service.dev.Repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reservation.service.dev.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@Transactional
public class UserDAOService {
    private EntityManager entityManager;

    @PersistenceContext
    public int InsertUser(User user){
        entityManager.persist(user);
        return user.getId();
    }
}
