package proj.concert.service.util;

import javax.ws.rs.container.AsyncResponse;

public class ConcertInfoSubscription {

    private final AsyncResponse response;
    private final int threshold;

    public ConcertInfoSubscription(AsyncResponse response, int threshold) {
        this.response = response;
        this.threshold = threshold;
    }

    public AsyncResponse getResponse() {
        return response;
    }

    public int getThreshold() { return threshold; }

}
