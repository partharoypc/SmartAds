package com.partharoy.smartads;

public class SmartAdsConfig {
    // Ad Unit IDs
    private final String adMobAppId;
    private final String adMobAppOpenId;
    private final String adMobBannerId;
    private final String adMobInterstitialId;
    private final String adMobRewardedId;
    private final String adMobNativeId;
    private final String metaBannerId;
    private final String metaInterstitialId;
    private final String metaRewardedId;
    private final String metaNativeId;

    // Settings
    private final boolean adsEnabled;
    private final boolean isTestMode;
    private final boolean useMetaBackup;
    private final int showAdsAfterDays;
    private final long adShowIntervalMillis;
    private final boolean showLoadingDialog;

    private SmartAdsConfig(Builder builder) {
        this.adMobAppId = builder.adMobAppId;
        this.adMobAppOpenId = builder.adMobAppOpenId;
        this.adMobBannerId = builder.adMobBannerId;
        this.adMobInterstitialId = builder.adMobInterstitialId;
        this.adMobRewardedId = builder.adMobRewardedId;
        this.adMobNativeId = builder.adMobNativeId;
        this.metaBannerId = builder.metaBannerId;
        this.metaInterstitialId = builder.metaInterstitialId;
        this.metaRewardedId = builder.metaRewardedId;
        this.metaNativeId = builder.metaNativeId;
        this.adsEnabled = builder.adsEnabled;
        this.isTestMode = builder.isTestMode;
        this.useMetaBackup = builder.useMetaBackup;
        this.showAdsAfterDays = builder.showAdsAfterDays;
        this.adShowIntervalMillis = builder.adShowIntervalMillis;
        this.showLoadingDialog = builder.showLoadingDialog;
    }

    // Getters
    public String getAdMobAppId() {
        return adMobAppId;
    }

    public String getAdMobAppOpenId() {
        return adMobAppOpenId;
    }

    public String getAdMobBannerId() {
        return adMobBannerId;
    }

    public String getAdMobInterstitialId() {
        return adMobInterstitialId;
    }

    public String getAdMobRewardedId() {
        return adMobRewardedId;
    }

    public String getAdMobNativeId() {
        return adMobNativeId;
    }

    public String getMetaBannerId() {
        return metaBannerId;
    }

    public String getMetaInterstitialId() {
        return metaInterstitialId;
    }

    public String getMetaRewardedId() {
        return metaRewardedId;
    }

    public String getMetaNativeId() {
        return metaNativeId;
    }

    public boolean isAdsEnabled() {
        return adsEnabled;
    }

    public boolean isTestMode() {
        return isTestMode;
    }

    public boolean isUseMetaBackup() {
        return useMetaBackup;
    }

    public int getShowAdsAfterDays() {
        return showAdsAfterDays;
    }

    public long getAdShowIntervalMillis() {
        return adShowIntervalMillis;
    }

    public boolean shouldShowLoadingDialog() {
        return showLoadingDialog;
    }


    public static class Builder {
        private String adMobAppId = "";
        private String adMobAppOpenId = "";
        private String adMobBannerId = "";
        private String adMobInterstitialId = "";
        private String adMobRewardedId = "";
        private String adMobNativeId = "";
        private String metaBannerId = "";
        private String metaInterstitialId = "";
        private String metaRewardedId = "";
        private String metaNativeId = "";
        private boolean adsEnabled = true;
        private boolean isTestMode = false;
        private boolean useMetaBackup = true;
        private int showAdsAfterDays = 0;
        private long adShowIntervalMillis = 60 * 1000L;
        private boolean showLoadingDialog = true;

        public Builder setAdsEnabled(boolean enabled) {
            this.adsEnabled = enabled;
            return this;
        }

        public Builder setAdMobAppId(String id) {
            this.adMobAppId = id;
            return this;
        }

        public Builder setAdMobAppOpenId(String id) {
            this.adMobAppOpenId = id;
            return this;
        }

        public Builder setAdMobBannerId(String id) {
            this.adMobBannerId = id;
            return this;
        }

        public Builder setAdMobInterstitialId(String id) {
            this.adMobInterstitialId = id;
            return this;
        }

        public Builder setAdMobRewardedId(String id) {
            this.adMobRewardedId = id;
            return this;
        }

        public Builder setAdMobNativeId(String id) {
            this.adMobNativeId = id;
            return this;
        }

        public Builder setMetaBannerId(String id) {
            this.metaBannerId = id;
            return this;
        }

        public Builder setMetaInterstitialId(String id) {
            this.metaInterstitialId = id;
            return this;
        }

        public Builder setMetaRewardedId(String id) {
            this.metaRewardedId = id;
            return this;
        }

        public Builder setMetaNativeId(String id) {
            this.metaNativeId = id;
            return this;
        }

        public Builder enableTestMode(boolean testMode) {
            this.isTestMode = testMode;
            return this;
        }

        public Builder setUseMetaBackup(boolean useBackup) {
            this.useMetaBackup = useBackup;
            return this;
        }

        public Builder setShowAdsAfterDays(int days) {
            this.showAdsAfterDays = days;
            return this;
        }

        public Builder setAdShowIntervalMillis(long millis) {
            this.adShowIntervalMillis = millis;
            return this;
        }

        public Builder setShowLoadingDialog(boolean show) {
            this.showLoadingDialog = show;
            return this;
        }

        public SmartAdsConfig build() {
            return new SmartAdsConfig(this);
        }
    }
}
