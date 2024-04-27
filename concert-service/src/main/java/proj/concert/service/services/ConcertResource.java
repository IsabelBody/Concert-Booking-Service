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
import proj.concert.service.domain.Booking;
import proj.concert.service.domain.Concert;
import proj.concert.service.domain.Seat;
import proj.concert.service.domain.User;
import proj.concert.service.jaxrs.LocalDateTimeParam;
import proj.concert.service.mapper.BookingMapper;
import proj.concert.service.mapper.ConcertMapper;
import proj.concert.service.mapper.SeatMapper;

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

            // Check if the concert exists
            Concert concert = em.find(Concert.class, request.getConcertId());
            if (concert == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            // check date exists
            if (!concert.getDates().contains(request.getDate())) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }


            // TODO: make use the getSeat functionality?

            // changing request from list of seat labels into list of seats
            // querying the database
            List<Seat> requestedSeats = em.createQuery("select s from Seat s where s.label in :seatLabels and s.date = :concertDate", Seat.class)
                    .setParameter("seatLabels", request.getSeatLabels())
                    .setParameter("concertDate", request.getDate())
                    .getResultList();

            // Check if the requested seats are available
            for (Seat seat : requestedSeats) {
                // if any seat is already booked, user is not allowed to book.
                if (seat.getIsBooked()) {
                    return Response.status(Response.Status.FORBIDDEN).build();
                } else {
                    seat.setIsBooked(true);
                    em.merge(seat);
                }
            }

            // Create the booking
            Booking booking = new Booking(request.getConcertId(), request.getDate(), requestedSeats);
            booking.setUser(user);


            em.persist(booking);
            em.getTransaction().commit();


            URI location = new URI("http://localhost:10000/services/concert-service/bookings/" + booking.getId());
            return Response.created(location).build();

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
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
        EntityManager em = PersistenceManager.instance().createEntityManager();

        User user = getAuthenticatedUser(clientCookie);

        // Checking if user is authenticated
        if (user == null) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        try {
            em.getTransaction().begin();
            Booking booking = em.find(Booking.class, id);
            em.getTransaction().commit();

            if (booking != null) {
                if (!booking.getUser().equals(user)) {
                    // The booking is not owned by the authenticated user
                    return Response.status(Response.Status.FORBIDDEN).build();
                }
                BookingDTO dtoBooking = BookingMapper.toDto(booking);
                return Response.ok(dtoBooking).build(); // success
            } else {
                // booking is not found
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        }
        finally {
            em.close();
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

        // Converting string to LocalDatetime from pathparam
        LocalDateTimeParam ldtp = new LocalDateTimeParam(dateTime);
        LocalDateTime datetime = ldtp.getLocalDateTime();

        EntityManager em = PersistenceManager.instance().createEntityManager();


        em = PersistenceManager.instance().createEntityManager();
        try {
            em.getTransaction().begin();
            // Status is booked
            if (status == BookingStatus.Booked) {
                TypedQuery<Seat> seatsQuery = em.createQuery("select s from Seat s where s.isBooked = :isBooked and s.date = :date", Seat.class)
                        .setParameter("isBooked", true)
                        .setParameter("date", datetime);
                List<Seat> seatListBooked = seatsQuery.getResultList();

                List<SeatDTO> seatDTOList = new ArrayList<>();

                for (Seat s : seatListBooked) {
                    SeatDTO seatDTO = SeatMapper.toDto(s);
                    seatDTOList.add(seatDTO);
                }
                return Response.ok().entity(seatDTOList).build();
            } else if (status == BookingStatus.Unbooked) {
                TypedQuery<Seat> seatsQuery = em.createQuery("select s from Seat s where s.isBooked = :isBooked and s.date = :date", Seat.class)
                        .setParameter("isBooked", false)
                        .setParameter("date", datetime);
                List<Seat> seatListUnbooked = seatsQuery.getResultList();

                List<SeatDTO> seatDTOList = new ArrayList<>();

                for (Seat s : seatListUnbooked) {
                    SeatDTO seatDTO = SeatMapper.toDto(s);
                    seatDTOList.add(seatDTO);
                }
                return Response.ok().entity(seatDTOList).build();
            } else {
                TypedQuery<Seat> seatsQuery = em.createQuery("select s from Seat s where s.date = :date", Seat.class)
                        .setParameter("date", datetime);
                List<Seat> seatListAny = seatsQuery.getResultList();

                List<SeatDTO> seatDTOList = new ArrayList<>();

                for (Seat s : seatListAny) {
                    SeatDTO seatDTO = SeatMapper.toDto(s);
                    seatDTOList.add(seatDTO);
                }
                return Response.ok().entity(seatDTOList).build();
            }
        }
        finally{
            em.close();
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
     *
     * @return a User object that has the associated token. If there is no token,
     *         or no user can be found, return null.
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



