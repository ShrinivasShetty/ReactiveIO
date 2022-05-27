import java.util.concurrent.atomic.AtomicInteger;
import reactor.core.publisher.Mono;

public class Test {

  public static void main(String[] args) {

    AtomicInteger i = new AtomicInteger();
    String nn = Mono.<String>create(s -> {
      if (i.incrementAndGet() == 1) {
        s.success("hey");
      }
      else {
        s.error(new RuntimeException("test " + i));
      }
    }).block();

    System.out.println("The data is...." +nn);
  }

  }

