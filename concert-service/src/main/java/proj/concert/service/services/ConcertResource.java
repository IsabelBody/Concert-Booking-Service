package proj.concert.service.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.NotImplementedException;
import proj.concert.common.dto.BookingRequestDTO;
import proj.concert.common.dto.ConcertInfoSubscriptionDTO;
import proj.concert.common.dto.UserDTO;
import proj.concert.common.types.BookingStatus;
import proj.concert.service.jaxrs.LocalDateTimeParam;

@Path("concert-service")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConcertResource {

    // Use for debugging in console
    private static Logger LOGGER = LoggerFactory.getLogger(ConcertResource.class);
    private EntityManager em = PersistenceManager.instance().createEntityManager();

    @GET
    @Path("/concerts/{id}")
    public Response getSingleConcert(@PathParam("id") long id) {
        // RETURN: a ConcertDTO instance

        /* TESTS TO COVER:
        - testGetSingleConcert
        - testGetSingleConcertWithMultiplePerformersAndDates
        - testGetNonExistentConcert
        */

        throw new NotImplementedException();
    }

    @GET
    @Path("/concerts")
    public Response getAllConcerts() {
        // RETURN: a List<ConcertDTO>

        /* TESTS TO COVER:
        - testGetAllConcerts
        */

        // To add an ArrayList as a Response object's entity, you should use the following code:
        // List<Concert> concerts = new ArrayList<Concert>();
        // GenericEntity<List<Concert>> entity = new GenericEntity<List<Concert>>(concerts) {};
        // ResponseBuilder builder = Response.ok(entity);

        throw new NotImplementedException();
    }

    @GET
    @Path("/concerts/summaries")
    public Response getConcertSummaries() {
        // RETURN: a List<ConcertSummaryDTO>

        /* TESTS TO COVER:
        - testGetConcertSummaries
        */

        // To add an ArrayList as a Response object's entity, you should use the following code:
        // List<Concert> concerts = new ArrayList<Concert>();
        // GenericEntity<List<Concert>> entity = new GenericEntity<List<Concert>>(concerts) {};
        // ResponseBuilder builder = Response.ok(entity);

        throw new NotImplementedException();
    }

    @GET
    @Path("/performers/{id}")
    public Response getSinglePerformer(@PathParam("id") long id) {
        // RETURN: a PerformerDTO instance

        /* TESTS TO COVER:
        - testGetSinglePerformer
        - testGetNonExistentPerformer
        */

        throw new NotImplementedException();
    }

    @GET
    @Path("/concerts")
    public Response getAllPerformers() {
        // RETURN: a List<PerformerDTO>

        /* TESTS TO COVER:
        - testGetAllPerformers
        */

        // To add an ArrayList as a Response object's entity, you should use the following code:
        // List<Concert> concerts = new ArrayList<Concert>();
        // GenericEntity<List<Concert>> entity = new GenericEntity<List<Concert>>(concerts) {};
        // ResponseBuilder builder = Response.ok(entity);

        throw new NotImplementedException();
    }

    @POST
    @Path("/login")
    public Response loginUser(UserDTO credentials) {
        // RETURN: Response with "auth" cookie

        /* TESTS TO COVER:
        - testFailedLogin_IncorrectUsername
        - testFailedLogin_IncorrectPassword
        - testSuccessfulLogin
        */

        throw new NotImplementedException();
    }

    @POST
    @Path("/bookings")
    public Response createBooking(BookingRequestDTO request) {
        // RETURN: Response with Location header in the form of "/bookings/{id}" if authenticated,
        //         otherwise, return Response object with status code

        /* TESTS TO COVER:
        - testAttemptUnauthorizedBooking
        - testMakeSuccessfulBooking
        - testAttemptBookingWrongDate
        - testAttemptBookingIncorrectConcertId
        - testAttemptDoubleBooking_SameSeats
        - testAttemptDoubleBooking_OverlappingSeats
        */

        throw new NotImplementedException();
    }

    @GET
    @Path("/bookings")
    public Response getAllBookingsForUser() {
        // RETURN: a List<BookingDTO>
        //         or if not authenticated, just the Response object with status code

        /* TESTS TO COVER:
        - testGetAllBookingsForUser
        - testAttemptGetAllBookingsWhenNotAuthenticated
        */

        // To add an ArrayList as a Response object's entity, you should use the following code:
        // List<Concert> concerts = new ArrayList<Concert>();
        // GenericEntity<List<Concert>> entity = new GenericEntity<List<Concert>>(concerts) {};
        // ResponseBuilder builder = Response.ok(entity);

        throw new NotImplementedException();
    }

    @GET
    @Path("/bookings/{id}")
    public Response getBookingById(@PathParam("id") long id) {
        // RETURN: a BookingDTO instance if the booking is owned by the user,
        //         otherwise, just a Response object with status code

        /* TESTS TO COVER:
        - testGetOwnBookingById
        - testAttemptGetOthersBookingById
        */

        throw new NotImplementedException();
    }

    @GET
    @Path("/seats/{date}")
    public Response getSeats(@PathParam("date") LocalDateTimeParam date,
                             @QueryParam("status") BookingStatus status) {

        // RETURN: a List<SeatDTO>

        /* TESTS TO COVER:
        - testGetBookedSeatsForDate
        - testGetUnbookedSeatsForDate
        - testGetAllSeatsForDate

        ALSO USED IN:
        - testAttemptUnauthorizedBooking
        - testMakeSuccessfulBooking
        - testAttemptDoubleBooking_OverlappingSeats
        */

        // To use the 'date' param, do:
        // LocalDateTime date = date.getLocalDateTime();

        // To add an ArrayList as a Response object's entity, you should use the following code:
        // List<Concert> concerts = new ArrayList<Concert>();
        // GenericEntity<List<Concert>> entity = new GenericEntity<List<Concert>>(concerts) {};
        // ResponseBuilder builder = Response.ok(entity);

        throw new NotImplementedException();
    }

    @POST
    @Path("/subscribe/concertInfo")
    public Response createSubscription(ConcertInfoSubscriptionDTO request) {
        // RETURN: a ConcertInfoNotificationDTO instance,
        //         otherwise, a Response object with status code

        /* TESTS TO COVER:
        - testSubscription
        - testSubscriptionForDifferentConcert
        - testUnauthorizedSubscription
        - testBadSubscription
        - testBadSubscription_NonexistentConcert
        - testBadSubscription_NonexistentDate
        */

        throw new NotImplementedException();
    }
}
