package com.ufund.api.ufundapi.model;

import java.util.List;

/**
 * Used to wrap users from the FundingBasket into a list of users
 * @author CTRL
 */

public class FundingBasketDataWrapper {
        private List<User> users;

        // Constructor //
        public FundingBasketDataWrapper(List<User> users) {
            this.users = users;
        }

        // Getters //
        public List<User> getUsers() {
            return users;
        }

}
