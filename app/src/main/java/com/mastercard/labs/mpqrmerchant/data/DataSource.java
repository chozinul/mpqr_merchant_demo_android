package com.mastercard.labs.mpqrmerchant.data;

import com.mastercard.labs.mpqrmerchant.data.model.User;
import com.mastercard.labs.mpqrmerchant.data.model.Transaction;

import java.util.List;

/**
 * @author Muhammad Azeem (muhammad.azeem@mastercard.com) on 2/2/17
 */
public interface DataSource {
    User saveUser(User user);

    User getUser(Long id);

    List<Transaction> getTransactions(Long id);
}
