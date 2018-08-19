## Big Talk

My implementation of [Big Talk](http://www.makebigtalk.com/]) in an form of Android app.


#### Two reasons for this project to exist:
1. To try latest Android technologies (Kotlin, Jetpack, etc.)
2. To actually use the app.


#### Android stack of technologies used:
- Kotlin
- Hybrid form of MVVM + Clean architecture
- Android Architectural Components, including:
  + ViewModel
  + LiveData
  + Room
- DI with Dagger2


#### Architectural layers:
- View (Activity/Fragment).
- View Model (Android's ViewModel implementation). Talks with View layer through LiveData and directly.
- Repository (custom implementation). View Model uses Repository through an interface, Repository talks to View Model layer through LiveData only.
- Data Sources (custom implementation for local and remote DS). Repository uses DSs through interfaces, DSs talk to Repository through RxJava2 Observables.
  + Local Data Source uses Android's Room ORM library
  + Remote Data Source uses Firebase Cloud Firestore NoSQL library
- Domain (data entities).
- All layers use dependency injection (Dagger2).
