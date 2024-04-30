package proj.concert.service.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.apache.commons.lang3.NotImplementedException;
import proj.concert.common.dto.*;
import proj.concert.common.types.BookingStatus;
import proj.concert.service.domain.*;
import proj.concert.service.jaxrs.LocalDateTimeParam;
import proj.concert.service.mapper.*;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import java.util.UUID;
import java.util.stream.Collectors;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;

import javax.persistence.*;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.*;

@Path("/concert-service")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConcertResource {

    // Use for debugging in console
    private static Logger LOGGER = LoggerFactory.getLogger(ConcertResource.class);

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
                    .setLockMode(LockModeType.OPTIMISTIC)
                    .getSingleResult();

            // Create cookie token
            NewCookie cookie = null;
            if (clientCookie == null) {
                String newToken = UUID.randomUUID().toString();
                cookie = new NewCookie("auth", newToken);
                user.setToken(newToken);

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
        } finally {
            em.close();
        }
    }

    @GET
    @Path("/concerts")
    public Response getAllConcerts() {
        // RETURN: a List<ConcertDTO>
        EntityManager em = PersistenceManager.instance().createEntityManager();
        /* TESTS TO COVER:
        - testGetAllConcerts
        */
        try {
            em.getTransaction().begin();

            // get Concerts from database
            List<Concert> concerts = em.createQuery("select c from Concert c", Concert.class).getResultList();

            // need to return list of dtos not concert objects
            // converting each concert
            List<ConcertDTO> concertDTOS = new ArrayList<>();
            for (Concert c: concerts) {
                ConcertDTO dto = ConcertMapper.toDto(c);
                concertDTOS.add(dto);
            }
            em.getTransaction().commit();
            return Response.ok(concertDTOS).build(); // success
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback(); // error, don't save changes
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }

//         To add an ArrayList as a Response object's entity, you should use the following code:
//
//         GenericEntity<List<Concert>> entity = new GenericEntity<List<Concert>>(concerts) {};
//         ResponseBuilder builder = Response.ok(entity);

    }

    @GET
    @Path("/concerts/summaries")
    public Response getConcertSummaries() {
        // RETURN: a List<ConcertSummaryDTO>
        EntityManager em = PersistenceManager.instance().createEntityManager();
        /* TESTS TO COVER:
        - testGetConcertSummaries
        */
        try {
            em.getTransaction().begin();

            // get concerts
            List<Concert> concerts = em.createQuery("select c from Concert c", Concert.class).getResultList();

            // need to return list of dtos not concert objects
            // converting each concert
            List<ConcertSummaryDTO> concertSummaryDTOS = new ArrayList<>();
            for (Concert c: concerts) {
                ConcertSummaryDTO dto = ConcertSummaryMapper.toDto(c);
                concertSummaryDTOS.add(dto);
            }
            em.getTransaction().commit();
            return Response.ok(concertSummaryDTOS).build(); // success
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback(); // error, don't save changes
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }


        // To add an ArrayList as a Response object's entity, you should use the following code:
        // List<Concert> concerts = new ArrayList<Concert>();
        // GenericEntity<List<Concert>> entity = new GenericEntity<List<Concert>>(concerts) {};
        // ResponseBuilder builder = Response.ok(entity);
    }

    @GET
    @Path("/performers/{id}")
    public Response getSinglePerformer(@PathParam("id") long id) {
        // RETURN: a PerformerDTO instance
        EntityManager em = PersistenceManager.instance().createEntityManager();
        /* TESTS TO COVER:
        - testGetSinglePerformer
        - testGetNonExistentPerformer
        */
        try {
            em.getTransaction().begin();
            Performer performer = em.find(Performer.class, id);
            em.getTransaction().commit();

            if (performer != null) {
                PerformerDTO dtoPerformer = PerformerMapper.toDto(performer);
                return Response.ok(dtoPerformer).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        }
        finally {
            em.close();
        }

    }

    @GET
    @Path("/performers")
    public Response getAllPerformers() {
        // RETURN: a List<PerformerDTO>
        EntityManager em = PersistenceManager.instance().createEntityManager();
        try {
            em.getTransaction().begin();

            // get performers from database
            List<Performer> performers = em.createQuery("select p from Performer p").getResultList();

            // need to return list of dtos not performer objects
            // converting each performer
            List<PerformerDTO> performerDTOS = new ArrayList<>();
            for (Performer p: performers) {
                PerformerDTO dto = PerformerMapper.toDto(p);
                performerDTOS.add(dto);
            }
            em.getTransaction().commit();
            return Response.ok(performerDTOS).build(); // success
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback(); // error, don't save changes
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }


    @POST
    @Path("/bookings")
    public Response createBooking(BookingRequestDTO request, @CookieParam("auth") Cookie clientCookie) {
        // RETURN: Response with Location header in the form of "/bookings/{id}" if authenticated,
        //         otherwise, return Response object with status code

         /*
         TESTS TO COVER:
        - testAttemptUnauthorizedBooking
        - testMakeSuccessfulBooking
        - testAttemptBookingWrongDate
        - testAttemptBookingIncorrectConcertId
        - testAttemptDoubleBooking_SameSeats
        - testAttemptDoubleBooking_OverlappingSeats
        */

        User user = getAuthenticatedUser(clientCookie);
        // check user is authenticated
        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        EntityManager em = PersistenceManager.instance().createEntityManager();
        em.getTransaction().begin();

        try {
            // Check if the concert exists and check date exists
            Concert concert = em.find(Concert.class, request.getConcertId());
            if (concert == null || !concert.getDates().contains(request.getDate())) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            // changing request from list of seat labels into list of seats
            // querying the database
            List<Seat> seats = em.createQuery("select s from Seat s where s.label in :seatLabels and s.date = :concertDate and isBooked = false", Seat.class)
                    .setParameter("seatLabels", request.getSeatLabels())
                    .setParameter("concertDate", request.getDate())
                    // ensure there are no double bookings
                    .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                    .getResultList();

            em.getTransaction().commit();

            if (seats.size() != request.getSeatLabels().size()) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            // Create the booking
            em.getTransaction().begin();
            Booking booking = new Booking(request.getConcertId(), request.getDate(), seats);
            booking.setUser(user);
            for (Seat seat : seats) {
                seat.setIsBooked(true);
            }

            em.persist(booking);
            em.getTransaction().commit();

            URI location = new URI("/concert-service/bookings/" + booking.getId());
            return Response.created(location).build();

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback(); // if any error, don't save changes
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }


    @GET
    @Path("/bookings")
    public Response getAllBookingsForUser(@CookieParam("auth") Cookie clientCookie) {
        // RETURN: a List<BookingDTO>
        //         or if not authenticated, just the Response object with status code

        /*
                 TESTS TO COVER:
                - testGetAllBookingsForUser
                - testAttemptGetAllBookingsWhenNotAuthenticated

        */
        EntityManager em = PersistenceManager.instance().createEntityManager();

        // check user is authenticated
        User user = getAuthenticatedUser(clientCookie);
        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        try {
            em.getTransaction().begin();

            // get bookings from user from database
            List<Booking> bookings = em.createQuery("select b from Booking b where b.user = :user", Booking.class)
                    .setParameter("user", user).getResultList();

            // need to return list of dtos not booking objects
            // converting each booking
            List<BookingDTO> bookingDTOs = new ArrayList<>();
            for (Booking booking: bookings) {
                BookingDTO dto = BookingMapper.toDto(booking);
                bookingDTOs.add(dto);
            }
            em.getTransaction().commit();
            return Response.ok(bookingDTOs).build(); // success
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback(); // error, don't save changes
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
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
        EntityManager em = PersistenceManager.instance().createEntityManager();

        // getting user from cookie
        User user = getAuthenticatedUser(clientCookie);

        // checking if user is authenticated
        if (user == null) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        try {
            em.getTransaction().begin();
            Booking booking = em.find(Booking.class, id);
            em.getTransaction().commit();

            // checking booking object exists and is owned by user
            if (booking == null || !booking.getUser().equals(user)) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            // returned object needs to be dto
            BookingDTO dtoBooking = BookingMapper.toDto(booking);
            return Response.ok(dtoBooking).build(); // success

        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @GET
    @Path("/seats/{date}")
    public Response getSeats(@QueryParam("status") BookingStatus status, @PathParam("date") String dateTime) {

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
        EntityManager em = PersistenceManager.instance().createEntityManager();

        // date is given as string so convert to LocalDatetime
        LocalDateTimeParam initDateObj = new LocalDateTimeParam(dateTime);
        LocalDateTime datetime = initDateObj.getLocalDateTime();

        try {
            em.getTransaction().begin();

            List<Seat> seatList; // initiate list outside of conditional scope

            // getting seat list from database
            if (status == BookingStatus.Booked || status == BookingStatus.Unbooked) {
                boolean status_param = false;
                if (status == BookingStatus.Booked) {
                    status_param = true;
                }
                seatList = em.createQuery("select s from Seat s where s.isBooked = :isBooked and s.date = :date", Seat.class)
                        .setParameter("isBooked", status_param)
                        .setParameter("date", datetime)
                        .setLockMode(LockModeType.PESSIMISTIC_READ)
                        .getResultList();

            // when booking status is any / undefined
            } else {
                seatList = em.createQuery("select s from Seat s where s.date = :date", Seat.class)
                        .setParameter("date", datetime)
                        .setLockMode(LockModeType.PESSIMISTIC_READ)
                        .getResultList();
            }

            // mapping each seat to a DTO
            List<SeatDTO> seatDTOs = new ArrayList<>();
            for (Seat seat: seatList) {
                SeatDTO seatDTO = SeatMapper.toDto(seat);
                seatDTOs.add(seatDTO);
            }
            return Response.ok().entity(seatDTOs).build();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }

    }




/*    @POST
    @Path("/subscribe/concertInfo")
    public Response createSubscription(ConcertInfoSubscriptionDTO request) {
        // RETURN: a ConcertInfoNotificationDTO instance,
        //         otherwise, a Response object with status code

        *//* TESTS TO COVER:
        - testSubscription
        - testSubscriptionForDifferentConcert
        - testUnauthorizedSubscription
        - testBadSubscription
        - testBadSubscription_NonexistentConcert
        - testBadSubscription_NonexistentDate
        *//*

        throw new NotImplementedException();
    }*/


    /**
     * Helper method that gets a User object based on provided token.
     *
     * @param clientCookie the Cookie whose name auth, extracted from a HTTP request message.
     *                     This can be null if there was no cookie in the request message.
     * @return a User object that has the associated token. If there is no token,
     * or no user can be found, return null.
     */
    private User getAuthenticatedUser(Cookie clientCookie) {
        User user = null;
        EntityManager em = PersistenceManager.instance().createEntityManager();


        if (clientCookie != null) {
            // try to get the user the token is associated to

            try {
                em.getTransaction().begin();
                user = em.createQuery(
                                "select u from User u where u.token = :token", User.class)
                        .setParameter("token", clientCookie.getValue())
                        .getSingleResult();

                LOGGER.info("user's token: " + user.getToken());
                LOGGER.info("user's username: " + user.getUsername());

                em.getTransaction().commit();

            } catch (Exception e) {
                LOGGER.info("an error occured" + e.getClass().getCanonicalName());
            } finally {
                if (em != null && em.isOpen())
                    em.close();
            }
        }

        return user;
    }

}



