## Map with markers

This is a typical test task for presenting my skills to an employer. Application works with Google
Maps.

### Application requirements

- the application must find the device by geolocation
- markers can be placed on the map
- on a single screen you can view and edit list of markers (add name and annotation to marker)

### Installing

I've used  [Secrets Gradle Plugin for Android](https://github.com/google/secrets-gradle-plugin). For
work you need edit *local.properties* file: add line `GOOGLE_MAPS_API_KEY=Your API key`

### Used

- Kotlin
- ViewModel and LiveData
- Google Maps Platform with Geocoder
- Room - for save and change markers