package it.gabrieletondi.salestaxes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Basket {
    private final InMemoryWithDefaultTaxPolicy taxPolicy;
    private BigDecimal total;
    private BigDecimal salesTaxes;
    private List<BasketItem> items;

    public BigDecimal getTotal() {
        return total;
    }

    public BigDecimal getSalesTaxes() {
        return salesTaxes;
    }

    public Basket(InMemoryWithDefaultTaxPolicy taxPolicy) {
        this.taxPolicy = taxPolicy;
        this.total = BigDecimal.ZERO;
        this.salesTaxes = BigDecimal.ZERO;
        this.items = new ArrayList<BasketItem>();
    }

    public void add(SaleItem saleItem) {
        Tax tax = taxPolicy.forItemName(saleItem.getProductName());
        BigDecimal taxAmount = tax.dutyAmount(saleItem.getNetPrice());
        BigDecimal taxedAmount = saleItem.getNetPrice().add(taxAmount);

        BasketItem basketItem = new BasketItem(saleItem.getProductName(), saleItem.getQuantity(), taxedAmount);
        items.add(basketItem);

        total = total.add(taxedAmount);
        salesTaxes = salesTaxes.add(taxAmount);
    }

    public List<BasketItem> getItems() {
        return items;
    }
}
