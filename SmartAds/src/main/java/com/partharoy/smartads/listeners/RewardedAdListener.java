package com.partharoy.smartads.listeners;

public interface RewardedAdListener {
    void onUserEarnedReward();
    void onAdDismissed();
    void onAdFailedToShow(String errorMessage);
}
