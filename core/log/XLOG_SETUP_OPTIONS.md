# Mars XLog Setup Options

## Problem
The `com.tencent.mars:mars-xlog` library is not available in standard Maven repositories (Maven Central, Google Maven, JitPack).

## Solutions

### Option 1: Build Mars XLog from Source (Most Reliable)

1. Clone the Tencent Mars repository:
   ```bash
   git clone https://github.com/Tencent/mars.git
   cd mars
   ```

2. Follow the build instructions in the repository to build the XLog module for Android

3. Copy the generated AAR file to `core/log/libs/`

4. The project is already configured to use local AAR files from the libs directory

### Option 2: Use Timber (Recommended Alternative)

Timber is a popular, well-maintained logging library that's easier to integrate:

1. Update `core/log/build.gradle.kts` to use Timber:
   ```kotlin
   dependencies {
       implementation(libs.timber)
   }
   ```

2. Modify your logging code to use Timber instead of XLog

**Pros:**
- Easy to integrate (available in Maven Central)
- Well-maintained by Jake Wharton
- Simple API
- Good performance

**Cons:**
- Different API than XLog
- May not have all XLog features (like log encryption)

### Option 3: Use Logback-Android

Another alternative with more features:

Add to `libs.versions.toml`:
```toml
logback = "3.0.0"
```

```toml
logback = { group = "com.github.tony19", name = "logback-android", version.ref = "logback" }
```

### Option 4: Manual AAR Download

Some users have reported success downloading pre-built AAR files from:
- Tencent's internal Maven repository (if you have access)
- Third-party mirrors (use with caution)
- Building from an older release tag that has pre-built artifacts

## Current Configuration

The project is currently set up to:
1. Look for AAR files in `core/log/libs/` directory
2. Has Timber available as an alternative (libs.timber)

## Recommendation

If you need XLog specifically for its features (encryption, compression, etc.), build from source.
Otherwise, use Timber for a simpler, more maintainable solution.
