import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Random;

public class ReactorBatchMain {
    public static void main(String[] args) throws IOException {
        final Random rnd = new Random();
        //Flux.interval(Duration.ofMillis(1)).map(x -> "Msg number " + x).subscribe(x -> System.out.println("" + x));

        final Flux<String> stringFlux = Flux.interval(Duration.ofMillis(1)).map(x -> "Msg number " + x);

        final Flux<List<String>> stringFluxMicrobatched = stringFlux
                .bufferTimeout(100, Duration.ofNanos(1));

        stringFluxMicrobatched.subscribe(strings -> {
            // Batch insert into DB
            System.out.print("Inserting in batch " + strings.size() + " strings.");
            try {
                // Inserting into db is simulated by 10 to 40 ms sleep here...
                Thread.sleep(rnd.nextInt(30) + 10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(" ... Done");
        });

        System.out.println("press enter to finish");
        System.in.read();
    }
}
