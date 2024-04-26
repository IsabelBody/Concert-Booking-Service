package proj.concert.service.services;

import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.NotImplementedException;
import proj.concert.common.dto.*;
import proj.concert.common.types.BookingStatus;
import proj.concert.service.domain.Concert;
import proj.concert.service.domain.Seat;
import proj.concert.service.domain.User;
import proj.concert.service.jaxrs.LocalDateTimeParam;
import proj.concert.service.mapper.ConcertMapper;
import proj.concert.service.util.TheatreLayout;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Path("/concert-service")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class ConcertResource {

    // Use for debugging in console
    private static Logger LOGGER = LoggerFactory.getLogger(ConcertResource.class);

    private HashMap<LocalDateTime, ArrayList<Pair<AsyncResponse, Integer>>> subscriptions = new HashMap<>();

    @POST
    @Path("/login")
    public Response loginUser(UserDTO credentials, @CookieParam("auth") Cookie clientCookie) {
        // RETURN: Response with "auth" cookie

        /* PASSING TESTS:
        - testFailedLogin_IncorrectUsername
        - testFailedLogin_IncorrectPassword
        - testSuccessfulLogin
        */
        EntityManager em = PersistenceManager.instance().createEntityManager();

        try {
            em.getTransaction().begin();
            User user = em.createQuery(
                            "select u from User u where u.username = :username and u.password = :password", User.class)
                    .setParameter("username", credentials.getUsername())
                    .setParameter("password", credentials.getPassword())
                    .getSingleResult();

            // Create cookie token
            NewCookie cookie = null;
            if (clientCookie == null) {
                String newToken = UUID.randomUUID().toString();
                cookie = new NewCookie("auth", newToken);
                user.setToken(newToken);

                LOGGER.info("LOGGED IN " + user.getUsername() + " with token:" + user.getToken());

                // save token in database
                em.persist(user);
                em.getTransaction().commit();
            } else {
                cookie = new NewCookie("auth", user.getToken());
            }

            // 2nd option: create a new cookie every time the user logs in
            // String newToken = UUID.randomUUID().toString();
            // NewCookie cookie = new NewCookie("auth", newToken);
            // user.setToken(newToken);
            // em.persist(user);
            // em.getTransaction().commit();

            return Response.ok().cookie(cookie).build();

        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        } finally {
            if (em != null && em.isOpen())
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
        EntityManager em = PersistenceManager.instance().createEntityManager();

        try {
            em.getTransaction().begin();
            Concert concert = em.find(Concert.class, id);
            em.getTransaction().commit();

            if (concert != null) {
                ConcertDTO dtoConcert = ConcertMapper.toDto(concert);
                return Response.ok(dtoConcert).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        }
        finally {
            em.close();
        }
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
    public Response createBooking(BookingRequestDTO request, @CookieParam("auth") Cookie clientCookie) {
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

        // Once a valid booking has been made:
        notifySubscribers(request.getDate());

        throw new NotImplementedException();
    }

    @GET
    @Path("/bookings")
    public Response getAllBookingsForUser(@CookieParam("auth") Cookie clientCookie) {
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
    public Response getBookingById(@PathParam("id") long id, @CookieParam("auth") Cookie clientCookie) {
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
    public void createSubscription(@Suspended AsyncResponse response, ConcertInfoSubscriptionDTO request, @CookieParam("auth") Cookie clientCookie) {
        // RETURN: a ConcertInfoNotificationDTO instance,
        //         otherwise, a Response object with status code

        /* TESTS TO COVER:
        - testSubscription
        - testSubscriptionForDifferentConcert

        COVERED TESTS:
        - testUnauthorizedSubscription
        - testBadSubscription_NonexistentConcert
        - testBadSubscription_NonexistentDate
        */
        EntityManager em = PersistenceManager.instance().createEntityManager();
        User user = getAuthenticatedUser(clientCookie);

        if (user == null) {
            response.resume(Response.status(Response.Status.UNAUTHORIZED).build());
        } else {
            long concertId = request.getConcertId();
            LocalDateTime date = request.getDate();
            int percentageBooked = request.getPercentageBooked();

            try {
                em.getTransaction().begin();
                Concert concert = em.createQuery(
                                "select c from Concert c where c.id = :id and :date member of c.dates", Concert.class)
                        .setParameter("id", concertId)
                        .setParameter("date", date)
                        .getSingleResult();

                em.getTransaction().commit();

                if (subscriptions.containsKey(date)) {
                    subscriptions.get(date).add(new Pair<>(response, percentageBooked));
                    LOGGER.info("subscriptions" + subscriptions);

                } else {
                    ArrayList<Pair<AsyncResponse, Integer>> subscriptionsForConcert = new ArrayList<>();
                    subscriptionsForConcert.add(new Pair<>(response, percentageBooked));
                    subscriptions.put(date, subscriptionsForConcert);
                    LOGGER.info("subscriptions" + subscriptions);
                }

                response.resume(Response.status(Response.Status.OK).build());

            } catch (Exception e) {
                LOGGER.info("ERROR OCCURED: " + e.getClass().getCanonicalName());
                response.resume(Response.status(Response.Status.BAD_REQUEST).build());
            } finally {
                em.close();
            }
        }

    }

    /**
     * Helper method that notifies all subscribers for a particular concert / date.
     * @param date the date of the concert that has just been booked for.
     */
    private void notifySubscribers(LocalDateTime date) {
        EntityManager em = PersistenceManager.instance().createEntityManager();
        em.getTransaction().begin();

        List<Seat> bookedSeats = em.createQuery(
                "select s from Seat s where s.date = :date and s.isBooked = true", Seat.class)
                .setParameter("date", date)
                .getResultList();

        em.getTransaction().commit();
        em.close();

        ArrayList<Pair<AsyncResponse, Integer>> subscribers = subscriptions.get(date);

        synchronized (subscribers) {
            int totalBookedSeats = bookedSeats.size();

            for (Pair<AsyncResponse, Integer> subscriber : subscribers) {
                if ((totalBookedSeats / TheatreLayout.NUM_SEATS_IN_THEATRE) >= subscriber.getValue()) {
                    ConcertInfoNotificationDTO notification = new ConcertInfoNotificationDTO(
                            TheatreLayout.NUM_SEATS_IN_THEATRE - totalBookedSeats);

                    subscriber.getKey().resume(notification);
                }
            }
        }

    }

    /**
     * Helper method that gets a User object based on provided token.
     *
     * @param clientCookie the Cookie whose name auth, extracted from a HTTP request message.
     *                     This can be null if there was no cookie in the request message.
     *
     * @return a User object that has the associated token. If there is no token,
     *         or no user can be found, return null.
     */
    private User getAuthenticatedUser(Cookie clientCookie) {

        EntityManager em = PersistenceManager.instance().createEntityManager();
        User user = null;

        // try to get the user the token is associated to
        try {

            em.getTransaction().begin();
            user = em.createQuery(
                            "select u from User u where u.token = :token", User.class)
                    .setParameter("token", clientCookie.getValue())
                    .getSingleResult();

            LOGGER.info("user: " + user.getUsername() + " w/ token: " + user.getToken());

            em.getTransaction().commit();

        } catch (Exception e) {
            LOGGER.info("ERROR OCCURED: " + e.getClass().getCanonicalName());
        } finally {
            em.close();
        }

        return user;
    }
}
