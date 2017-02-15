package com.mastercard.labs.mpqrmerchant.data;

import com.mastercard.labs.mpqrmerchant.data.model.User;
import com.mastercard.labs.mpqrmerchant.data.model.Transaction;

import java.util.ArrayList;
import java.util.List;

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
    public List<Transaction> getTransactions(Long id) {
        if (id == null) {
            return new ArrayList<>();
        }

        try (Realm realm = Realm.getDefaultInstance()) {
            User user = realm.where(User.class).equalTo("id", id).findFirst();
            if (user == null) {
                return new ArrayList<>();
            } else {
                return realm.copyFromRealm(user.getTransactions());
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
}
