package proj.concert.service.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import proj.concert.common.dto.*;
import proj.concert.common.types.BookingStatus;
import proj.concert.service.domain.*;
import proj.concert.service.jaxrs.LocalDateTimeParam;
import proj.concert.service.mapper.*;
import proj.concert.service.util.ConcertInfoSubscription;
import proj.concert.service.util.TheatreLayout;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.persistence.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;

@Path("/concert-service")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConcertResource {

    // Use for debugging in console
    private static Logger LOGGER = LoggerFactory.getLogger(ConcertResource.class);

    /* Dictionary storing all subscriptions, where key is the date of the concert
    and the value is the list of subscribers that have subscribed to that particular date.
    Each subscriber is represented by a ConcertInfoSubscription object.
    */
    private static HashMap<LocalDateTime, ArrayList<ConcertInfoSubscription>> subscriptions = new HashMap<>();

    @POST
    @Path("/login")
    public Response loginUser(UserDTO credentials, @CookieParam("auth") Cookie clientCookie) {

        EntityManager em = PersistenceManager.instance().createEntityManager();

        try {
            em.getTransaction().begin();

            // Check whether the user exists based on provided credentials
            User user = em.createQuery(
                            "select u from User u where u.username = :username and u.password = :password", User.class)
                    .setParameter("username", credentials.getUsername())
                    .setParameter("password", credentials.getPassword())
                    // check whether any other transaction has modified the user details since this transaction started
                    .setLockMode(LockModeType.OPTIMISTIC)
                    .getSingleResult();

            em.getTransaction().commit();

            // Create a cookie every time the user logs in
            String newToken = UUID.randomUUID().toString();
            NewCookie cookie = new NewCookie("auth", newToken);
            user.setToken(newToken);

            // Save the token info in database for getting authenticated user from response
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();

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
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @GET
    @Path("/concerts")
    public Response getAllConcerts() {

        EntityManager em = PersistenceManager.instance().createEntityManager();

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
            GenericEntity<List<ConcertDTO>> entity = new GenericEntity<List<ConcertDTO>>(concertDTOS) {};
            Response.ResponseBuilder builder = Response.ok(entity);
            return builder.build();

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
//         GenericEntity<List<ConcertDTO>> concertDTOS = new GenericEntity<List<Concert>>(concerts) {};
//         ResponseBuilder builder = Response.ok(entity);

    }

    @GET
    @Path("/concerts/summaries")
    public Response getConcertSummaries() {

        EntityManager em = PersistenceManager.instance().createEntityManager();

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

            GenericEntity<List<ConcertSummaryDTO>> entity = new GenericEntity<List<ConcertSummaryDTO>>(concertSummaryDTOS) {};
            Response.ResponseBuilder builder = Response.ok(entity);
            return builder.build();
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

        EntityManager em = PersistenceManager.instance().createEntityManager();

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
            if (em != null && em.isOpen()) {
                em.close();
            }
        }

    }

    @GET
    @Path("/performers")
    public Response getAllPerformers() {

        EntityManager em = PersistenceManager.instance().createEntityManager();

        try {
            em.getTransaction().begin();

            // get performers from database
            List<Performer> performers = em.createQuery("select p from Performer p", Performer.class).getResultList();

            // need to return list of dtos not performer objects
            // converting each performer
            List<PerformerDTO> performerDTOS = new ArrayList<>();
            for (Performer p: performers) {
                PerformerDTO dto = PerformerMapper.toDto(p);
                performerDTOS.add(dto);
            }
            em.getTransaction().commit();

            GenericEntity<List<PerformerDTO>> entity = new GenericEntity<List<PerformerDTO>>(performerDTOS) {};
            Response.ResponseBuilder builder = Response.ok(entity);
            return builder.build();

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

        User user = getAuthenticatedUser(clientCookie);

        // check user is authenticated
        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        EntityManager em = PersistenceManager.instance().createEntityManager();

        try {
            em.getTransaction().begin();
            /*
            Checking if the concert exists and check date exists.
            Query to get concert and seats at the same time. This is an optimisation to avoid
            iterating over every date in each concert.
             */
            TypedQuery<Concert> query = em.createQuery(
                            "SELECT c FROM Concert c LEFT JOIN FETCH c.dates WHERE c.id = :concertId", Concert.class)
                    .setParameter("concertId", request.getConcertId());

            // Execute query, check if the concert exists
            Concert concert;
            try {
                concert = query.getSingleResult();
            } catch (NoResultException e) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            // Check if the concert date exists
            // dates should be already fetched for this operation
            if (!concert.getDates().contains(request.getDate())) {
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


            /*
            Using .size() allows us to check the request without iterating over to
            check parameters, increasing the efficiency of our code.
             */
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

            // notify subscribers to check whether the threshold of seats booked is met
            notifySubscribers(request.getDate());

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
            // returning a GenericType list, to fully align with testing.
            return Response.ok(new GenericType<List<BookingDTO>>() {}).entity(bookingDTOs).build(); // success
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

                em.getTransaction().commit();

                // when booking status is any / undefined
            } else {
                seatList = em.createQuery("select s from Seat s where s.date = :date", Seat.class)
                        .setParameter("date", datetime)
                        .setLockMode(LockModeType.PESSIMISTIC_READ)
                        .getResultList();

                em.getTransaction().commit();
            }

            // mapping each seat to a DTO
            List<SeatDTO> seatDTOs = new ArrayList<>();
            for (Seat seat: seatList) {
                SeatDTO seatDTO = SeatMapper.toDto(seat);
                seatDTOs.add(seatDTO);
            }
            // Return GenericType list to fully align with testing.
            return Response.ok(new GenericType<List<BookingDTO>>() {}).entity(seatDTOs).build(); // success
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }

    }


    @POST
    @Path("/subscribe/concertInfo")
    public void createSubscription(@Suspended AsyncResponse response, ConcertInfoSubscriptionDTO request, @CookieParam("auth") Cookie clientCookie) {

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

                // Check whether the subscription request is valid
                Concert concert = em.createQuery(
                                "select c from Concert c where c.id = :id and :date member of c.dates", Concert.class)
                        .setParameter("id", concertId)
                        .setParameter("date", date)
                        .getSingleResult();

                em.getTransaction().commit();

                // Add subscription request to dictionary of current subscriptions
                if (subscriptions.containsKey(date)) {
                    // Add subscription to an already existing list of subscribers for that date
                    subscriptions.get(date).add(new ConcertInfoSubscription(response, percentageBooked));
                } else {
                    // Create a new list of subscribers for that date if there isn't one already
                    ArrayList<ConcertInfoSubscription> subscriptionsForConcert = new ArrayList<>();
                    subscriptionsForConcert.add(new ConcertInfoSubscription(response, percentageBooked));
                    subscriptions.put(date, subscriptionsForConcert);
                }

            } catch (Exception e) {
                response.resume(Response.status(Response.Status.BAD_REQUEST).build());
            } finally {
                if (em != null && em.isOpen()) {
                    em.close();
                }
            }
        }

    }


    /**
     * Helper method that notifies all subscribers for a particular concert / date.
     * @param date the date of the concert that has just been booked for.
     */
    private void notifySubscribers(LocalDateTime date) {
        EntityManager em = PersistenceManager.instance().createEntityManager();

        // Get list of subscribers for that date
        ArrayList<ConcertInfoSubscription> subscribers = subscriptions.get(date);

        if (subscribers != null) {
            try {
                em.getTransaction().begin();

                // Get all seats that are currently available
                List<Seat> unbookedSeats = em.createQuery(
                                "select s from Seat s where s.date = :date and s.isBooked = false", Seat.class)
                        .setParameter("date", date)
                        .setLockMode(LockModeType.PESSIMISTIC_READ)
                        .getResultList();

                em.getTransaction().commit();

                int totalUnbookedSeats = unbookedSeats.size();
                int totalBookedSeats = TheatreLayout.NUM_SEATS_IN_THEATRE - totalUnbookedSeats;
                double proportionBooked = (double) totalBookedSeats / TheatreLayout.NUM_SEATS_IN_THEATRE;

                synchronized (subscribers) {
                    // For each subscriber, check whether their specific threshold is met
                    for (ConcertInfoSubscription subscriber : subscribers) {
                        double threshold = (double) subscriber.getThreshold() / 100;
                        if (proportionBooked >= threshold) {
                            // Create a notification with the number of seats remaining for the concert
                            ConcertInfoNotificationDTO notification = new ConcertInfoNotificationDTO(totalUnbookedSeats);

                            subscriber.getResponse().resume(notification);
                        }
                    }
                }

            } catch (Exception e) {
                LOGGER.info("ERROR OCCURED in Notifying Subscribers" + e.getClass().getCanonicalName());
            } finally {
                if (em != null && em.isOpen()) {
                    em.close();
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

        try {
            em.getTransaction().begin();

            user = em.createQuery(
                            "select u from User u where u.token = :token", User.class)
                    .setParameter("token", clientCookie.getValue())
                    .getSingleResult();

            em.getTransaction().commit();

        } catch (Exception e) {
            LOGGER.info("ERROR OCCURED in Authenticating User: " + e.getClass().getCanonicalName());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }

        return user;
    }

}


