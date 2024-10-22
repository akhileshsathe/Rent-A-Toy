package com.rentatoy;

import java.util.ArrayList;

public class OrderHelper {
    private String orderKey;
    private String userId;
    private String address;
    private String city;
    private ArrayList<String> cartItems;
    private String email;
    private boolean isApproved;
    private boolean isDelivered;
    private boolean isReturned;

    private Long orderDate;
    private Long orderDeliveryDate;
    private Long orderReturnDate;
    private String upiRef;

    private Long orderAmount;

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    private Long duration;
    private String contact;

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Long getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Long orderAmount) {
        this.orderAmount = orderAmount;
    }

    // Default constructor for Firestore
    public OrderHelper() {
    }

    public OrderHelper(String orderKey, String userId, String address, String city, ArrayList<String> cartItems,
                 String email, boolean isApproved, boolean isDelivered, boolean isReturned, Long orderDate,
                 Long orderDeliveryDate, Long orderReturnDate, String upiRef,Long orderAmount,Long duration,String contact) {
        this.orderKey = orderKey;
        this.userId = userId;
        this.address = address;
        this.city = city;
        this.cartItems = cartItems;
        this.email = email;
        this.isApproved = isApproved;
        this.isDelivered = isDelivered;
        this.isReturned = isReturned;
        this.orderDate = orderDate;
        this.orderDeliveryDate = orderDeliveryDate;
        this.orderReturnDate = orderReturnDate;
        this.upiRef = upiRef;
        this.orderAmount=orderAmount;
        this.duration=duration;
        this.contact=contact;
    }

    public String getOrderKey() {
        return orderKey;
    }

    public String getUserId() {
        return userId;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public ArrayList<String> getCartItems() {
        return cartItems;
    }

    public String getEmail() {
        return email;
    }

    public boolean getIsApproved() {
        return isApproved;
    }

    public boolean getIsDelivered() {
        return isDelivered;
    }

    public boolean getIsReturned() {
        return isReturned;
    }

    public Long getOrderDate() {
        return orderDate;
    }

    public Long getOrderDeliveryDate() {
        return orderDeliveryDate;
    }

    public Long getOrderReturnDate() {
        return orderReturnDate;
    }

    public String getUpiRef() {
        return upiRef;
    }


}
