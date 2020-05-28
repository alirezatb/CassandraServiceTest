package reservation.service.dev.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;


@ApiModel(value = "ReservationRequest", description = "Reservation request is used when the confirmation number has not provided yet.")
public class ReservationRequest implements Serializable {
    @ApiModelProperty(value = "the hotel identifier in string", example = "Meriton Hotel")
    private String hotelID;
    @ApiModelProperty(value = "the room number in short int", example = "110")
    private short roomNumber;
    @ApiModelProperty(value = "the guest identifier in UUID", example = "b9c5a9d8-9781-4de8-a00a-601a9cd6b366")
    private UUID guestID;
    @ApiModelProperty(value = "Start of the stay in YYYY-MM-DD", example = "2020-07-01")
    private LocalDate startDate;
    @ApiModelProperty(value = "end of the stay in YYYY-MM-DD", example = "2020-07-03")
    private LocalDate endDate;

    public void setHotelID(String hotelID) {
        this.hotelID = hotelID;
    }

    public void setRoomNumber(short roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void setGuestID(UUID guestID) {
        this.guestID = guestID;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getHotelID() {
        return hotelID;
    }

    public short getRoomNumber() {
        return roomNumber;
    }

    public UUID getGuestID() {
        return guestID;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}
