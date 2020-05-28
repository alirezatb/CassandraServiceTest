package reservation.service.dev.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.UUID;

@ApiModel(value = "ReservationConfirmation", description = "ReservationConfirmation is used when a confirmation has been generated")
public class ReservationConfirmation extends ReservationRequest{

    @ApiModelProperty(value = "Confirmation number as a UUID", example = "b9c5a9d8-9781-4de8-a00a-601a9cd6b366")
    private UUID confirmationNumber;

    public ReservationConfirmation(ReservationRequest reservationRequest){
        setEndDate(reservationRequest.getEndDate());
        setStartDate(reservationRequest.getStartDate());
        setGuestID(reservationRequest.getGuestID());
        setHotelID(reservationRequest.getHotelID());
        setRoomNumber(reservationRequest.getRoomNumber());
    }

    public void setConfirmationNumber(UUID confirmationNumber)
    {
        this.confirmationNumber = confirmationNumber;
    }

    public UUID getConfirmationNumber()
    {
        return confirmationNumber;
    }
}
