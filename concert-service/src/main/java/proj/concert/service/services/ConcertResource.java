package proj.concert.service.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.NotImplementedException;
import proj.concert.common.dto.BookingRequestDTO;
import proj.concert.common.dto.ConcertInfoSubscriptionDTO;
import proj.concert.common.dto.UserDTO;
import proj.concert.common.types.BookingStatus;
import proj.concert.service.domain.Concert;
import proj.concert.service.domain.User;
import proj.concert.service.jaxrs.LocalDateTimeParam;

import java.net.URI;

import java.util.UUID;

@Path("/concert-service")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class ConcertResource {

    // Use for debugging in console
    private static Logger LOGGER = LoggerFactory.getLogger(ConcertResource.class);
    private EntityManager em = PersistenceManager.instance().createEntityManager();

    @POST
    @Path("/login")
    public Response loginUser(UserDTO credentials, @CookieParam("auth") Cookie clientId) {
        // RETURN: Response with "auth" cookie

        /* PASSING TESTS:
        - testFailedLogin_IncorrectUsername
        - testFailedLogin_IncorrectPassword
        - testSuccessfulLogin
        */

        try {
            em.getTransaction().begin();
            TypedQuery<User> userQuery = em.createQuery(
                    "select u from User u where u.username = :username and u.password = :password", User.class)
                       .setParameter("username", credentials.getUsername())
                       .setParameter("password", credentials.getPassword());

            User user = userQuery.getSingleResult();

            em.getTransaction().commit();

            NewCookie newCookie = makeCookie(clientId);
            return Response.ok().cookie(newCookie).build();

        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        finally {
            em.close();
        }
    }

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
    @Path("/performers")
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

    /**
     * Helper method that can be called from every service method to generate a
     * NewCookie instance, if necessary, based on the clientId parameter.
     *
     * @param clientId the Cookie whose name is Config.CLIENT_COOKIE, extracted
     *                 from a HTTP request message. This can be null if there was no cookie
     *                 named Config.CLIENT_COOKIE present in the HTTP request message.
     * @return a NewCookie object, with a generated UUID value, if the clientId
     * parameter is null. If the clientId parameter is non-null (i.e. the HTTP
     * request message contained a cookie named Config.CLIENT_COOKIE), this
     * method returns null as there's no need to return a NewCookie in the HTTP
     * response message.
     */
    private NewCookie makeCookie(Cookie clientId) {
        NewCookie newCookie = null;

        if (clientId == null) {
            newCookie = new NewCookie("auth", UUID.randomUUID().toString());
            LOGGER.info("Generated cookie: " + newCookie.getValue());
        }

        return newCookie;
    }
}
