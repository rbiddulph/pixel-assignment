Pixel Assignment
================


Prerequisites
-------------

Android Studio Narwhal 4 Feature Drop | 2025.1.4 RC 1

Gradle 8.12.3

Android 36 SDK (minimum of SDK 29 - Android 10 (~88% of market))


Functionality
-------------

The app uses a service to combine user data from 2 sources - remote StackOverflow API and local storage

This service is fed into MainViewModel which services the Compose UI view and handles state.

The Compose UI has different states based on availability of data.

Users can tap a button to follow/unfollow a user in the UI - this changes the local storage state for that user.

Profile images are downloaded from remote server and cached in memory for that session (url as key)

Tests
-----

UserRemoteParser

- sample data
- list of 2 items with optional profile image
- empty list

UserTest

- fields merged
- follow state merged
- negative follow state merged

UserServiceImplTest

- data returned by fetch
- data changed by toggle action

FollowerStorageTest

- empty state
- addition from toggle
- removal from toggle

MainViewModelTest

- state begins as Loading
- state is Loaded after loading user data
- state is Failed after loading empty user data


Technical Decisions
-------------------

**Architecture**

MVVM - dumb UI, logic in view model.

**User Obj**

Separate UserRemote vs UserLocal - distinction between data from API and from local storage.
Merged into User obj for view model.
Later on it has become more apparent that UserLocal is only being used by the tests, but it provides some separation of concerns, so will keep as-is.

**UserService**

Pulls data from sources - current thinking is fetch command for API, toggling of follow on/off for a user, merging users from Remote and Local.
API and toggle to run off IO dispatcher.

**FollowerStorage**

Simple data store with user id vs toggle state (T/F). no entry = assumed false.
Shared Prefs used to avoid needing DataStore library. StringSet to keep a list of id's - preferred to option of using key as user id.

**StackOverflowCall**

Initially just building to recognise 200 response as OK and others as failure to keep things simple.

**Image Downloading**

async server request with local cache if time.

**Testing**

Focus on parsing of user data, merging of user object, service impl and storage of follower data

Storage tests required robolectric to mock context. State tests required dispatcher to be available in UserServiceImpl

**View Model**

Simple state engine with 3 states: Loading, Loaded, Failed

**State**

State holds the user list for the Loaded state and failure message for the Failed state. 
Toggle follow/unfollow updates the view model data directly rather than re-querying as we're only using a local storage for the flag, not unreliable.

**Style**

Stackoverflow standard orange chosen with the grey used in their branding. Secondary colour of blue as used in Stack Exchange, but unused in the ui currently.
Rest of colours left as material defaults.

**Screens**

MainActivity was getting a little busy, so moved screens out into their own class files.  
