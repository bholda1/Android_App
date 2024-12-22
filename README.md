# Android_App
Tower Defense Android App

### Relevant Code

Here are the main areas of the project:
- [Main Activity Code](app/src/main/java/com/example/groupproject/)
- [UI Layouts](app/src/main/res/layout/)
- [AndroidManifest](app/src/main/AndroidManifest.xml/)

### Firebase Configuration
This project uses Firebase for its backend services. To run the app:
1. Go to the [Firebase Console](https://console.firebase.google.com/).
2. Create a Firebase project and add an Android app with your app’s package name.
3. Download the `google-services.json` file from the Firebase Console.
4. Place the file in the `/app` directory of this project.

### Google ads
- This project uses Google test ads which are fake
- Google play services needs to be installed
### Installing Google Play Services
1. Inside of Android Studio navigate to tools -> SDK Manager
2. Once in Android SDK click on SDK tools
3. Google Play Services should be listed, if not installed check it, click Apply, then OK
4. Google Play Services should now say installed

### Additional info
In Build.gradle you will need to add the dependencies for Firebase and Google Play Services
```Kotlin
dependencies {
    ...
    implementation(platform("com.google.firebase:firebase-bom:33.6.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.android.gms:play-services-ads-base:23.6.0")
}
