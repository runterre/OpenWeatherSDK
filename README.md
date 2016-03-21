# OpenWeatherSDK
Android SDK that uses the OpenWeather API to show the weather at a coordinate.

# Open Weather SDK for Android

You have arrived at the source repository for the Open Weather SDK for Android.  Welcome!

Installation (do this first)
==
Working with this repository requires working with git.  Any workflow that leaves you with a functioning git clone of this repository should set you up for success.  Downloading the ZIP file from GitHub, on the other hand, is likely to put you at a dead end.

## Setting up the repo
First, clone the repo:

- Open the Terminal App
- `cd` to the parent directory where the repo directory will live
- `git clone https://github.com/runterre/OpenWeatherSDK.git`

After cloning the repo:
Open Android Studio, start a new project then click "Import Module" and navigate to the repo directory mentioed above.
Write your app as usual then create a FrameLayout on your layout that you want to show the weather on.

## Adding the code

Add this code to your onCreate method:

  Bundle bundle = new Bundle();<br/>
  bundle.putDouble("lat", lat);<br/>
  bundle.putDouble("lon", lon);<br/>
  getSupportFragmentManager().beginTransaction().add(R.id.overlay, WeatherFragment.newInstance(bundle)).commit();<br/>

In your update event, add this code:

control.setOnMapClickListener(new GoogleMap.OnMapClickListener() {<br/>
&nbsp;&nbsp;@Override<br/>
&nbsp;&nbsp;public void onMapClick(LatLng latLng) {<br/>
&nbsp;&nbsp;&nbsp;&nbsp;lat = latLng.latitude;<br/>
&nbsp;&nbsp;&nbsp;&nbsp;lon = latLng.longitude;<br/>
&nbsp;&nbsp;&nbsp;&nbsp;View view = findViewById(R.id.overlay);<br/>
&nbsp;&nbsp;&nbsp;&nbsp;if (view.getVisibility() == View.GONE) {<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Bundle bundle = new Bundle();
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;bundle.putDouble("lat", lat);<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;bundle.putDouble("lon", lon);<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;WeatherFragment frag = new WeatherFragment();<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;frag.setArguments(bundle);<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;// Replace the weather fragment in the overlay<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;getSupportFragmentanager().beg<code>inTransaction().replace(R.id.overlay, f</code>rag).commit();<br/>
&nbsp;&nbsp;&nbsp;&nbsp;} else {<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;// Hide the fragment<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;view.setVisibility(View.GONE);<br/>
&nbsp;&nbsp;&nbsp;&nbsp;}<br/>
&nbsp;&nbsp;}<br/>
});

Also

@Override<br/>
// This is a callback function from the fragment<br/>
public void onFragmentInteraction(Uri uri) {<br/>
&nbsp;View view = findViewById(R.id.overlay);<br/>
&nbsp;view.setVisibility(View.VISIBLE);<br/>
}
