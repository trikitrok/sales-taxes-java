package it.gabrieletondi.salestaxes.catalog;

import it.gabrieletondi.salestaxes.catalog.repository.CategoryRepository;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CartItem {
    public static final String COMMAND_PATTERN = "^(\\d+)\\s([a-zA-Z\\s]*)\\sat\\s(\\d+\\.\\d{2}?)";

    private final String productName;
    private final BigDecimal netPrice;
    private final int quantity;
    private boolean isImported;
    private Category category;

    public String getProductName() {
        return productName;
    }

    public BigDecimal getNetPrice() {
        return netPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isImported() {
        return isImported;
    }

    public Category getCategory() {
        return category;
    }

    public CartItem(String productName, BigDecimal netPrice, int quantity, boolean isImported, Category category) {
        this.productName = productName;
        this.netPrice = netPrice;
        this.quantity = quantity;
        this.isImported = isImported;
        this.category = category;
    }

    public static CartItem fromSellCommand(String sellCommand, CategoryRepository categoryRepository) {
        Pattern pattern = Pattern.compile(COMMAND_PATTERN);
        Matcher matcher = pattern.matcher(sellCommand);

        matcher.matches();
        int quantity = Integer.parseInt(matcher.group(1));
        String productName = matcher.group(2);
        BigDecimal netPrice = new BigDecimal(matcher.group(3));

        boolean isImported = isImported(productName);
        if (isImported)
            productName = sanitizeImportedProductName(productName);

        Category category = categoryRepository.ofProduct(productName);

        return new CartItem(productName, netPrice, quantity, isImported, category);
    }

    private static String sanitizeImportedProductName(String productName) {
        productName = productName.replace("imported", "");
        productName = productName.replace("  ", " ");
        return productName.trim();
    }

    private static boolean isImported(String productName) {
        return productName.contains("imported");
    }

}
