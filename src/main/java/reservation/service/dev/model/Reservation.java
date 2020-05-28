package reservation.service.dev.model;


import org.apache.commons.lang3.ObjectUtils;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

public class Reservation implements Serializable {

    private String hotel_id;
    private LocalDate start_date;
    private LocalDate end_date;
    private short room_number;
    private String confirmation_number;
    private UUID guest_id;

    private Reservation(){}

    private Reservation(Builder builder){
        this.hotel_id = builder.hotel_id;
        this.end_date = builder.end_date;
        this.start_date = builder.start_date;
        this.room_number = builder.room_number;
        this.guest_id = builder.guest_id;
        this.confirmation_number = builder.confirmation_number;

    }

    public static Builder builder(){return new Builder();}
    public static Builder builder(ReservationRequest reservationRequest, String confirmation_number)
    {return new Builder(reservationRequest, confirmation_number);}

    public String getHotel_id() {
        return hotel_id;
    }

    public LocalDate getStart_date() {
        return start_date;
    }

    public LocalDate getEnd_date() {
        return end_date;
    }

    public short getRoom_number() {
        return room_number;
    }

    public String getConfirmation_number() {
        return confirmation_number;
    }

    public UUID getGuest_id() {
        return guest_id;
    }

    public static final class Builder{
        private String hotel_id;
        private LocalDate start_date;
        private LocalDate end_date;
        private short room_number;
        private String confirmation_number;
        private UUID guest_id;

        private Builder(){}
        private Builder(ReservationRequest reservationRequest, String confirmation_number){
            this.hotel_id = reservationRequest.getHotelID();
            this.start_date = reservationRequest.getStartDate();
            this.end_date = reservationRequest.getEndDate();
            this.room_number = reservationRequest.getRoomNumber();
            this.guest_id = reservationRequest.getGuestID();
            this.confirmation_number = confirmation_number;
        }

        @Override
        public String toString() {
            return "Builder{" +
                    "hotel_id='" + hotel_id + '\'' +
                    ", start_date=" + start_date +
                    ", end_date=" + end_date +
                    ", room_number=" + room_number +
                    ", confirmation_number='" + confirmation_number + '\'' +
                    ", guest_id=" + guest_id +
                    '}';
        }

        public Builder setHotel_id(String hotel_id) {
            this.hotel_id = hotel_id;
            return this;
        }

        public Builder setStart_date(LocalDate start_date) {
            this.start_date = start_date;
            return this;
        }

        public Builder setEnd_date(LocalDate end_date) {
            this.end_date = end_date;
            return this;
        }

        public Builder setRoom_number(short room_number) {
            this.room_number = room_number;
            return this;
        }

        public Builder setConfirmation_number(String confirmation_number) {
            this.confirmation_number = confirmation_number;
            return this;
        }

        public Builder setGuest_id(UUID guest_id) {
            this.guest_id = guest_id;
            return this;
        }
        public Reservation build(){return new Reservation(this);}



    }
}
