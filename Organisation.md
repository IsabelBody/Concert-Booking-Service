# Team Members:
- **Isabel Body** / `ibod875` / IsabelBody
- **Sophia Halapchuk** / `shal847` / micattoc
- **Jason Peng** / `jpen687` / jaspeng6


# Summary of each team member’s contributions
**Isabel Body:**
<br><br>**Sophia Halapchuk:** Jackson annotations, Mapper classes, User domain class, Login and Subscription/Notification HTTP methods, Transaction locking for concurrency
<br><br>**Jason Peng:**


# Collaboration practices 
- ERD, planning and design decisions created collaboratively to develop shared project understanding, but implementation done individually 
- weekly meetings were held with code discussion, help & task allocation  
- all code was reviewed through pull requests
- Github Issues & discord were used for ongoing idea discussions & updates 

Please reference both open and closed issues starting with a date (e.g 4/04) to understand the full scope of our planning & collaboration.

# Explanation of Domain Design Choices 
*Short description of how the domain model is organised (2-3 sentences)*

### Relevant Field Data Types Decisions
### Unique Identifiers & Class Relationships. 
### Use of Lazy Loading, Eager Fetching, Cascading 

# Strategy used to minimise the chance of concurrency errors  


# Other design decisions 
- We've created an additional class (not part of the domain model): ConcertInfoSubscription, to store the subscription response and the threshold of seats booked to notify subscribers.
- We used Publish/Subscribe asynchronous communication because multiple 'clients' are subscribed to the server (multiple subscription requests), and the server must publish messages to them (notifications), so messaging may be frequent (so not Server-side push) and there won't be long-running tasks (so not Priority scheduling).

(.. to add after your explanations : "these decisions all lead to **scalability** as our choice of locking ensures that concurrency issues are minimised when there are a lot of requests and the way we mitigate Lazy Loading and Eager Fetching ensures that the system can efficiently return a lot of records (while the system scales up).
