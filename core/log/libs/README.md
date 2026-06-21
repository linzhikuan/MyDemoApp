# Mars XLog Library Files

This directory should contain the Tencent Mars XLog AAR files.

## How to obtain the files:

1. Visit the official Tencent Mars GitHub releases page:
   https://github.com/Tencent/mars/releases

2. Download the latest release (or version 1.2.6 or higher)

3. Extract the following files and place them in this directory:
   - `mars-xlog-sdk-<version>.aar` (or `mars-xlog.aar`)

## Alternative: Build from source

If AAR files are not available in releases, you can build from source:

1. Clone the repository:
   ```bash
   git clone https://github.com/Tencent/mars.git
   ```

2. Follow the build instructions in the Mars repository to build the XLog module

3. Copy the generated AAR file to this directory

## Note:

The project is currently configured to use any `.aar` or `.jar` files in this directory.
Make sure to place the correct Mars XLog library files here for the project to build successfully.
