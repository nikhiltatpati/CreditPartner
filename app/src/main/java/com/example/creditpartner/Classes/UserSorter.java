package com.example.creditpartner.Classes;

import java.util.Comparator;

public class UserSorter implements Comparator<Users> {
    @Override
    public int compare(Users o1, Users o2) {
        return o1.getUserName().compareToIgnoreCase(o2.getUserName());
    }
}
