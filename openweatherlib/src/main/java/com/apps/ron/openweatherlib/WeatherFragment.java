package com.apps.ron.openweatherlib;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WeatherFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WeatherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeatherFragment extends Fragment {
    // Store the location of the weather API
    private static final String OPEN_WEATHER_MAP_API = "http://api.openweathermap.org/data/2.5/";
    // Store the app id key
    private static final String OPEN_WEATHER_MAP_API_APPID = "7cdcc90545ac43e738e8563392347087";
    private static boolean runWeatherSearch = false;
    private double latitude;
    private double longitude;
    TextView cityField;
    TextView updatedField;
    TextView detailsField;
    TextView currentTemperatureField;
    TextView weatherIcon;

    private OnFragmentInteractionListener mListener;

    public WeatherFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
      * this fragment using the provided parameters.
     */
    public static WeatherFragment newInstance(Bundle bundle) {
        runWeatherSearch = true;
        WeatherFragment fragment = new WeatherFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the coordinates from the calling activity
        Bundle bundle = this.getArguments();
        latitude = bundle.getDouble("lat");
        longitude = bundle.getDouble("lon");
        if (!runWeatherSearch) {
            new GetWeatherTask().execute();
        }
        runWeatherSearch = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_weather, container, false);
        // Get references to the TextViews in the weather fragment
        cityField = (TextView)rootView.findViewById(R.id.city_field);
        updatedField = (TextView)rootView.findViewById(R.id.updated_field);
        detailsField = (TextView)rootView.findViewById(R.id.details_field);
        currentTemperatureField = (TextView)rootView.findViewById(R.id.current_temperature_field);
        weatherIcon = (TextView)rootView.findViewById(R.id.weather_icon);
        // Set the typeface font for the fragment
        weatherIcon.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/weather.ttf"));
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    private void setWeatherIcon(int actualId, long sunrise, long sunset){
        int id = actualId / 100;
        String icon = "";
        if(actualId == 800){
            long currentTime = new Date().getTime();
            if(currentTime>=sunrise && currentTime<sunset) {
                icon = getString(R.string.weather_sunny);
            } else {
                icon = getString(R.string.weather_clear_night);
            }
        } else {
            switch(id) {
                case 2 : icon = getString(R.string.weather_thunder);
                    break;
                case 3 : icon = getString(R.string.weather_drizzle);
                    break;
                case 7 : icon = getString(R.string.weather_foggy);
                    break;
                case 8 : icon = getString(R.string.weather_cloudy);
                    break;
                case 6 : icon = getString(R.string.weather_snowy);
                    break;
                case 5 : icon = getString(R.string.weather_rainy);
                    break;
            }
        }
        weatherIcon.setText(icon);
    }

    public void populateFields(OpenWeatherModel model) {
        cityField.setText(model.getCityName() + ", " + model.getCountryName());
        detailsField.setText(model.getDescription() +
                "\n" + "Humidity: " + model.getHumidity() + "%" +
                "\n" + "Pressure: " + model.getPressure() + " hPa");
        currentTemperatureField.setText(
                String.format("%.2f", model.getCurrentTemp()) + " ℉");
        DateFormat df = DateFormat.getDateTimeInstance();
        String updatedOn = df.format(model.getLastUpdated());
        updatedField.setText("Last update: " + updatedOn);
        setWeatherIcon(model.getId(),
                model.getSunrise() * 1000,
                model.getSunset() * 1000);
        mListener.onFragmentInteraction(Uri.parse("http://test"));
    }

    /**
     * A simple {@link AsyncTask} subclass.
     * Use the {@link new GetForecastTask.execute()} method to initialize and run this task
     */
    private class GetWeatherTask extends AsyncTask<String, String, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... args) {
            // Create an instance of the JSONParser
            JSONParser jParser = new JSONParser();
            String url = OPEN_WEATHER_MAP_API + "weather?units=imperial&lat=" + latitude + "&lon=" + longitude + "&APPID=" + OPEN_WEATHER_MAP_API_APPID;

            // Getting JSON from URL
            JSONObject json = jParser.getJSONFromUrl(url);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                if (json.getString("cod").equals("200")) {
                    // Parse the json result
                    JSONObject details = json.getJSONArray("weather").getJSONObject(0);
                    JSONObject main = json.getJSONObject("main");
                    OpenWeatherModel weatherModel = new OpenWeatherModel();
                    weatherModel.setCityName(json.getString("name").toUpperCase(Locale.US));
                    weatherModel.setCountryName(json.getJSONObject("sys").getString("country"));
                    weatherModel.setDescription(details.getString("description").toUpperCase(Locale.US));
                    weatherModel.setHumidity(main.getString("humidity"));
                    weatherModel.setPressure(main.getString("pressure"));
                    weatherModel.setCurrentTemp(main.getDouble("temp"));
                    weatherModel.setLastUpdated(new Date(json.getLong("dt") * 1000));
                    weatherModel.setId(details.getInt("id"));
                    weatherModel.setSunrise(json.getJSONObject("sys").getLong("sunrise") * 1000);
                    weatherModel.setSunset(json.getJSONObject("sys").getLong("sunset") * 1000);
                    populateFields(weatherModel);
                }
                else {
                    Log.e("GetWeatherTask", json.getString("message"));
                }
            }catch(Exception e){
                Log.e("GetWeatherTask", "One or more fields not found in the JSON data");
                Log.e("GetWeatherTask", e.getMessage());
            }
        }
    }
}
