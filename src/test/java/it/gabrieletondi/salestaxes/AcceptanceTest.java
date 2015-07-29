package it.gabrieletondi.salestaxes;

import it.gabrieletondi.salestaxes.catalog.repository.NeverlandCategoryRepositoryFactory;
import it.gabrieletondi.salestaxes.doubles.InMemoryDisplay;
import it.gabrieletondi.salestaxes.tax.NeverlandTaxPolicyFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AcceptanceTest {

    private POS pos;
    private InMemoryDisplay display;

    @Before
    public void setUp() throws Exception {
        display = new InMemoryDisplay();
        pos = new POS(NeverlandTaxPolicyFactory.build(), display, NeverlandCategoryRepositoryFactory.build());
    }

    @Test
    public void input1() throws Exception {
        pos.onSellCommand("1 book at 12.49");
        pos.onSellCommand("1 music CD at 14.99");
        pos.onSellCommand("1 chocolate bar at 0.85");
        pos.onSaleComplete();

        String expected = "1 book: 12.49\n" +
                "1 music CD: 16.49\n" +
                "1 chocolate bar: 0.85\n" +
                "Sales Taxes: 1.50\n" +
                "Total: 29.83";

        assertEquals(expected, display.getMessage());
    }

    @Test
    public void input2() throws Exception {
        pos.onSellCommand("1 imported box of chocolates at 10.00");
        pos.onSellCommand("1 imported bottle of perfume at 47.50");
        pos.onSaleComplete();

        String expected = "1 imported box of chocolates: 10.50\n" +
                "1 imported bottle of perfume: 54.65\n" +
                "Sales Taxes: 7.65\n" +
                "Total: 65.15";

        assertEquals(expected, display.getMessage());
    }

    @Test
    public void input3() throws Exception {
        pos.onSellCommand("1 imported bottle of perfume at 27.99");
        pos.onSellCommand("1 bottle of perfume at 18.99");
        pos.onSellCommand("1 packet of headache pills at 9.75");
        pos.onSellCommand("1 box of imported chocolates at 11.25");
        pos.onSaleComplete();

        String expected = "1 imported bottle of perfume: 32.19\n" +
                "1 bottle of perfume: 20.89\n" +
                "1 packet of headache pills: 9.75\n" +
                "1 imported box of chocolates: 11.85\n" +
                "Sales Taxes: 6.70\n" +
                "Total: 74.68";

        assertEquals(expected, display.getMessage());
    }
}
