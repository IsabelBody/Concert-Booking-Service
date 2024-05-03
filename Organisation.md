# Team Members:
- **Isabel Body** / `ibod875` / IsabelBody
- **Sophia Halapchuk** / `shal847` / micattoc
- **Jason Peng** / `jpen687` / jaspeng6


# Summary of each team member’s contributions
**Isabel Body:** booking & seat domain model, booking & seat HTTP methods
<br><br>**Sophia Halapchuk:** Jackson annotations, Mapper classes, User domain class, Login and Subscription/Notification HTTP methods, Transaction locking for concurrency
<br><br>**Jason Peng:**


# Collaboration practices 
- ERD, planning and design decisions created collaboratively to develop shared project understanding, but implementation done individually 
- weekly meetings were held with code discussion, help & task allocation  
- all code was reviewed through pull requests
- Github Issues & discord were used for ongoing idea discussions & updates 

Please reference open issues to understand the full scope of our planning & collaboration, and closed issues for our independent task allocation.

# Explanation of Domain Design Choices 
*Short description of how the domain model is organised (2-3 sentences)*
<br> *(Jason + Isabel add onto this)*
- Fields & data types:
- Unique identifiers & class relationships:

### Use of Lazy Loading, Eager Fetching, Cascading
*(Jason explains how he implemented this in his classes):*
<br><br>*(Isabel explains how she implemented this in her classes):*

# Booking & Seat Domain Design Choices, Use of Lazy Loading, Eager Fetching, Cascading
Booking has a many-to-one bidirectional relationship with user & a one-to-many bidirectional relationship with seats. The seat collection has cascade style persist so that changes in booking lead to changes in seats. As seats & booking are always used in conjunct, seats are eager loaded. However, a booking object does not always obtain user, so user is lazy loaded until needed. 
When a booking is created in concertResource, it dynamic eager fetches concert dates to minimise queries - optimising performance which is necessary for our concurrency implementation. 



# Strategy used to minimise the chance of concurrency errors
- Pessimistic locking when creating bookings and loading seats was used because the locks with exclusive access prevents other concurrent transactions from accessing the same resource concurrently, particularly when conflicts are frequent. The conflict of double-booking the same seats is highly expected when lots of users use the service.
  
- Optimistic locking for logging in users was used because there is a low chance of conflicts (user's credentials won't be changed that frequently) and this approach enables concurrent access to a shared resource (the Users table), unlike Pessimistic locking. This means that when there are a lot of users, the system concurrency is efficient. 


# Other design decisions 
- We've created an additional class (not part of the domain model): ConcertInfoSubscription, to store the subscription response and the threshold of seats booked to notify subscribers.
- We used Publish/Subscribe asynchronous communication because multiple 'clients' are subscribed to the server (multiple subscription requests), and the server must publish messages to them (notifications), so messaging may be frequent (so not Server-side push) and there won't be long-running tasks (so not Priority scheduling).


These decisions all lead to **scalability** as our choice of locking ensures that concurrency issues are minimised when there are a lot of requests and the way we mitigate Lazy Loading and Eager Fetching ensures that the system can efficiently return a lot of records (while the system scales up).
