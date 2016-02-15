# RSSReader
A reader application for RSS. Gather some famous rss sources such as douban, cnbeta, coolshell and so on. User could add personal interested sites and collect articles.
The main work flow of the rssreader is : 
When user clicks a site button,load the items from cache and download the updated contents in background thread. The downloaded work firstly curls the rss xml file from its url, then stores the result in the cache after resolving the xml. 
