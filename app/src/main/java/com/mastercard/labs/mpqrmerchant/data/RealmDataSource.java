package com.mastercard.labs.mpqrmerchant.data;

import com.mastercard.labs.mpqrmerchant.data.model.Transaction;
import com.mastercard.labs.mpqrmerchant.data.model.User;

import java.util.HashSet;
import java.util.Set;

import io.realm.Realm;

/**
 * @author Muhammad Azeem (muhammad.azeem@mastercard.com) on 2/2/17
 */
public class RealmDataSource implements DataSource {
    private static RealmDataSource dataSource = new RealmDataSource();

    // Prevent direct instantiation.
    private RealmDataSource() {

    }

    public static RealmDataSource getInstance() {
        return dataSource;
    }

    @Override
    public User saveUser(User user) {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.beginTransaction();

            user = realm.copyToRealmOrUpdate(user);

            realm.commitTransaction();

            return realm.copyFromRealm(user);
        }
    }

    @Override
    public User getUser(Long id) {
        if (id == null) {
            return null;
        }

        try (Realm realm = Realm.getDefaultInstance()) {
            User user = realm.where(User.class).equalTo("id", id).findFirst();
            if (user == null) {
                return null;
            } else {
                return realm.copyFromRealm(user);
            }
        }
    }

    @Override
    public Transaction getTransaction(String referenceId) {
        if (referenceId == null) {
            return null;
        }

        try (Realm realm = Realm.getDefaultInstance()) {
            Transaction transaction = realm.where(Transaction.class).equalTo("referenceId", referenceId).findFirst();
            if (transaction == null) {
                return null;
            } else {
                return realm.copyFromRealm(transaction);
            }
        }
    }

    @Override
    public boolean deleteUser(long id) {
        try (Realm realm = Realm.getDefaultInstance()) {
            User user = realm.where(User.class).equalTo("id", id).findFirst();
            if (user == null) {
                return false;
            }

            realm.beginTransaction();

            user.deleteFromRealm();

            realm.commitTransaction();
        }

        return true;
    }

    @Override
    public Transaction saveTransaction(long userId, Transaction transaction) {
        if (transaction == null) {
            return null;
        }

        try (Realm realm = Realm.getDefaultInstance()) {
            User user = realm.where(User.class).equalTo("id", userId).findFirst();
            if (user == null) {
                return null;
            }

            realm.beginTransaction();

            transaction = realm.copyToRealmOrUpdate(transaction);

            // TODO: Figure out proper way of adding items with relationships
            Set<Transaction> transactions = new HashSet<>(user.getTransactions());
            transactions.add(transaction);

            user.getTransactions().clear();
            user.getTransactions().addAll(transactions);

            realm.copyToRealmOrUpdate(user);

            realm.commitTransaction();

            return realm.copyFromRealm(transaction);
        }
    }
}
