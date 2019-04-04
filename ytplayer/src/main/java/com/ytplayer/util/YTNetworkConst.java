package com.ytplayer.util;

public interface YTNetworkConst {

    /* To get channels list :*/

    //Get Channels list by forUserName
    String getChannelsListByUsername = "https://www.googleapis.com/youtube/v3/channels?part=snippet,contentDetails,statistics&forUsername=Apple&key=";

    //Get channels list by channel id:
    String getChannelsListByChannelId = "https://www.googleapis.com/youtube/v3/channels/?part=snippet,contentDetails,statistics&id=UCE_M8A5yxnLfW0KghEeajjw&key=";

    //Get Channel sections:
    String getChannelsListByChannelId2 = "https://www.googleapis.com/youtube/v3/channelSections?part=snippet,contentDetails&channelId=UCE_M8A5yxnLfW0KghEeajjw&key=";


    /*To get PlayLists :*/
    //Get PlayLists by Channel ID:
    String getPlayListByChannelId = "https://www.googleapis.com/youtube/v3/playlists?part=snippet,contentDetails&channelId=UCq-Fj5jknLsUf-MWSy4_brA&maxResults=50&key=";

    //Get PlayLists by Channel ID with pageToken:
    String getPlayListByChannelIdAndPageToken = "https://www.googleapis.com/youtube/v3/playlists?part=snippet,contentDetails&channelId=UCq-Fj5jknLsUf-MWSy4_brA&maxResults=50&key=&pageToken=CDIQAA";


    /*To get PlaylistItems :*/
    //Get PlaylistItems list by PlayListId:
    String getPlayListByPlayListId = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet,contentDetails&maxResults=25&playlistId=PLHFlHpPjgk70Yv3kxQvkDEO5n5tMQia5I&key=";


    /*To get videos :*/
    //Get videos list by video id:
    String getVideoListByVideoId = "https://www.googleapis.com/youtube/v3/videos?part=snippet,contentDetails,statistics&id=YxLCwfA1cLw&key=";

    //Get videos list by multiple videos id:
    String getVideoListByMultipleVideoId = "https://www.googleapis.com/youtube/v3/videos?part=snippet,contentDetails,statistics&id=YxLCwfA1cLw,Qgy6LaO3SB0,7yPJXGO2Dcw&key=";


    String getVideoStatistics = "https://www.googleapis.com/youtube/v3/videos?part=statistics&id={{VIDEO-ID}}&key={{YOUR-KEY}}";

}
