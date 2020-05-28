package reservation.service.dev.Respository;

import org.junit.Test;
import reservation.service.dev.Repository.ReservationRepository;

import static org.junit.Assert.assertEquals;


public class ReservationRepositoryTest {
    ReservationRepository rr = new ReservationRepository();
    @Test
    public void TestSumSomeNumbers(){
        int result = rr.SumSomeNumbers(new int[]{1,2,3,4,5,6,7,8,9,10});
        assertEquals(55, result);

    }

}
