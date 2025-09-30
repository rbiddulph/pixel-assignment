Pixel Assignment
================


Prerequisites
-------------

Android Studio Narwhal 4 Feature Drop | 2025.1.4 RC 1
Gradle 8.12.3
Android 36 SDK


Functionality
-------------

The app uses a service to combine user data from 2 sources - remote StackOverlow API and local storage



TODO
This user data is held in a view model which is referenced by the compose UI
Users can tap a button to follow/unfollow a user in the UI - this changes the local storage state for that user.
Profile images are downloaded from remote server and cached


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



Technical Decisions
-------------------

**User Obj**

Separate UserRemote vs UserLocal - distinction between data from API and from local storage.
Merged into User obj for view model.

**UserService**

Pulls data from sources - current thinking is fetch command for API, toggling of follow on/off for a user, merging users from Remote and Local.
API and toggle to run off IO dispatcher.

**FollowerStorage**

Simple data store with user id vs toggle state (T/F). no entry = assumed false.
Shared Prefs used to avoid needing DataStore library

**Image Downloading**

async server request with local cache if time.

**Testing**

Focus on parsing of user data, merging of user object, service impl and storage of follower data

Storage tests required robolectric to mock context.


--------------------------------------
