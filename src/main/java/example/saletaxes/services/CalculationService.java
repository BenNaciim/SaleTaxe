package example.saletaxes.services;

import example.saletaxes.models.TaxesRate;
import example.saletaxes.models.entities.Article;
import example.saletaxes.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CalculationService {

    private final ArticleRepository articleRepository;

    public double calculateTax(double price, double taxRate) {
        return price * (taxRate / 100);
    }

    public double calculateTotalPrice(double price, double tax) {
        return price + tax;
    }

    public List<Article> retrieveAll() {
        return articleRepository.findAll();
    }

    /**
     * Calculates the total price including taxes for a given map of articles and their quantities.
     *
     * @param articlesToBuy A map where the key is an {@link Article} and the value is the quantity to purchase.
     *                      The method calculates the total price and tax for each article and prints the results.
     */

    public void calculateTotalPriceWithTaxes(Map<Article, Integer> articlesToBuy) {
        double totalPrice = 0.0;
        double totalTax = 0.0;
        for (Map.Entry<Article, Integer> entry : articlesToBuy.entrySet()) {
            Article article = entry.getKey();
            int quantity = entry.getValue();
            double priceBT = article.getPriceBT();
            double taxRate = article.isImported() ? article.getTaxesRate().getRate() + TaxesRate.Imported.getRate() : article.getTaxesRate().getRate();
            double tax = roundUpToNearestFiveCents(BigDecimal.valueOf(calculateTax(priceBT, taxRate))).doubleValue();
            double priceTTC = BigDecimal.valueOf(calculateTotalPrice(priceBT, tax) * quantity).setScale(2, RoundingMode.HALF_UP).doubleValue();
            totalPrice += priceTTC;
            totalTax += tax;

            System.out.println(quantity+" "+ article.getName()+": "+ priceTTC);
        }
        totalPrice = BigDecimal.valueOf(totalPrice).setScale(2, RoundingMode.HALF_UP).doubleValue();
        totalTax = BigDecimal.valueOf(totalTax).setScale(2, RoundingMode.HALF_UP).doubleValue();
        System.out.println("Sales Taxes : "+ roundUpToNearestFiveCents(BigDecimal.valueOf(totalTax)) + " Total: " + totalPrice);
    }


    public static BigDecimal roundUpToNearestFiveCents(BigDecimal amount) {
        BigDecimal increment = new BigDecimal("0.05");

        // Divide amount by 0.05, round up, then multiply back
        BigDecimal divided = amount.divide(increment, 0, RoundingMode.UP);

        return divided.multiply(increment);
    }
}
