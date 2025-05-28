package example.saletaxes.services;

import example.saletaxes.models.TaxesRate;
import example.saletaxes.models.entities.Article;
import example.saletaxes.repository.ArticleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CalculationServiceTest {
    @Mock
    private ArticleRepository articleRepository;
    @InjectMocks
    private CalculationService calculationService;

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(calculationService, "articleRepository", articleRepository);
    }

    @Test
    public void testCalculateTax() {
        double tax = calculationService.calculateTax(100.0, 10.0);
        assertEquals(10.0, tax);
    }

    @Test
    public void testCalculateTotalPrice() {
        double total = calculationService.calculateTotalPrice(100.0, 10.0);
        assertEquals(110.0, total);
    }

    @Test
    public void testRoundUpToNearestFiveCents() {
        assertEquals(1.50, CalculationService.roundUpToNearestFiveCents(new java.math.BigDecimal("1.49")).doubleValue());
        assertEquals(1.55, CalculationService.roundUpToNearestFiveCents(new java.math.BigDecimal("1.51")).doubleValue());
    }

    @Test
    @DisplayName("TEST WITH INPUT 1")
    public void testCalculateTotalPriceWithTaxes() {
        // Préparation des articles simulés
        Article article = new Article();
        article.setName("Book");
        article.setPriceBT(12.49);
        article.setImported(false);
        TaxesRate rate= TaxesRate.Exempt;
        article.setTaxesRate(rate);

        Article article1 = new Article();
        article1.setName("Music CD");
        article1.setPriceBT(14.99);
        article1.setImported(false);
        TaxesRate rate1 = TaxesRate.Goods;
        article1.setTaxesRate(rate1);

        Article article2 = new Article();
        article2.setName("Chocolate Bar");
        article2.setPriceBT(0.85);
        article2.setImported(false);
        TaxesRate rate2 = TaxesRate.Exempt;
        article2.setTaxesRate(rate2);

        Map<Article, Integer> cart = new LinkedHashMap<>();
        cart.put(article, 1);
        cart.put(article1, 1);
        cart.put(article2, 1);

        // Capturer la sortie système
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        calculationService.calculateTotalPriceWithTaxes(cart);

        String output = outContent.toString().trim();

        assertTrue(output.contains("1 Book: 12.49"));
        assertTrue(output.contains("Music CD: 16.49"));
        assertTrue(output.contains("Chocolate Bar: 0.85"));
        assertTrue(output.contains("Sales Taxes : 1.50"));
        assertTrue(output.contains("Total: 29.83"));
    }

    @Test
    @DisplayName("TEST WITH INPUT 2")
    public void testCalculateTotalPriceWithTaxes_2() {
        // Préparation des articles simulés
        Article article = new Article();
        article.setName("Imported Box of Chocolates");
        article.setPriceBT(10.00);
        article.setImported(true);
        TaxesRate rate= TaxesRate.Exempt;
        article.setTaxesRate(rate);

        Article article1 = new Article();
        article1.setName("Imported Bottle of Perfume");
        article1.setPriceBT(47.50);
        article1.setImported(true);
        TaxesRate rate1 = TaxesRate.Goods;
        article1.setTaxesRate(rate1);


        Map<Article, Integer> cart = new LinkedHashMap<>();
        cart.put(article, 1);
        cart.put(article1, 1);

        // Capturer la sortie système
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        calculationService.calculateTotalPriceWithTaxes(cart);

        String output = outContent.toString().trim();

        assertTrue(output.contains("1 Imported Box of Chocolates: 10.5"));
        assertTrue(output.contains("1 Imported Bottle of Perfume: 54.65"));
        assertTrue(output.contains("Sales Taxes : 7.65"));
        assertTrue(output.contains("Total: 65.15"));
    }


    @Test
    @DisplayName("TEST WITH INPUT 3")
    public void testCalculateTotalPriceWithTaxes_3() {
        // Préparation des articles simulés
        Article article = new Article();
        article.setName("Imported Bottle of Perfume 1");
        article.setPriceBT(27.99);
        article.setImported(true);
        TaxesRate rate= TaxesRate.Goods;
        article.setTaxesRate(rate);

        Article article1 = new Article();
        article1.setName("bottle of perfume");
        article1.setPriceBT(18.99);
        article1.setImported(false);
        TaxesRate rate1 = TaxesRate.Goods;
        article1.setTaxesRate(rate1);

        Article article2 = new Article();
        article2.setName("packet of headache pills");
        article2.setPriceBT(9.75);
        article2.setImported(false);
        TaxesRate rate2 = TaxesRate.Exempt;
        article2.setTaxesRate(rate2);


        Article article3 = new Article();
        article3.setName("Imported Box of Chocolates 1");
        article3.setPriceBT(11.25);
        article3.setImported(true);
        TaxesRate rate3 = TaxesRate.Exempt;
        article3.setTaxesRate(rate3);


        Map<Article, Integer> cart = new LinkedHashMap<>();
        cart.put(article, 1);
        cart.put(article1, 1);
        cart.put(article2, 1);
        cart.put(article3, 1);

        // Capturer la sortie système
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        calculationService.calculateTotalPriceWithTaxes(cart);

        String output = outContent.toString().trim();

        assertTrue(output.contains("1 Imported Bottle of Perfume 1: 32.19"));
        assertTrue(output.contains("1 bottle of perfume: 20.89"));
        assertTrue(output.contains("1 packet of headache pills: 9.75"));
        assertTrue(output.contains("1 Imported Box of Chocolates 1: 11.85"));
        assertTrue(output.contains("Sales Taxes : 6.70"));
        assertTrue(output.contains("Total: 74.68"));
    }
}

