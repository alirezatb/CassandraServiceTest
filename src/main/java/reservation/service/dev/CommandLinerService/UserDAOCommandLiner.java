package reservation.service.dev.CommandLinerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reservation.service.dev.Repository.UserDAOService;
import reservation.service.dev.model.User;

@Component
public class UserDAOCommandLiner  implements CommandLineRunner {
    @Autowired
    private UserDAOService userDAOService;
    @Override
    public void run(String... args) throws Exception {
        User user = new User("Alireza", "Tabebordbar", "Data Engineer");
        long gerUserID = userDAOService.InsertUser(user);
        System.out.println(gerUserID);
    }
}
