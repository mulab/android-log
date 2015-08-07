# android-log
Android log library for debugging. It's a wrapper of android.util.Log, and will write logs into logfile, and can be send directly via other applications.

## Usage

In your gradle build file add dependency:
```
compile 'mu.lab:log:1.0.0'
```

Add `Log.init(this)` in your Application's onCreate method:
```
if (BuildConfig.DEBUG) {
  Log.init(this);
}
```
