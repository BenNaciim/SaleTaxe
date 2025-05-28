package example.saletaxes;

import example.saletaxes.models.entities.Article;
import example.saletaxes.services.CalculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@SpringBootApplication
@RequiredArgsConstructor
public class SaleTaxesApplication {
    public final CalculationService calculationService;
    Map<Article, Integer> articlesToBuy = new HashMap<>();
    public static void main(String[] args) {
        SpringApplication.run(SaleTaxesApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        List<Article> articleEntities = calculationService.retrieveAll();
        Scanner sc = new Scanner(System.in);
        System.out.println("Please select the article number and the quantity you want to buy (e.g., 1 2 for two of article 1) PRESS 0 to exit:");
        articleEntities.forEach(
                article ->
                        System.out.println(article.getId() + " - " + article.getName() + " - Price : " + article.getPriceBT())
        );

        String input = sc.nextLine();
        while(!input.equals("0") && !input.equals("X")) {
            processInput(input,articleEntities);
            input = sc.nextLine();
        }
        if(input.equals("0")){
            System.out.println("Exiting...");
            return;
        }
        calculationService.calculateTotalPriceWithTaxes(articlesToBuy);

    }

    private void processInput(String input, List<Article> articleEntities) {
        String[] parts = input.split(" ");
        if (parts.length != 2) {
            System.out.println("Invalid input format. Please enter in the format: Number <quantity>");
            return;
        }
        articleEntities.stream()
            .filter(article -> article.getId().equals(Long.parseLong(parts[0])))
            .findFirst()
            .ifPresent(article -> {
                int quantity = Integer.parseInt(parts[1]);
                articlesToBuy.put(article, quantity);
                System.out.println("Added " + quantity + " of " + article.getName() + " to your cart.");
            });
    }

}
