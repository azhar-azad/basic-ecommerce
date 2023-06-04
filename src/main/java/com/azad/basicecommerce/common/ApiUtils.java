package com.azad.basicecommerce.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

@Component
public class ApiUtils {

    @Value("${default_page_number}")
    private int defaultPage;

    @Value("${default_result_limit}")
    private int defaultLimit;

    @Value("${default_sort_order}")
    private String defaultOrder;

    private static final Logger LOG = LoggerFactory.getLogger(ApiUtils.class);

    public void printRequestInfo(String url, String method, String hasAccess) {
        LOG.info("*** REQUEST RECEIVED ***");
        LOG.info("*** URL: " + url + " ***");
        LOG.info("*** Method: " + method.toUpperCase() + " ***");
        LOG.info("*** Access: " + hasAccess + " ***");
    }

    public void logInfo(String log) {
        LOG.info(log);
    }

    public void logDebug(String log) {
        LOG.debug(log);
    }

    public void logError(String log) {
        LOG.error(log);
    }

    public Pageable getPageable(PagingAndSorting ps) {

        if (ps.getSort().isEmpty())
            return PageRequest.of(ps.getPage(), ps.getLimit());
        else
            return PageRequest.of(ps.getPage(), ps.getLimit(), getSortAndOrder(ps.getSort(), ps.getOrder()));
    }

    public String generateUserUid(String email, String username, String firstName, String lastName) {
        return generateUid("user", email, username, firstName, lastName);
    }

    public String generateAddressUid(String addressType, String apartment, String house,
                                     String subDistrict, String district) {
        return generateUid("address", addressType, apartment, house, subDistrict, district);
    }

    public String generateStoreUid(String storeName, String storeOwnerEmail) {
        return generateUid("store", storeName, storeOwnerEmail);
    }

    public String generateWarehouseUid(String warehouseName, String storeName) {
        return generateUid("warehouse", warehouseName, storeName);
    }

    public String generateCategoryUid(String categoryName) {
        return generateUid("category", categoryName);
    }

    public String generateProductUid(String productName, String brand, String price, String storeName) {
        return generateUid("product", productName, brand, price, storeName);
    }

    public String generateRatingUid(String ratingValue, String productUid, String userUid) {
        return generateUid("rating", ratingValue, productUid, userUid);
    }

    public String generateReviewUid(String reviewText, String productUid, String userUid) {
        return generateUid("review", reviewText, productUid, userUid);
    }

    private String generateUid(String entityCode, String... properties) {
        StringBuilder sb = new StringBuilder();
        Arrays.stream(properties).forEach(sb::append);
        return hashSHA256(entityCode, sb + getSecureRandomString());
    }

    private Sort getSortAndOrder(String sort, String order) {

        Sort sortBy = Sort.by(Arrays.stream(sort.split(","))
                .map(String::trim).toArray(String[]::new));

        if (order.equalsIgnoreCase("desc"))
            sortBy = sortBy.descending();
        else
            sortBy = sortBy.ascending();

        return sortBy;
    }

    private String hashSHA256(String entityName, String input) {
        MessageDigest digest; // create SHA-256 hash object
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            LOG.info("Exception on hashSha256 method");
            throw new RuntimeException(e);
        }
        byte[] hash = digest.digest(input.getBytes()); // generate hash as a byte array

        // take first 4 bytes (32 bits) of the hash and convert to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            String hex = Integer.toHexString(hash[i] & 0xFF);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }

        return entityName + "_" + sb;
    }

    private String getSecureRandomString() {
        final String ALPHA_NUMERIC_SYMBOLS =
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                "abcdefghijklmnopqrstuvwxyz" +
                "0123456789!@#$%^&*()_+-=[]{}|;':\",./<>?";

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 32; i++) {
            int index = random.nextInt(ALPHA_NUMERIC_SYMBOLS.length());
            char randomChar = ALPHA_NUMERIC_SYMBOLS.charAt(index);
            sb.append(randomChar);
        }

        return sb.toString();
    }

    private boolean isNumeric(String strValue) {
        return strValue != null && strValue.matches("[0-9.]+");
    }

    private int convertStringToInt(String strValue) {
        try {
            if (strValue == null)
                return -1;
            if (isNumeric(strValue)) {
                return Integer.parseInt(strValue);
            }
            throw new RuntimeException(strValue + " is not numeric");
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
        return -1;
    }
}
