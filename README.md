# class-schedule
FALL15
##Week 4
### Becky: OCT 23, 2015 HttpURLConnection
**Log**
`v(String, String)` (verbose)
`d(String, String)` (debug)
`i(String, String)` (information)
`w(String, String)` (warning)
`e(String, String)` (error)
Example:
>Log.i("MyActivity", "MyClass.getView() — get item number " + position);

The LogCat will then output something like:
>I/MyActivity( 1557): MyClass.getView() — get item number 1

`adb logcat` 
(To use this, might need to add the path to .bash_profile: ` echo 'export PATH=~/Library/Android/sdk/platform-tools:$PATH' > ~/.bash_profile`)


**HttpURLConnetion**
Http request and download web page

>http://developer.android.com/training/basics/network-ops/connecting.html#AsyncTask 

Convert InputStream to String
>tringWriter writer = new StringWriter();
>IOUtils.copy(is, writer, "UTF-8");
>String contentAsString = writer.toString();

Import IOUtils in Android Studio
>In file build.gradle (Module: app), add the following line: 
>dependencies {
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;...
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;compile 'commons-io:commons-io:+'
>}

