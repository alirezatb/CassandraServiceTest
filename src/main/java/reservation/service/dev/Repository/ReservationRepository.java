package reservation.service.dev.Repository;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.metadata.schema.ClusteringOrder;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.core.type.UserDefinedType;
import org.mapstruct.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import reservation.service.dev.model.Reservation;
//import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createType;


import javax.annotation.PreDestroy;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.*;
import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createTable;
import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createType;
import static com.datastax.oss.driver.api.querybuilder.relation.Relation.column;

@Repository
public class ReservationRepository {

    // Reservation Schema Constants
    public static final CqlIdentifier TYPE_ADDRESS               = CqlIdentifier.fromCql("address");
    public static final CqlIdentifier TABLE_RESERVATION_BY_HOTEL_DATE =
            CqlIdentifier.fromCql("reservations_by_hotel_date");
    public static final CqlIdentifier TABLE_RESERVATION_BY_CONFI = CqlIdentifier.fromCql("reservations_by_confirmation");
    public static final CqlIdentifier TABLE_RESERVATION_BY_GUEST = CqlIdentifier.fromCql("reservations_by_guest");
    public static final CqlIdentifier TABLE_GUESTS               = CqlIdentifier.fromCql("guests");
    public static final CqlIdentifier STREET                     = CqlIdentifier.fromCql("street");
    public static final CqlIdentifier CITY                       = CqlIdentifier.fromCql("city");
    public static final CqlIdentifier STATE_PROVINCE             = CqlIdentifier.fromCql("state_or_province");
    public static final CqlIdentifier POSTAL_CODE                = CqlIdentifier.fromCql("postal_code");
    public static final CqlIdentifier COUNTRY                    = CqlIdentifier.fromCql("country");
    public static final CqlIdentifier HOTEL_ID                   = CqlIdentifier.fromCql("hotel_id");
    public static final CqlIdentifier START_DATE                 = CqlIdentifier.fromCql("start_date");
    public static final CqlIdentifier END_DATE                   = CqlIdentifier.fromCql("end_date");
    public static final CqlIdentifier ROOM_NUMBER                = CqlIdentifier.fromCql("room_number");
    public static final CqlIdentifier CONFIRM_NUMBER             = CqlIdentifier.fromCql("confirm_number");
    public static final CqlIdentifier GUEST_ID                   = CqlIdentifier.fromCql("guest_id");
    public static final CqlIdentifier GUEST_LAST_NAME            = CqlIdentifier.fromCql("guest_last_name");
    public static final CqlIdentifier FIRSTNAME                  = CqlIdentifier.fromCql("first_name");
    public static final CqlIdentifier LASTNAME                   = CqlIdentifier.fromCql("last_name");
    public static final CqlIdentifier TITLE                      = CqlIdentifier.fromCql("title");
    public static final CqlIdentifier EMAILS                     = CqlIdentifier.fromCql("emails");
    public static final CqlIdentifier PHONE_NUMBERS              = CqlIdentifier.fromCql("phone_numbers");
    public static final CqlIdentifier ADDRESSES                  = CqlIdentifier.fromCql("addresses");

    private PreparedStatement psExistsReservation;
    private PreparedStatement psFindReservation;
    private PreparedStatement psInsertReservationByHotelDate;
    private PreparedStatement psInsertReservationByConfirmation;
    private PreparedStatement psDeleteReservationByConfirmation;
    private PreparedStatement psDeleteReservationByHotelDate;
    private PreparedStatement psSearchReservation;

    private CqlIdentifier keyspaceName;
    private CqlSession cqlSession;

    public int SumSomeNumbers(int [] values){
        int sumValuesArray = 0;
        for(int number: values){
                sumValuesArray += number;
        }
        return sumValuesArray;
    }

    public ReservationRepository(){}
//    public ReservationRepository(
//            @NonNull CqlSession cqlSession,
//            @Qualifier("keyspace") @NonNull CqlIdentifier keyspaceName) {
//        this.cqlSession   = cqlSession;
//        this.keyspaceName = keyspaceName;
//        CreateReservationTables();
//        prepareStatements();
//    }
    @PreDestroy
    public void cleanup() {
        if(null != cqlSession){
            cqlSession.close();
        }
    }
    public boolean exists(String confirmationNumber){
        return cqlSession.execute(psExistsReservation.bind(confirmationNumber)).getAvailableWithoutFetching() > 0;
    }
    public Optional<Reservation> findByConfirmationNumber(@NonNull String confirmationNumber){
        ResultSet resultSet = cqlSession.execute(psFindReservation.bind(confirmationNumber));
        Row row = resultSet.one();
        if(row == null){
            return Optional.empty();
        }
        return Optional.of(mapRowToReservation(row));
    }


    public List<Reservation> findAll(){
        return  cqlSession.execute(selectFrom(keyspaceName, TABLE_RESERVATION_BY_CONFI)
                .all().build()
        ).all().stream().map(this::mapRowToReservation).collect(Collectors.toList());

    }
    public boolean delete(String confirmationNumber){
        Optional<Reservation> reservationToDelete = this.findByConfirmationNumber(confirmationNumber);
        if(reservationToDelete.isPresent()){
            Reservation reservation = reservationToDelete.get();

        }
        return false;
    }

    private Reservation mapRowToReservation(Row row) {
        Reservation.Builder reservation = Reservation.builder();
        reservation.setConfirmation_number(row.getString(CONFIRM_NUMBER));
        reservation.setEnd_date(row.getLocalDate(START_DATE));
        reservation.setEnd_date(row.getLocalDate(END_DATE));
        reservation.setRoom_number(row.getShort(ROOM_NUMBER));
        reservation.setGuest_id(row.getUuid(GUEST_ID));
        return reservation.build();
    }

    public void prepareStatements(){
        if(psExistsReservation == null){
            psExistsReservation = cqlSession.prepare(selectFrom(keyspaceName, TABLE_RESERVATION_BY_CONFI).column(CONFIRM_NUMBER)
            .where(column(CONFIRM_NUMBER).isEqualTo(bindMarker(CONFIRM_NUMBER))).build());

            psFindReservation = cqlSession.prepare(
                    selectFrom(keyspaceName, TABLE_RESERVATION_BY_CONFI).all()
                    .where(column(CONFIRM_NUMBER).isEqualTo(bindMarker(CONFIRM_NUMBER))).build());

            psSearchReservation = cqlSession.prepare(
                    selectFrom(keyspaceName, TABLE_RESERVATION_BY_HOTEL_DATE).all()
                            .where(column(HOTEL_ID).isEqualTo(bindMarker(HOTEL_ID)))
                            .where(column(START_DATE).isEqualTo(bindMarker(START_DATE)))
                            .build());
            psDeleteReservationByConfirmation = cqlSession.prepare(
                    deleteFrom(keyspaceName, TABLE_RESERVATION_BY_CONFI)
                            .where(column(CONFIRM_NUMBER).isEqualTo(bindMarker(CONFIRM_NUMBER)))
                            .build());
            psDeleteReservationByHotelDate = cqlSession.prepare(
                    deleteFrom(keyspaceName, TABLE_RESERVATION_BY_HOTEL_DATE)
                            .where(column(HOTEL_ID).isEqualTo(bindMarker(HOTEL_ID)))
                            .where(column(START_DATE).isEqualTo(bindMarker(START_DATE)))
                            .where(column(ROOM_NUMBER).isEqualTo(bindMarker(ROOM_NUMBER)))
                            .build());
            psInsertReservationByHotelDate = cqlSession.prepare(insertInto(keyspaceName, TABLE_RESERVATION_BY_HOTEL_DATE)
                    .value(HOTEL_ID, bindMarker(HOTEL_ID))
                    .value(START_DATE, bindMarker(START_DATE))
                    .value(END_DATE, bindMarker(END_DATE))
                    .value(ROOM_NUMBER, bindMarker(ROOM_NUMBER))
                    .value(CONFIRM_NUMBER, bindMarker(CONFIRM_NUMBER))
                    .value(GUEST_ID, bindMarker(GUEST_ID))
                    .build());
            psInsertReservationByConfirmation = cqlSession.prepare(insertInto(keyspaceName, TABLE_RESERVATION_BY_CONFI)
                    .value(CONFIRM_NUMBER, bindMarker(CONFIRM_NUMBER))
                    .value(HOTEL_ID, bindMarker(HOTEL_ID))
                    .value(START_DATE, bindMarker(START_DATE))
                    .value(END_DATE, bindMarker(END_DATE))
                    .value(ROOM_NUMBER, bindMarker(ROOM_NUMBER))
                    .value(GUEST_ID, bindMarker(GUEST_ID))
                    .build());
        }
    }



    public void CreateReservationTables(){
        /**
         * Create the address type
         */
        cqlSession.execute(createType(keyspaceName, TYPE_ADDRESS)
                .ifNotExists()
                .withField(STREET, DataTypes.TEXT)
                .withField(CITY, DataTypes.TEXT)
                .withField(STATE_PROVINCE, DataTypes.TEXT)
                .withField(POSTAL_CODE, DataTypes.TEXT)
                .withField(COUNTRY, DataTypes.TEXT)
                .build()
        );
        /**
         * create the tables
         */
        cqlSession.execute(createTable(keyspaceName,TABLE_RESERVATION_BY_HOTEL_DATE)
                        .ifNotExists()
                .withPartitionKey(HOTEL_ID, DataTypes.TEXT)
                .withPartitionKey(START_DATE, DataTypes.DATE)
                .withClusteringColumn(ROOM_NUMBER, DataTypes.SMALLINT)
                .withColumn(END_DATE, DataTypes.DATE)
                .withColumn(CONFIRM_NUMBER, DataTypes.TEXT)
                .withColumn(GUEST_ID, DataTypes.UUID)
                .withClusteringOrder(ROOM_NUMBER, ClusteringOrder.ASC)
                .withComment("Q7. Find Reservation By Hotel and Date")
                .build()
            );
        /**
         *
         */
        cqlSession.execute(createTable(keyspaceName, TABLE_RESERVATION_BY_CONFI)
        .ifNotExists()
        .withPartitionKey(CONFIRM_NUMBER, DataTypes.TEXT)
        .withColumn(HOTEL_ID, DataTypes.TEXT)
        .withColumn(START_DATE, DataTypes.DATE)
        .withColumn(END_DATE, DataTypes.DATE)
        .withColumn(ROOM_NUMBER, DataTypes.SMALLINT)
        .withColumn(GUEST_ID, DataTypes.UUID)
        .build());
        cqlSession.execute(createTable(keyspaceName, TABLE_RESERVATION_BY_GUEST)
                .ifNotExists()
                .withPartitionKey(GUEST_LAST_NAME, DataTypes.TEXT)
                .withClusteringColumn(HOTEL_ID, DataTypes.TEXT)
                .withColumn(START_DATE, DataTypes.DATE)
                .withColumn(END_DATE, DataTypes.DATE)
                .withColumn(ROOM_NUMBER, DataTypes.SMALLINT)
                .withColumn(CONFIRM_NUMBER, DataTypes.TEXT)
                .withColumn(GUEST_ID, DataTypes.UUID)
                .withComment("Q8. Find reservations by guest name")
                .build());

        UserDefinedType udtAddressType =
                cqlSession.getMetadata().getKeyspace(keyspaceName).get() // Retrieving KeySpaceMetadata
                        .getUserDefinedType(TYPE_ADDRESS).get();        // Looking for UDT (extending DataType)
        cqlSession.execute(createTable(keyspaceName, TABLE_GUESTS)
                .ifNotExists()
                .withPartitionKey(GUEST_ID, DataTypes.UUID)
                .withColumn(FIRSTNAME, DataTypes.TEXT)
                .withColumn(LASTNAME, DataTypes.TEXT)
                .withColumn(TITLE, DataTypes.TEXT)
                .withColumn(EMAILS, DataTypes.setOf(DataTypes.TEXT))
                .withColumn(PHONE_NUMBERS, DataTypes.listOf(DataTypes.TEXT))
                .withColumn(ADDRESSES, DataTypes.mapOf(DataTypes.TEXT, udtAddressType, true))
                .withColumn(CONFIRM_NUMBER, DataTypes.TEXT)
                .withComment("Q9. Find guest by ID")
                .build());
    }




}
