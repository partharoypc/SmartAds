package com.partharoypc.smartads;

public enum AdStatus {
    /**
     * The ad manager is idle and ready for a new request.
     */
    IDLE,

    /**
     * The ad is currently fetching content from the network.
     */
    LOADING,

    /**
     * The ad has been successfully loaded and is ready to show.
     */
    LOADED,

    /**
     * The ad failed to load.
     */
    FAILED,

    /**
     * The ad is currently being displayed to the user.
     */
    SHOWN
}
