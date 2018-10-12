# family-map
Family map is a family history application for Android.

This repo contains the code for both the server and the app.

**To run this app you need to do a few things:**
  1. Start the server in ```family-map/server/src/main/java/rbdavis/server/Server.java``` 
      and include a port number for it to listen on. i.e. 8080
  2. I have not made a release APK for the app so you will have to run the app in Android Studio.

Once the server and app are running you should be able to view the app in a connect Android device or an emulator.

## App/Server info and features
#### Info:
- The data used in the app is generated in the server code located in the ```family-map/server``` package.
- Signning up for the app generates 4 generations of family members and basic life events from their 'lives'.

#### Features:
- Tapping on a life event (the map markers), which are color coordinated based on the type of event, do the following:
  - Show info about the person behind the event
  - Connect color coordinated lines between:
    1. Family members
    2. Spouses
    3. Other personal events
- Tapping on the event info window sends you to a personal info screen with info about the person
- Life events, lines, map type, etc. can all be changed by tapping the Settings or Filter icons
