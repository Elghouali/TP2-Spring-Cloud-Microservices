package ma.enset.tp2.customer.config;
import ma.enset.tp2.customer.domain.Customer;
import ma.enset.tp2.customer.repo.CustomerRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class DemoData {
  @Bean CommandLineRunner sampleCustomers(CustomerRepository repo,@Value("${app.demo.seed:true}") boolean seed){
    return args->{ if(seed && repo.count()==0){
      repo.save(new Customer("Amina Berrada","amina@example.test"));
      repo.save(new Customer("Youssef Alaoui","youssef@example.test"));
    }};
  }
}
